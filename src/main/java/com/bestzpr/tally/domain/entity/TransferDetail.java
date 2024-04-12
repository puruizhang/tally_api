package com.bestzpr.tally.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @className TransferDetail
 * @Desc 转账详情
 * @Author 张埔枘
 * @Date 2023/12/8 00:12
 * @Version 1.0
 */
@Entity
@Data
@Table(name = "t_transfer_detail")
public class TransferDetail {

    /**
     * 转账明细 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 房间 ID
     */
    private Long roomId;

    /**
     * 付款人用户 ID
     */
//    @Transient
//    private Long payerUserId;

    @OneToOne
    @JoinColumn(name = "payer_user_id")
    private User payerUser;
    /**
     * 收款人用户 ID
     */
//    @Transient
//    private Long payeeUserId;

    @OneToOne
    @JoinColumn(name = "payee_user_id")
    private User payeeUser;
    /**
     * 转账金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

}