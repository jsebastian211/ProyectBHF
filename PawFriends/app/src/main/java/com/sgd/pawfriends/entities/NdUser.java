package com.sgd.pawfriends.entities;


import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by Daza on 14/05/2017.
 */

public class NdUser {


    public static final int TOKEN = 1;
    public static final int EMAIL = 2;
    public static final int LOG_PROVIDER = 3;
    public static final int EMAIL_SENDED = 4;
    public static final int REGISTER_DATE = 5;
    public static final int NAME = 6;
    public static final int ID = 7;
    public static final int IS_OWNER = 8;
    public static final int IS_USER = 9;
    public static final int IS_ASSISTANT = 10;
    public static final int IS_PAW_FRIENDLY = 11;
    public static final int LAST_LOGIN = 12;

    private String token;
    private String email;
    private String logProvider;
    private Boolean emailSended;
    private Long registerDate;
    private String name;
    private String id;
    private Integer score;
    private Boolean isOwner;
    private Boolean isUser;
    private Boolean isAssistant;
    private Boolean isPawFriendly;
    private Long lastLogin;
    private ArrayList<NdPets> pets;

    @Exclude
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogProvider() {
        return logProvider;
    }

    public void setLogProvider(String logProvider) {
        this.logProvider = logProvider;
    }

    public Boolean getEmailSended() {
        return emailSended;
    }

    public void setEmailSended(Boolean emailSended) {
        this.emailSended = emailSended;
    }

    public Long getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Long registerDate) {
        this.registerDate = registerDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public Boolean getDogWalker() {
        return isUser;
    }

    public void setDogWalker(Boolean dogWalker) {
        isUser = dogWalker;
    }

    public Boolean getAssistant() {
        return isAssistant;
    }

    public void setAssistant(Boolean assistant) {
        isAssistant = assistant;
    }

    public Boolean getPawFriendly() {
        return isPawFriendly;
    }

    public void setPawFriendly(Boolean pawFriendly) {
        isPawFriendly = pawFriendly;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public ArrayList<NdPets> getPets() {
        return pets;
    }

    public void setPets(ArrayList<NdPets> pets) {
        this.pets = pets;
    }

    public static String getName(int field) {
        switch (field) {
            case TOKEN:
                return "token";
            case EMAIL:
                return "email";
            case LOG_PROVIDER:
                return "logProvider";
            case EMAIL_SENDED:
                return "emailSended";
            case REGISTER_DATE:
                return "registerDate";
            case NAME:
                return "name";
            case ID:
                return "id";
            case IS_OWNER:
                return "registerDate";
            case IS_USER:
                return "isUser";
            case IS_ASSISTANT:
                return "isAssistant";
            case IS_PAW_FRIENDLY:
                return "isPawFriendly";
            case LAST_LOGIN:
                return "lastLogin";
            default:
                return null;
        }
    }

    public NdUser(String token, String email, String logProvider, Boolean emailSended, Long registerDate, String name, boolean isUser) {
        this.token = token;
        this.email = email;
        this.logProvider = logProvider;
        this.emailSended = emailSended;
        this.registerDate = registerDate;
        this.name = name;
        this.isUser = isUser;
    }

    public NdUser() {
    }
}
