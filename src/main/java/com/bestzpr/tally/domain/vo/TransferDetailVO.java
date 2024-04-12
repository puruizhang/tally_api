package com.bestzpr.tally.domain.vo;

import com.bestzpr.tally.domain.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @className TransferDetailVO
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/17 20:25
 * @Version 1.0
 */
@Data
@Entity
@Table(name = "t_transfer_detail")
public class TransferDetailVO {
    /**
     * 转账明细 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payee_user_id", referencedColumnName = "id")
    private User payeeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_user_d", referencedColumnName = "id")
    private User payerUser;

    private BigDecimal amount;

    private Date createTime;

}
