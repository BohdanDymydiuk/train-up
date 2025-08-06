package com.example.trainup.model;

import java.util.List;

public interface Rateable {
    Float getOverallRating();

    void setOverallRating(Float overallRating);

    Integer getNumberOfReviews();

    void setNumberOfReviews(Integer numberOfReviews);

    List<Review> getReviews();

    void setReviews(List<Review> reviews);
}
