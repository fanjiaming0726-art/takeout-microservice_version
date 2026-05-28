package com.example.fjm0313_takeout_self.service;

import com.example.fjm0313_takeout_self.dto.ReviewSubmitDto;
import com.example.fjm0313_takeout_self.mongo.Review;

import java.util.List;

public interface ReviewService {

    Review submitReview(Long userId, ReviewSubmitDto dto);

    List<Review> listMyReviews(Long userId);

    List<Review> listByDishId(Long dishId);

}
