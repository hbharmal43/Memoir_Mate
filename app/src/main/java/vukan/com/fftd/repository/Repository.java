package vukan.com.fftd.repository;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vukan.com.fftd.R;
import vukan.com.fftd.firebase.Database;
import vukan.com.fftd.models.Comment;
import vukan.com.fftd.models.Conv;
import vukan.com.fftd.models.FavoritePost;
import vukan.com.fftd.models.Message;
import vukan.com.fftd.models.Post;
import vukan.com.fftd.models.PostCategory;
import vukan.com.fftd.models.PostImage;
import vukan.com.fftd.models.User;

public class Repository {
    private final Database database;
    private final MutableLiveData<List<Post>> mPosts;
    private List<FavoritePost> mFavouritesPosts;
    private final MutableLiveData<List<PostCategory>> mCategories;
    private final MutableLiveData<Post> mPost;
    private final MutableLiveData<List<PostImage>> mPostImages;
    private final MutableLiveData<PostCategory> mPostCategory;
    private final MutableLiveData<User> mUser;
    private final MutableLiveData<Float> mUserRating;
    private final MutableLiveData<List<Post>> mUserPosts;
    private FirebaseUser user;
    private final MutableLiveData<List<Message>> mMessages;
    private final MutableLiveData<List<Conv>> mConv;
    private final MutableLiveData<User> mPostUser;
    private final MutableLiveData<List<Comment>> mUserComments;

    public Repository() {
        database = new Database();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFavouritesPosts = new ArrayList<>();
        mPostUser = new MutableLiveData<>();
        mPostCategory = new MutableLiveData<>();
        mPosts = new MutableLiveData<>();
        mUserRating = new MutableLiveData<>();
        mCategories = new MutableLiveData<>();
        mPost = new MutableLiveData<>();
        mPostImages = new MutableLiveData<>();
        mUser = new MutableLiveData<>();
        mMessages = new MutableLiveData<>();
        mConv = new MutableLiveData<>();
        mUserPosts = new MutableLiveData<>();
        mUserComments = new MutableLiveData<>();
    }

    public void reportUser(String userID) {
        database.reportUser(userID);
    }

    public void deleteConversation(Conv conv) {
        database.deleteConversation(conv);
    }

    public void deletePostImage(String url) {
        database.deletePostImage(url);
    }

    public void deleteUserData(String userID) {
        database.deleteUserData(userID);
    }

    public void deletePost(String id) {
        database.deletePost(id);
    }

    public MutableLiveData<List<PostCategory>> getCategories() {
        database.getCategories(mCategories::setValue);
        return mCategories;
    }

    public MutableLiveData<User> getPostUser(String id) {
        database.getPostUser(id, mPostUser::setValue);
        return mPostUser;
    }

    public void addUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database.addUser(Objects.requireNonNull(user));
    }

    public void updateProfilePicture(Uri imageUrl) {
        database.updateProfilePicture(imageUrl);
    }

    public void updateProfilePictureBitmap(Bitmap imageBitmap) {
        database.updateProfilePictureBitmap(imageBitmap);
    }

    public void sendMessage(Message m) {
        database.sendMessage(m);
    }

    public String addPost(Post p, String postID) {
        return database.addPost(p, postID);
    }

    public void addPostImage(PostImage pi) {
        database.addPostImage(pi);
    }

    public MutableLiveData<List<Message>> getUserMessages(String sender, String receiver, String postID) {
        database.getUserMessages(sender, receiver, postID, mMessages::setValue);
        return mMessages;
    }

    public MutableLiveData<List<Conv>> getAllUserMessages(String sender) {
        database.getAllUserMessages(sender, mConv::setValue);
        return mConv;
    }

    public MutableLiveData<List<Post>> getPosts() {
        database.getPosts(mPosts::setValue);
        return mPosts;
    }

    public MutableLiveData<List<Post>> getFavouritePosts() {
        List<Post> posts = new ArrayList<>();
        mPosts.setValue(posts);

        database.getFavouritePosts(user.getUid(), favoritesPosts -> {
            mFavouritesPosts = favoritesPosts;
            for (FavoritePost post : mFavouritesPosts) {
                database.getPost(post.getPostID(), favouritePost -> {
                    posts.add(favouritePost);
                    mPosts.setValue(posts);
                });
            }
        });

        return mPosts;
    }

    public MutableLiveData<List<Post>> filterPosts(String[] filters) {
        database.filterPosts(filters, mPosts::setValue);
        return mPosts;
    }

    public void isFavourite(String postID, View v) {
        if (user != null) {
            database.isFavourite(postID, user.getUid(), favourite -> {
                if (favourite)
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_star));
                else
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_star_border));
            });
        }
    }

    public void incrementCounter(String id) {
        database.incrementCounter(id, user.getUid());
    }

    public MutableLiveData<Post> getPostDetails(String id) {
        database.getPostDetails(id, mPost::setValue);
        return mPost;
    }

    public MutableLiveData<List<PostImage>> getPostImages(String id) {
        database.getPostImages(id, mPostImages::setValue);
        return mPostImages;
    }

    public MutableLiveData<User> getUser(String userID) {
        database.getUser(userID, mUser::setValue);
        return mUser;
    }

    public MutableLiveData<Float> getUserRating(String userID) {
        database.getUserRating(userID, mUserRating::setValue);
        return mUserRating;
    }

    public MutableLiveData<PostCategory> getCategory(String id) {
        database.getCategory(id, mPostCategory::setValue);
        return mPostCategory;
    }


    public MutableLiveData<List<Post>> getUserPosts(String userID) {
        database.getUserPosts(userID, mUserPosts::setValue);
        return mUserPosts;
    }

    public void addNewUserComment(Comment newComment) {
        database.addUserComment(newComment);
    }

    public MutableLiveData<List<Comment>> getUserComments(String userID) {
        database.getUserComments(userID, mUserComments::setValue);
        return mUserComments;
    }

    public void addPostToFavourites(String postID) {
        database.addPostToFavourites(postID, user.getUid());
    }

    public void removePostFromFavourites(String postID) {
        database.removePostFromFavourites(postID, user.getUid());
    }

    public void editUserInfo(User user) {
        database.editUserInfo(user);
    }

    public void reportAd(String postID) {
        database.reportAd(postID);
    }
}