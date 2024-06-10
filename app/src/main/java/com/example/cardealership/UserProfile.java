package com.example.cardealership;

public class UserProfile {

    private String name;
    private String group;
    private String year;
    private String email;
    private String avatar;
    private String role;

    public UserProfile(){

    }

    public UserProfile(String name, String group, String year, String email, String avatar) {
        this.name = name;
        this.group = group;
        this.year = year;
        this.email = email;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
