package com.example.fjm0313_takeout_self.mongo;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
// 注意不要和collection写混，collation是排序规则的意思
@Document(collection = "review")
public class Review {


    @Id
    private String id;

    private Long orderId;

    private Long userId;

    private String username;

    private Long dishId;

    private String dishName;

    private String dishImage;

    private Integer rating;

    private String content;

    private List<String> imageUrls;

    private BigDecimal dishAmount;

    private Boolean anonymous;

    private LocalDateTime createTime;


}
