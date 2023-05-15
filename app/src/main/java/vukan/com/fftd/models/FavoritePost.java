package vukan.com.fftd.models;

public class FavoritePost {
    private String postID;
    private String userID;

    public FavoritePost() {
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}