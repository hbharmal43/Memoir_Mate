package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.Comment;

public interface CommentsCallback {
    void onCallback(List<Comment> comments);
}