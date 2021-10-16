package com.example.chatpoint.Models;

public class Users {

    String profilePicture, username, password,userId, lastMessage,email, status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Users(String profilePicture, String username, String email, String password, String userId, String lastMessage) {
        this.profilePicture = profilePicture;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.email = email;
    }

    public Users(){}

    //Signup constructors
    public Users( String username, String email, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId(String key) {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
