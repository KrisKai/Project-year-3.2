package com.example.myprojectyear32.data.user;

public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String passWord;
    private String email;
    private String phoneNumber;
    private String DoB;
    private String gender;
    private String connectCode;

    public User(){}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.phoneNumber = PhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String Email) {
        this.email = Email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String Password) {
        this.passWord = Password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String Username) {
        this.userName = Username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String LastName) {
        this.lastName = LastName;
    }

    public String getConnect() {
        return connectCode;
    }

    public void setConnect(String Connect) {
        this.connectCode = Connect;
    }
}