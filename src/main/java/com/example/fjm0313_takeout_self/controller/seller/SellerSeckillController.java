package com.example.fjm0313_takeout_self.controller.seller;

import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.fjm0313_takeout_self.entity.SeckillActivity;
import com.example.fjm0313_takeout_self.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller/seckill")
public class SellerSeckillController {

    @Autowired
    private SeckillService seckillService;


    @LoginRequired("EMPLOYEE")
    @GetMapping("/list")
    public Result<List<SeckillActivity>> list() {
        return Result.success(seckillService.listActivities());
    }

    @LoginRequired("EMPLOYEE")
    @PostMapping("/create")
    public Result<String> create(@RequestBody SeckillActivity activity){
        try {
            seckillService.createActivity(activity);
            return Result.success("该菜品已进入秒杀活动");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }





}
