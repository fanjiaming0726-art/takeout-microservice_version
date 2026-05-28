package com.example.fjm0313_takeout_self.controller.seller;

import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.fjm0313_takeout_self.service.DishSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/dish/rebuild")
public class SellerDishSearchController {

    @Autowired
    private DishSearchService dishSearchService;

    @LoginRequired("EMPLOYEE")
    @PostMapping("/rebuild")
    public Result<String> rebuild(){
        dishSearchService.rebuildDishIndex();
        return Result.success("菜品搜索索引重建成功");
    }

}
