package vukan.com.fftd.ui.post_image;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.firebase.Storage;

public class PostImageFragment extends Fragment {
    ImageView slika;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slika = view.findViewById(R.id.slika);
        Storage storage = new Storage();

        if (getArguments() != null)
            GlideApp.with(slika.getContext())
                    .load(storage.getPostImage(PostImageFragmentArgs.fromBundle(getArguments()).getImageUrl()))
                    .into(slika);
    }
}