package com.myproject.skillswap;

public class Report {
    private String postId;
    private String title;
    private String description;
    private String reporterId;
    private String reportReason;

    public Report() {
    }

    public Report(String postId, String title, String description, String reporterId, String reportReason) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.reporterId = reporterId;
        this.reportReason = reportReason;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
}
