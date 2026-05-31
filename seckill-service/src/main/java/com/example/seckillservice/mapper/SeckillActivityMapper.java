package com.example.seckillservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.seckillservice.entity.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    @Update("UPDATE seckill_activity SET total_stock = total_stock - 1 " +
            "WHERE id = #{activityId} AND total_stock > 0 ")
    int deductStock(@Param("activityId") Long activityId);

    @Update(("UPDATE seckill_activity SET total_stock = total_stock + 1 " +
            "WHERE id = #{activityId} AND total_stock > 0"))
    int restoreStock(@Param("activityId") Long activityId);
}
