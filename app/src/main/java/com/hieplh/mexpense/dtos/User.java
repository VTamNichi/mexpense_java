package com.hieplh.mexpense.dtos;

public class User {
    private String user_name;
    private String user_email;
    private String user_phone;
    private int country_code;

    public User() {
    }

    public User(String user_name, String user_email, String user_phone, int country_code) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_phone = user_phone;
        this.country_code = country_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }
}
