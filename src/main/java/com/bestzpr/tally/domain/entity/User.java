package com.bestzpr.tally.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @className User
 * @Desc 用户
 * @Author 张埔枘
 * @Date 2023/12/8 00:13
 * @Version 1.0
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_user")
public class User{

    /**
     * 用户 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String openId;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像 URL
     */
    private String avatarUrl;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}