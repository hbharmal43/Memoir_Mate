package vukan.com.fftd.ui.new_ad_window;

import androidx.lifecycle.ViewModel;

import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.repository.Repository;

public class NewAdWindowViewModel extends ViewModel {
    private final Repository repository;

    public NewAdWindowViewModel() {
        repository = new Repository();
    }

    String addPost(Post p, String postID) {
        return repository.addPost(p, postID);
    }

    void addPostImage(PostImage pi) {
        repository.addPostImage(pi);
    }

    void deletePostImage(String url) {
        repository.deletePostImage(url);
    }
}