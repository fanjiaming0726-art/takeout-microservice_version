package com.example.fjm0313_takeout_self.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fjm0313_takeout_self.dto.ReviewSubmitDto;
import com.example.fjm0313_takeout_self.entity.Dish;
import com.example.fjm0313_takeout_self.entity.OrderDetail;
import com.example.fjm0313_takeout_self.entity.Orders;
import com.example.fjm0313_takeout_self.entity.User;
import com.example.fjm0313_takeout_self.mapper.DishMapper;
import com.example.fjm0313_takeout_self.mapper.OrderDetailMapper;
import com.example.fjm0313_takeout_self.mapper.OrdersMapper;
import com.example.fjm0313_takeout_self.mapper.UserMapper;
import com.example.fjm0313_takeout_self.mongo.Review;
import com.example.fjm0313_takeout_self.mongo.repository.ReviewRepository;
import com.example.fjm0313_takeout_self.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.BatchUpdateException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public Review submitReview(Long userId, ReviewSubmitDto dto) {

        // 检查前端传过来的消息内容
        if(dto.getOrderId() == null){
            throw new RuntimeException("订单Id不能为空");
        }
        if(dto.getDishId() == null){
            throw new RuntimeException("菜品Id不能为空");
        }

        if(dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5){
            throw new RuntimeException("评分必须在1-5分之间");
        }

        if(!StringUtils.hasText(dto.getContent())){
            throw new RuntimeException("评价内容不能为空哦");
        }

        // 检查订单是否真实存在
        Orders order = ordersMapper.selectById(dto.getOrderId());
        if(order == null){
            throw new RuntimeException("订单不存在");
        }
        if(!order.getUserId().equals(userId)){
            throw new RuntimeException("不能评价别人的订单");
        }
        if(order.getStatus() == null || order.getStatus() == 0){
            throw new RuntimeException("未支付订单不能评价");
        }

        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId,dto.getOrderId());
        wrapper.eq(OrderDetail::getDishId,dto.getDishId());
        OrderDetail orderDetail = orderDetailMapper.selectOne(wrapper);

        if(orderDetail == null) {
            throw new RuntimeException("该订单中不存在这个菜品");
        }

        boolean exists = reviewRepository.existsByOrderIdAndDishIdAndUserId(dto.getOrderId(),dto.getDishId(),userId);

        if(exists){
            throw new RuntimeException("该订单已评价过了哦");
        }

        User user = userMapper.selectById(userId);

        Review review = new Review();
        review.setOrderId(dto.getOrderId());
        review.setUserId(userId);
        review.setUsername(user == null  ? null : user.getUsername());
        review.setDishId(dto.getDishId());
        review.setDishName(orderDetail.getName());
        review.setDishImage(orderDetail.getImage());
        review.setDishAmount(orderDetail.getAmount());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        review.setImageUrls(dto.getImageUrls());

        // 默认不匿名
        review.setAnonymous(dto.getAnonymous() != null && dto.getAnonymous());
        review.setCreateTime(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> listByDishId(Long dishId) {
        if(dishId == null){
            throw new RuntimeException("菜品Id不能为空");
        }
        return reviewRepository.findByDishIdOrderByCreateTimeDesc(dishId);
    }

    @Override
    public List<Review> listMyReviews(Long userId) {
        return reviewRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }



}
