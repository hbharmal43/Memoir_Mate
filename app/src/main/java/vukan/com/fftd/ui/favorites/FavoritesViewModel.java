package vukan.com.fftd.ui.favorites;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vukan.com.fftd.models.Post;
import vukan.com.fftd.repository.Repository;

public class FavoritesViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<Post>> mPosts;

    public FavoritesViewModel() {
        repository = new Repository();
        mPosts = new MutableLiveData<>();
    }

    MutableLiveData<List<Post>> getFavouritePosts() {
        mPosts = repository.getFavouritePosts();
        return mPosts;
    }

    void removePostFromFavourites(String postID) {
        repository.removePostFromFavourites(postID);
    }
}