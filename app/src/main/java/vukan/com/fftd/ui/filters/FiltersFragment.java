package vukan.com.fftd.ui.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import vukan.com.fftd.R;
import vukan.com.fftd.models.PostCategory;

public class FiltersFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private TextView cenaOd;
    private TextView cenaDo;
    private Spinner kategorije;
    private RadioButton opadajuce;
    private RadioButton rastuce;
    public static String[] filters = new String[4];
    private List<PostCategory> categories;
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        filters = new String[4];
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(getString(R.string.filters));
        cenaOd = view.findViewById(R.id.cenaOd);
        cenaDo = view.findViewById(R.id.cenaDo);
        Button primeni = view.findViewById(R.id.primeni);
        opadajuce = view.findViewById(R.id.opadajuce);
        rastuce = view.findViewById(R.id.rastuce);
        FiltersViewModel filtersViewModel = new ViewModelProvider(this).get(FiltersViewModel.class);
        kategorije = view.findViewById(R.id.kategorija);

        filtersViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            this.categories = categories;
            List<String> list = new ArrayList<>();
            list.add(getString(R.string.sve));
            for (PostCategory category : this.categories) list.add(category.getName());
            adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            kategorije.setOnItemSelectedListener(this);
            kategorije.setAdapter(adapter);
        });

        primeni.setOnClickListener(view1 -> {
            if (!cenaOd.getText().toString().isEmpty() && !cenaDo.getText().toString().isEmpty() && Integer.parseInt(cenaOd.getText().toString()) > Integer.parseInt(cenaDo.getText().toString())) {
                Toast.makeText(getContext(), R.string.invalid_prices, Toast.LENGTH_SHORT).show();
            } else {
                if (!cenaOd.getText().toString().isEmpty())
                    filters[0] = cenaOd.getText().toString();

                if (!cenaDo.getText().toString().isEmpty())
                    filters[1] = cenaDo.getText().toString();

                if (opadajuce.isChecked())
                    filters[2] = "opadajuce";
                else if (rastuce.isChecked())
                    filters[2] = "rastuce";

                FiltersFragmentDirections.FilteriToPocetnaFragmentAction action = FiltersFragmentDirections.filteriToPocetnaFragmentAction();
                action.setFilters(filters);
                Navigation.findNavController(requireView()).navigate(action);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        for (PostCategory category : categories) {
            if (adapterView.getItemAtPosition(i).toString().equals(category.getName()))
                filters[3] = category.getCategoryID();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}