package vukan.com.fftd.models;

public class Comment {
    private String fromUserID;
    private String toUserID;
    private Float grade;
    private String comment;

    public Comment() {
    }

    public Comment(String fromUserID, String toUserID, String comment, Float grade) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.grade = grade;
        this.comment = comment;
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(String fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getToUserID() {
        return toUserID;
    }

    public void setToUserID(String toUserID) {
        this.toUserID = toUserID;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}