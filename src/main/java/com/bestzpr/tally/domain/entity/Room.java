package com.bestzpr.tally.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @className Room
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/8 00:13
 * @Version 1.0
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "t_room")
public class Room{

    /**
     * 房间 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房主 ID
     */
    private Long ownerId;

    /**
     * 房间状态 0:进行中 1:结束
     */
    private int roomStatus;

    private String roomCodeUrl;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}