package vukan.com.fftd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.firebase.Storage;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.ui.post.PostFragmentDirections;

public class PostImageRecyclerViewAdapter extends SliderViewAdapter<PostImageRecyclerViewAdapter.PostViewHolder> {
    private final Storage storage;
    private final List<PostImage> posts;

    public PostImageRecyclerViewAdapter(List<PostImage> posts) {
        this.posts = posts;
        storage = new Storage();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(position);

        holder.itemView.setOnClickListener(view -> {
            PostFragmentDirections.ProizvodToSlikaFragmentAction action = PostFragmentDirections.proizvodToSlikaFragmentAction();
            action.setImageUrl(posts.get(position).getImageUrl());
            Navigation.findNavController(view).navigate(action);
        });
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    class PostViewHolder extends SliderViewAdapter.ViewHolder {
        final ImageView postImage;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.slika_proizvoda);
        }

        void bind(int index) {
            GlideApp.with(postImage.getContext())
                    .load(storage.getPostImage(posts.get(index).getImageUrl()))
                    .into(postImage);
        }
    }
}