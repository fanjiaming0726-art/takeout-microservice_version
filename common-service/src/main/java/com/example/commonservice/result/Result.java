package com.example.commonservice.result;


import lombok.Data;

import java.io.Serializable;

/***
 * 为什么不用静态方法返回result结果，每次都new一个回去不就行了吗
 * 答案：每次都new的话，每次都需要往里传参数，方法一多就容易写错，而且也不便阅读，使用success/fail一眼就知道是响应失败还是成功了
 *
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private int code;
    private String msg;
    private T object;

    public static <T> Result<T> success(T object){

        Result<T> result = new Result<>();

        // 之所以不返回200，是因为200/404/500这类是发生在网络层面的，而0/1属于是业务上的成功与否的代码
        result.code = 1;

        result.object = object;

        return result;

    }

    public static <T> Result<T> fail(String msg){

        Result<T> result = new Result<>();

        // 之所以不返回200，是因为200/404/500这类是发生在网络层面的，而0/1属于是业务上的成功与否的代码
        result.code = 0;

        result.msg = msg;

        return result;

    }

}
