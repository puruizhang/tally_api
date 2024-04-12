package com.bestzpr.tally.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @className UserRoom
 * @Desc 用户和房间的绑定关系
 * @Author 张埔枘
 * @Date 2023/12/12 21:25
 * @Version 1.0
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_user_room")
public class UserRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    // 0:进行中 1:结束
    private int status;
    // 0:负 1:胜利 2:平局
    private Integer victoryFlag;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
