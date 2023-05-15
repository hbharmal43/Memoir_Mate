package vukan.com.fftd.models;

public class User {
    private String userID;
    private String username;
    private String location;
    private Float grade;
    private String phone;
    private String imageUrl;

    public User() {
    }

    public User(String userID, String username, String location, Float grade, String phone, String imageUrl) {
        this.userID = userID;
        this.username = username;
        this.location = location;
        this.grade = grade;
        this.imageUrl = imageUrl;
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}