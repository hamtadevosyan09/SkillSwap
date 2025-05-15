package com.myproject.skillswap;

public class Post {
    private String postId;
    private String title;
    private String description;
    private String userId;
    private String creatorUserId;
    private String category;
    private String username;

    public Post() {
    }

    public Post(String title, String description, String userId, String creatorUserId, String category, String username) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.creatorUserId = creatorUserId;
        this.category = category;
        this.username = username;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}