package com.example.fjm0313_takeout_self.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    /***
     * 这个是版本号，用来让Java序列化机制成功匹配字节流与类的，适用于后期可能需要改字段的情况
     * 每次序列化的时候，如果写了id就把id打包进字节流，如果没写就会根据类的结构自己生成一个id，每次Java反序列化前都会从字节流中拿版本号并与类的版本号对比
     * 类中不写这个id：如果后期字段被更改了，那么序列化时候的版本号和反序列化的版本号对不上，就会报错。
     * 这个变量名不能更改，Java序列化内部硬编码写死的
     */
    private static final long serialVersionUID = 1L;

    /***
     *数据库设置了AUTO_INCREMENT，但是mybatis-plus不知道，它会自己生成id覆盖掉数据的id。所以需要这个注解告诉mybatis不要管这个id了
     *      */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String phone;


    /***
     * 为什么设置成Integer而不是int，因为有的字段可以为空，当从数据库映射到Java对象的时候，int对象是无法接收null的，所以会报错
     */
    private Integer sex;

    private Integer status;

    /***
     * mybatis在执行sql语句的时候会扫描这个实体类，只有被@TableField标注的字段才会被认为是需要填充的字段，否则MyMedaObjectHandler不起作用
     */

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;






}
