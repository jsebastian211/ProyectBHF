package com.sgd.pawfriends.entities;

/**
 * Created by Daza on 02/09/2017.
 */

public class NdPets {

    private String token;
    private String name;
    private String owner;
    private String species;
    private String breed;
    private String gender;
    private Long birthDay;
    private Integer age;
    private String urlImage;
    private String chipNumber;
    private String note;
    private String localUriImage;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Long birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocalUriImage() {
        return localUriImage;
    }

    public void setLocalUriImage(String localUriImage) {
        this.localUriImage = localUriImage;
    }

    public NdPets(String token, String name, String owner, String species, String breed,
                  String gender, Long birthDay, Integer age, String urlImage, String chipNumber,
                  String note, String localUriImage) {
        this.token = token;
        this.name = name;
        this.owner = owner;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.birthDay = birthDay;
        this.age = age;
        this.urlImage = urlImage;
        this.chipNumber = chipNumber;
        this.note = note;
        this.localUriImage = localUriImage;
    }

    public NdPets() {
    }
}
