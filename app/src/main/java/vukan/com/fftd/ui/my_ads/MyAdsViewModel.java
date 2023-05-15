package vukan.com.fftd.ui.my_ads;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vukan.com.fftd.models.Comment;
import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.User;
import vukan.com.fftd.repository.Repository;

public class MyAdsViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<User> mUser;
    private MutableLiveData<Float> mUserRating;
    private MutableLiveData<List<Post>> mPost;
    private MutableLiveData<List<Comment>> mComments;

    public MyAdsViewModel() {
        repository = new Repository();
        mUser = new MutableLiveData<>();
        mPost = new MutableLiveData<>();
        mUserRating = new MutableLiveData<>();
        mComments = new MutableLiveData<>();
    }

    public void reportUser(String userID) {
        repository.reportUser(userID);
    }

    public void updateProfilePicture(Uri imageUrl) {
        repository.updateProfilePicture(imageUrl);
    }

    public void updateProfilePictureBitmap(Bitmap imageBitmap) {
        repository.updateProfilePictureBitmap(imageBitmap);
    }

    public MutableLiveData<User> getUser(String userID) {
        mUser = repository.getUser(userID);
        return mUser;
    }

    public MutableLiveData<Float> getUserRating(String userID) {
        mUserRating = repository.getUserRating(userID);
        return mUserRating;
    }

    public void deleteUserData(String userID) {
        repository.deleteUserData(userID);
    }

    public void addNewUserComment(Comment newComment) {
        repository.addNewUserComment(newComment);
    }

    public void addUser() {
        repository.addUser();
    }

    MutableLiveData<List<Post>> getUserPosts(String userID) {
        mPost = repository.getUserPosts(userID);
        return mPost;
    }

    MutableLiveData<List<Comment>> getUserComments(String userID) {
        mComments = repository.getUserComments(userID);
        return mComments;
    }

    public void editUserInfo(User user) {
        repository.editUserInfo(user);
    }
}