package vukan.com.fftd.ui.post;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.PostCategory;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.models.User;
import vukan.com.fftd.repository.Repository;

public class PostViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<List<PostImage>> mPostImages;
    private MutableLiveData<Post> mPostDetails;
    private MutableLiveData<PostCategory> mPostCategory;
    private MutableLiveData<User> mPostUser;

    public PostViewModel() {
        repository = new Repository();
        mPostUser = new MutableLiveData<>();
        mPostImages = new MutableLiveData<>();
        mPostDetails = new MutableLiveData<>();
        mPostCategory = new MutableLiveData<>();
    }

    public MutableLiveData<List<PostImage>> getPostImages(String id) {
        mPostImages = repository.getPostImages(id);
        return mPostImages;
    }

    public MutableLiveData<Post> getPostDetails(String id) {
        mPostDetails = repository.getPostDetails(id);
        return mPostDetails;
    }

    MutableLiveData<User> getPostUser(String id) {
        mPostUser = repository.getPostUser(id);
        return mPostUser;
    }

    MutableLiveData<PostCategory> getCategory(String id) {
        mPostCategory = repository.getCategory(id);
        return mPostCategory;
    }

    void deletePost(String id) {
        repository.deletePost(id);
    }

    public void incrementCounter(String id) {
        repository.incrementCounter(id);
    }

    public void reportAd(String postID) {
        repository.reportAd(postID);
    }
}