package vukan.com.fftd.callbacks;

import java.util.List;

import vukan.com.fftd.models.Post;

public interface PostsCallback {
    void onCallback(List<Post> posts);
}