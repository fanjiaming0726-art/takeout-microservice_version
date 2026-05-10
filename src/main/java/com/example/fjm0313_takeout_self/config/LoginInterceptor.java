package com.example.fjm0313_takeout_self.config;


import com.example.commonservice.annotation.LoginRequired;
import com.example.commonservice.result.Result;
import com.example.commonservice.context.UserContext;
import com.example.fjm0313_takeout_self.service.BlackListService;
import com.example.fjm0313_takeout_self.service.RedisLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.example.fjm0313_takeout_self.common.RedisConstant.MAX_REQUEST;
import static com.example.fjm0313_takeout_self.common.RedisConstant.WINDOW_SECONDS;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private  BlackListService blackListService;

    @Autowired
    private RedisLimitService redisLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod handlerMethod)){
            return true;
        }
        LoginRequired annotation = handlerMethod.getMethodAnnotation(LoginRequired.class);

        if(annotation == null){
            return true;
        }
        String type = annotation.value();

        if(type.equals("CUSTOMER")){
            Long userId = (Long) request.getSession().getAttribute("userId");
            if(userId == null){
                writeError(response,"用户未登录",401);
                return false;
            }

            if(blackListService.isBlacklisted(userId)){
                writeError(response,"您的账号已被限制使用",403);
                return false;
            }
            if(!redisLimitService.isAllowed(userId,MAX_REQUEST,WINDOW_SECONDS)){
                writeError(response,"请求过于频繁，请稍后再试",429);
                return false;
            }

            UserContext.setUserId(userId);
        }else{
            Long employeeId = (Long) request.getSession().getAttribute("employeeId");
            if(employeeId == null){
                writeError(response,"商家未登录",401);
                return false;
            }
            UserContext.setEmployeeId(employeeId);
        }
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,Exception ex) throws Exception {
        UserContext.clear();

    }

    private void writeError(HttpServletResponse response, String msg,int status) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(msg)));
    }
}
