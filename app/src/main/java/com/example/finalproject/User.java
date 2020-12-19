package com.example.finalproject;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;


    public User(){

    }

    public User(int userId, String firstName,String lastName, String phoneNumber, String emailAddress){
        this.userId= userId;
        this.firstName= firstName;
        this.lastName=lastName;
        this.phoneNumber=phoneNumber;
        this.emailAddress= emailAddress;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

