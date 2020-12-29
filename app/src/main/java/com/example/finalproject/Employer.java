package com.example.finalproject;

public class Employer {
    private String name,image,address,welfare;

    public Employer(String name, String image, String address, String welfare) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.welfare = welfare;
    }

    public Employer() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
