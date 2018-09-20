package com.studentcares.spps.model;

public class SchoolReviewsListItems {

    public String school_rating;
    public String school_reviews;
    public String name;
    public String userId;

    public SchoolReviewsListItems() {
    }

    public SchoolReviewsListItems(String clinicRatings, String clinicReviews  ,String name, String userId) {

        this.school_reviews = clinicReviews;
        this.school_rating = clinicRatings;
        this.name = name;
        this.userId = userId;
    }

    public String getschool_rating() {
        return school_rating;
    }

    public void setschool_rating(String clinicRatings) {
        this.school_rating = clinicRatings;
    }

    public String getschool_reviews() {
        return school_reviews;
    }

    public void setschool_reviews(String clinicReviews) {
        this.school_reviews = clinicReviews;
    }

    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }

    public String getuserId() {
        return userId;
    }
    public void setuserId(String userId) {
        this.userId = userId;
    }
}
