package com.bestzpr.tally.domain.req;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @className TransferDetail
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/8 00:12
 * @Version 1.0
 */
@Data
public class TransferDetailReq {

    /**
     * 转账明细 ID
     */
    private Long id;

    /**
     * 房间 ID
     */
    private Long roomId;

    /**
     * 付款人用户 ID
     */
    private Long payerUserId;

    /**
     * 收款人用户 ID
     */
    private Long payeeUserId;

    /**
     * 转账金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private String createTime;

}