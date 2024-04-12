package com.bestzpr.tally.service.impl;

import com.bestzpr.tally.dao.TransferDetailDao;
import com.bestzpr.tally.service.TransferDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @className TransferDetailServiceImpl
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/13 14:08
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class TransferDetailServiceImpl implements TransferDetailService {
    private final TransferDetailDao transferDetailDao;

    @Override
    public BigDecimal getTransferDetailsByRoomAndUser(Long roomId, Long userId) {
        return transferDetailDao.getBalanceByRoomIdAndUserId(roomId,userId);
    }

}
