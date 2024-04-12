package com.bestzpr.tally.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @className RoomUserCase
 * @Desc 房间用户的输赢情况
 * @Author 张埔枘
 * @Date 2023/12/21 22:49
 * @Version 1.0
 */
@Data
public class RoomUserCase {

    private String roomName;
    private String createTime;
    private BigDecimal userPoint;
}
