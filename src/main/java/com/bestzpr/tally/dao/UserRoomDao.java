package com.bestzpr.tally.dao;

import com.bestzpr.tally.domain.entity.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @className UserRoomDao
 * @Desc 用户和房间的绑定关系
 * @Author 张埔枘
 * @Date 2023/12/8 00:14
 * @Version 1.0
 */
@Repository
public interface UserRoomDao extends JpaRepository<UserRoom, Long> {

    UserRoom findByUserIdAndStatus(Long id, int status);

    List<UserRoom> findByRoomId(Long roomId);

    UserRoom findByUserIdAndRoomId(Long userId, Long roomId);

    List<UserRoom> findByUserId(Long userId);
    List<UserRoom> findListByUserIdAndStatus(Long id, int status);

    List<UserRoom> findByRoomIdAndStatus(Long roomId, int status);
}