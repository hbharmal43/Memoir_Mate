package vukan.com.fftd.models;

import java.util.ArrayList;
import java.util.List;

public class Conv {
    private final List<Message> messages;
    private String postName;
    private String userName;

    public Conv() {
        messages = new ArrayList<>();
        postName = "";
        userName = "";
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Message> getMessages() {
        return messages;
    }
}