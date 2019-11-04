package com.example.toursapp2.Model;

public class PartnerItemModel {
    String name;
    String id;
    String icon;
    double rating;
    long ratingcount;
    String address;
    long offerpercent;

    public PartnerItemModel(String name, String icon, double rating, long ratingcount, String address, long offerpercent,String id) {
        this.name = name;
        this.icon = icon;
        this.rating = rating;
        this.ratingcount = ratingcount;
        this.address = address;
        this.id=id;
        this.offerpercent = offerpercent;
    }


    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public double getRating() {
        return rating;
    }

    public long getRatingcount() {
        return ratingcount;
    }

    public String getAddress() {
        return address;
    }

    public long getOfferpercent() {
        return offerpercent;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setRatingcount(int ratingcount) {
        this.ratingcount = ratingcount;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOfferpercent(int offerpercent) {
        this.offerpercent = offerpercent;
    }
}
