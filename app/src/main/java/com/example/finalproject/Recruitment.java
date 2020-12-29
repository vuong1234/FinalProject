package com.example.finalproject;

public class Recruitment {
    private String title,address,salary,date,reasonApply,image,job,key;

    public Recruitment() {
    }

    public Recruitment(String title, String address, String salary, String date, String reasonApply,String image,String job,String key) {
        this.title = title;
        this.address = address;
        this.salary = salary;
        this.date = date;
        this.reasonApply = reasonApply;
        this.image = image;
        this.job = job;
        this.key = key;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReasonApply() {
        return reasonApply;
    }

    public void setReasonApply(String reasonApply) {
        this.reasonApply = reasonApply;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
