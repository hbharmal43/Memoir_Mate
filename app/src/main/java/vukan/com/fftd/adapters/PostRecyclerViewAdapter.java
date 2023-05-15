package vukan.com.fftd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vukan.com.fftd.GlideApp;
import vukan.com.fftd.R;
import vukan.com.fftd.firebase.Storage;
import vukan.com.fftd.models.Post;
import vukan.com.fftd.repository.Repository;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder> {
    private final Storage storage;
    private final Repository repository;
    private List<Post> posts;
    private final List<Post> postsCopy = new ArrayList<>();
    final private ListItemClickListener mOnClickListener;

    public PostRecyclerViewAdapter(ListItemClickListener listener) {
        posts = new ArrayList<>();
        repository = new Repository();
        postsCopy.addAll(posts);
        storage = new Storage();
        mOnClickListener = listener;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        postsCopy.clear();
        postsCopy.addAll(posts);
    }

    public void filter(String text) {
        posts.clear();
        if (text.isEmpty()) posts.addAll(postsCopy);
        else {
            text = text.toLowerCase();

            for (Post item : postsCopy) {
                if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text))
                    posts.add(item);
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bind(position);
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView postName;
        final ImageView postImage;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postName = itemView.findViewById(R.id.post_name);
            postImage = itemView.findViewById(R.id.post_image);
            postImage.setOnClickListener(this);
        }

        void bind(int index) {
            postName.setText(posts.get(index).getName());

            try {
                GlideApp.with(postImage.getContext())
                        .load(storage.getPostImage(posts.get(index).getHomePhotoUrl()))
                        .useAnimationPool(false)
                        .placeholder(R.drawable.ic_image)
                        .dontAnimate()
                        .into(postImage);
            } catch (Exception e) {
                e.printStackTrace();
                postImage.setImageResource(R.drawable.ic_image);
            }

        }

        @Override
        public void onClick(View v) {
            int i = getBindingAdapterPosition();

            if (v instanceof ImageView)
                mOnClickListener.onListItemClick(posts.get(i).getPostID());

        }
    }
    public interface ListItemClickListener {
        void onListItemClick(String postID);

    }
}
