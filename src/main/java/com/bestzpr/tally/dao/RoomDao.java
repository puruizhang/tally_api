package com.bestzpr.tally.dao;

import com.bestzpr.tally.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @className RoomDao
 * @Desc 房间
 * @Author 张埔枘
 * @Date 2023/12/8 00:14
 * @Version 1.0
 */
@Repository
public interface RoomDao extends JpaRepository<Room, Long> {

    /**
     * 保存房间信息
     *
     * @param room 房间信息
     * @return
     */
    @Override
    Room save(Room room);

}