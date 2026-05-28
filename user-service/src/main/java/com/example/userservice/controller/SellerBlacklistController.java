package com.example.userservice.controller;

import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.userservice.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/seller/blacklist")
public class SellerBlacklistController {

    @Autowired
    private BlackListService blackListService;

    @LoginRequired("EMPLOYEE")
    @PostMapping("/add")
    public Result<String> add(@RequestBody Map<String,Object> params){
        Long userId =  Long.valueOf(params.get("userId").toString());
        String reason = (String) params.get("reason");
        blackListService.addToBlackList(userId,reason);
        return Result.success("已将用户加入黑名单");
    }

    @LoginRequired("EMPLOYEE")
    @PostMapping("/remove")
    public Result<String> remove(@RequestBody Map<String, Object> params){
        Long userId = Long.valueOf(params.get("userId").toString());
        blackListService.removeFromBlacklist(userId);
        return Result.success("已将用户移出黑名单");

    }

    @LoginRequired("EMPLOYEE")
    @PostMapping("/list")
    public Result<Set<String>> list(){
        return Result.success(blackListService.getAllBlacklist());
    }

}
