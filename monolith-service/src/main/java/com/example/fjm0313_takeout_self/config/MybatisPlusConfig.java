package com.example.fjm0313_takeout_self.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * @Configuration和@Bean注解搭配在一起用：
 * spring启动的时候会扫描这个配置类，然后把@Bean标记方法的返回对象注入进容器中
 * 之后mybatis-plus调用sql语句的时候，会去容器中找拦截器，然后使用这个PaginationInnerInterceptor自动为sql语句补上limit（每一条都会，但是只有使用Page对象的语句会这样）
 *
 * */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

}
