package vukan.com.fftd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vukan.com.fftd.R;
import vukan.com.fftd.models.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
    private final List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        final TextView comment;
        final RatingBar ratingBar;

        CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        void bind(int index) {
            comment.setText(comments.get(index).getComment());
            ratingBar.setRating(comments.get(index).getGrade());
        }
    }
}