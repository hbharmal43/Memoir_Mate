package vukan.com.fftd.models;

import com.google.firebase.Timestamp;

public class Post {
    private String postID;
    private String name;
    private String description;
    private Double price;
    private Timestamp datetime;
    private String categoryID;
    private String userID;
    private String homePhotoUrl;
    private Long seen;

    public Post() {
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHomePhotoUrl() {
        return homePhotoUrl;
    }

    public void setHomePhotoUrl(String homePhotoUrl) {
        this.homePhotoUrl = homePhotoUrl;
    }

    public Long getSeen() {
        return seen;
    }

    public void setSeen(Long seen) {
        this.seen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postID.equals(post.postID) &&
                name.equals(post.name) &&
                description.equals(post.description) &&
                price.equals(post.price) &&
                categoryID.equals(post.categoryID) &&
                datetime.equals(post.datetime) &&
                userID.equals(post.userID) &&
                homePhotoUrl.equals(post.homePhotoUrl) &&
                seen.equals(post.seen);
    }
}
