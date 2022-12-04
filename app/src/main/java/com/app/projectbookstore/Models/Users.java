package com.app.projectbookstore.Models;

public class Users {
    private String userID, userFullName, userPassword, userEmail, userImage;
    private Integer userPoints;

    public Users() {
    }

    public Users(String userID, String userFullName, String userPassword, String userEmail, String userImage, Integer userPoints) {
        this.userID = userID;
        this.userFullName = userFullName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userPoints = userPoints;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(Integer userPoints) {
        this.userPoints = userPoints;
    }
}


