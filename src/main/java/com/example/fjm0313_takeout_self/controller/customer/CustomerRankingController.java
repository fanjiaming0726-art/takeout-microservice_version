package com.example.fjm0313_takeout_self.controller.customer;


import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.fjm0313_takeout_self.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer/ranking")
public class CustomerRankingController {

    @Autowired
    private RankingService rankingService;

    @LoginRequired("CUSTOMER")
    @PostMapping("/hot")
    public Result<List<Map<String, Object>>> hot(){
        return Result.success(rankingService.getTopN(5));
    }
}
