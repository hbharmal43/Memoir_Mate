package vukan.com.fftd.models;

public class ReportUser {
    private String reporterUserID;
    private String reportedUserID;

    public ReportUser() {
    }

    public String getReporterUserID() {
        return reporterUserID;
    }

    public void setReporterUserID(String reporterUserID) {
        this.reporterUserID = reporterUserID;
    }

    public String getReportedUserID() {
        return reportedUserID;
    }

    public void setReportedUserID(String reportedUserID) {
        this.reportedUserID = reportedUserID;
    }
}