package vukan.com.fftd.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

import java.util.Objects;

public class Message implements Parcelable {
    private String content;
    private Timestamp dateTime;
    private String postID;
    private String senderID;
    private String receiverID;

    public Message() {
    }

    public Message(String content, Timestamp dateTime, String postID, String senderID, String receiverID) {
        this.content = content;
        this.dateTime = dateTime;
        this.postID = postID;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    @NonNull
    @Override
    public String toString() {
        return senderID + content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return content.equals(message.content) &&
                dateTime.equals(message.dateTime) &&
                postID.equals(message.postID) &&
                senderID.equals(message.senderID) &&
                receiverID.equals(message.receiverID);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(content, dateTime, postID, senderID, receiverID);
    }

    protected Message(Parcel in) {
        content = in.readString();
        dateTime = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        postID = in.readString();
        senderID = in.readString();
        receiverID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeValue(dateTime);
        dest.writeString(postID);
        dest.writeString(senderID);
        dest.writeString(receiverID);
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}