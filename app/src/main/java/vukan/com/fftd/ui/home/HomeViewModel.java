package vukan.com.fftd.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vukan.com.fftd.models.Post;
import vukan.com.fftd.repository.Repository;

public class HomeViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Post>> mPosts;

    public HomeViewModel() {
        repository = new Repository();
        mPosts = new MutableLiveData<>();
    }

    MutableLiveData<List<Post>> getPosts() {
        mPosts = repository.getPosts();
        return mPosts;
    }

    MutableLiveData<List<Post>> filterPosts(String[] filters) {
        mPosts = repository.filterPosts(filters);
        return mPosts;
    }

    void addPostToFavourites(String postID) {
        repository.addPostToFavourites(postID);
    }

    void removePostFromFavourites(String postID) {
        repository.removePostFromFavourites(postID);
    }
}