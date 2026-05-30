package com.example.productservice.controller.customer;

import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.productservice.entity.DishDoc;
import com.example.productservice.service.DishSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer/dish/search")
public class CustomerDishSearchController {

    @Autowired
    private DishSearchService dishSearchService;

    @LoginRequired("CUSTOMER")
    @GetMapping
    public Result<List<DishDoc>> search(@RequestParam String keyword){
        return Result.success(dishSearchService.search(keyword));
    }
}
