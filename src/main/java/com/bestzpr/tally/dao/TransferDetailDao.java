package com.bestzpr.tally.dao;

import com.bestzpr.tally.domain.entity.TransferDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className TransferDetailDao
 * @Desc 转账信息
 * @Author 张埔枘
 * @Date 2023/12/8 00:14
 * @Version 1.0
 */
@Repository
public interface TransferDetailDao extends JpaRepository<TransferDetail, Long> {

    @Query("SELECT SUM(CASE WHEN td.payerUser.id = :userId THEN -td.amount ELSE td.amount END) " +
            "FROM TransferDetail td " +
            "WHERE td.roomId = :roomId AND (td.payerUser.id = :userId OR td.payeeUser.id = :userId)")
    BigDecimal getBalanceByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    List<TransferDetail> findByRoomIdOrderByCreateTimeDesc(@Param("roomId")Long roomId);

}