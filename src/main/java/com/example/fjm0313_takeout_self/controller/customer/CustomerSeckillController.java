package com.example.fjm0313_takeout_self.controller.customer;

import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.commonservice.context.UserContext;
import com.example.fjm0313_takeout_self.common.MQ.message.SeckillMessage;
import com.example.fjm0313_takeout_self.common.MQ.sender.SeckillOrderSender;
import com.example.fjm0313_takeout_self.entity.AddressBook;
import com.example.fjm0313_takeout_self.entity.SeckillActivity;
import com.example.fjm0313_takeout_self.service.AddressBookService;
import com.example.fjm0313_takeout_self.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer/seckill")
public class CustomerSeckillController {

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private SeckillOrderSender seckillOrderSender;

    @Autowired
    private SeckillService  seckillService;

    @LoginRequired("CUSTOMER")
    @PostMapping("/list")
    public Result<List<SeckillActivity>> list(){
        return Result.success(seckillService.listActivities());
    }

    @LoginRequired("CUSTOMER")
    @PostMapping("/rush/{activityId}")
    public Result<String> rush(@PathVariable Long activityId){
        Long userId = UserContext.getUserId();

        AddressBook addressBook = addressBookService.findDefaultByUserId(userId);
        if(addressBook == null){
            return Result.fail("还没有填写地址哦");
        }

        int result = seckillService.trySeckill(activityId,userId);

        if(result == 0){
            SeckillMessage message = new SeckillMessage();
            message.setActivityId(activityId);
            message.setUserId(UserContext.getUserId());
            seckillOrderSender.sendSeckillOrder(message);
            return Result.success("秒杀成功，订单生成中,请稍后查看");
        }


        return switch (result){
            // case 0 ->  Result.success("秒杀成功，请尽快下单");
            case -1 ->   Result.success("该秒杀商品已售完");
            case -2 -> Result.success("您已秒杀过同一商品，不可再次秒杀");
            default -> Result.success("系统繁忙，请稍后再试");
        };
    }
}
