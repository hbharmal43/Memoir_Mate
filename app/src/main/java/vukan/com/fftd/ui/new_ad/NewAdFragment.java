package vukan.com.fftd.ui.new_ad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import vukan.com.fftd.R;

public class NewAdFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(getString(R.string.title_create));
        final TextView textView = view.findViewById(R.id.text_novioglas);
        textView.setText(R.string.title_newpost);
        Animation mAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade);
        mAnimation.setDuration(150);
        ImageView odeca = view.findViewById(R.id.odeca);
        ImageView obuca = view.findViewById(R.id.obuca);
        ImageView aksesoari = view.findViewById(R.id.aksesoari);
        ImageView mobilni = view.findViewById(R.id.mobilni);

        odeca.setOnClickListener(v -> {
            v.startAnimation(mAnimation);
            NewAdFragmentDirections.NoviOglasToNovioglasprozorFragmentAction action = NewAdFragmentDirections.noviOglasToNovioglasprozorFragmentAction();
            action.setId(1);
            Navigation.findNavController(v).navigate(action);
        });

        obuca.setOnClickListener(v -> {
            v.startAnimation(mAnimation);
            NewAdFragmentDirections.NoviOglasToNovioglasprozorFragmentAction action = NewAdFragmentDirections.noviOglasToNovioglasprozorFragmentAction();
            action.setId(2);
            Navigation.findNavController(v).navigate(action);
        });

        aksesoari.setOnClickListener(v -> {
            v.startAnimation(mAnimation);
            NewAdFragmentDirections.NoviOglasToNovioglasprozorFragmentAction action = NewAdFragmentDirections.noviOglasToNovioglasprozorFragmentAction();
            action.setId(3);
            Navigation.findNavController(v).navigate(action);
        });


        mobilni.setOnClickListener(v -> {
            v.startAnimation(mAnimation);
            NewAdFragmentDirections.NoviOglasToNovioglasprozorFragmentAction action = NewAdFragmentDirections.noviOglasToNovioglasprozorFragmentAction();
            action.setId(13);
            Navigation.findNavController(v).navigate(action);
        });

    }
}