package com.bestzpr.tally.service;

import java.math.BigDecimal;

/**
 * @className TransferDetailService
 * @Desc 转账详情
 * @Author 张埔枘
 * @Date 2023/12/13 14:08
 * @Version 1.0
 */
public interface TransferDetailService {
    /**
     * 通过roomId 和 userId 查询转账信息
     * @param roomId
     * @param userId
     * @return
     */
    BigDecimal getTransferDetailsByRoomAndUser(Long roomId, Long userId);
}
