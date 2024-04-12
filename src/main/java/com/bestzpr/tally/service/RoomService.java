package com.bestzpr.tally.service;

import com.bestzpr.tally.domain.entity.Room;
import com.bestzpr.tally.domain.entity.RoomDetail;
import com.bestzpr.tally.domain.vo.RoomUserCase;

import java.util.List;

/**
 * @className RoomService
 * @Desc 房间
 * @Author 张埔枘
 * @Date 2023/12/12 21:21
 * @Version 1.0
 */
public interface RoomService {
    /**
     * 构建房间
     * @return
     */
    Room buildRoom();

    /**
     * 根据id查询房间
     * @param roomId
     * @return
     */
    Room findById(Long roomId);

    /**
     * 加入房间
     * @param roomId
     * @return
     */
    Boolean joinRoom(Long roomId);

    /**
     * 获取房间详细信息
     * @param roomId
     * @return
     */
    RoomDetail roomCommonInfo(Long roomId);

    /**
     * 获取房间内其他人员的积分情况
     * @param roomId
     * @return
     */
    List<RoomDetail.OtherPointInfoVo> getUserRoomByRoomId(Long roomId);

    /**
     * 查询当前用户所有房间的战绩情况
     * @return
     */
    List<RoomUserCase> userHistoryInfoDetails();

    /**
     * 获取房间-当前用户的
     * @return
     */
    Room getCurrentRoomInfoByUser();
}
