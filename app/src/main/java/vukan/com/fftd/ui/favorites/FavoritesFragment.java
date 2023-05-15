package vukan.com.fftd.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vukan.com.fftd.R;
import vukan.com.fftd.adapters.PostRecyclerViewAdapter;

public class FavoritesFragment extends Fragment implements PostRecyclerViewAdapter.ListItemClickListener {
    private PostRecyclerViewAdapter adapter;
    FavoritesViewModel favoritesViewModel;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().setTitle(getString(R.string.favorites));
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recycler_view_omiljeni);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        getPosts();
    }

    @Override
    public void onListItemClick(String postID) {
        FavoritesFragmentDirections.OmiljeniToProizvodFragmentAction action = FavoritesFragmentDirections.omiljeniToProizvodFragmentAction();
        action.setPostId(postID);
        Navigation.findNavController(requireView()).navigate(action);
    }


    void getPosts() {
        favoritesViewModel.getFavouritePosts().observe(getViewLifecycleOwner(), posts -> {
            adapter.setPosts(posts);
            recyclerView.setAdapter(adapter);
        });
    }
}