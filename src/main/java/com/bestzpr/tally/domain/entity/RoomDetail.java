package com.bestzpr.tally.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className RoomDetail
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/12 23:07
 * @Version 1.0
 */
@Data
public class RoomDetail{
//    private boolean showKeyboard; // 是否显示键盘
//    private boolean showDetails = false; // 是否显示详细信息
//    private String money; // 金额
    private boolean showShare = false; // 是否显示分享
    private Option[] options; // 选项数组
    private boolean showCode = false; // 是否显示二维码
    private String roomCode; // 房间二维码
    private String roomId; // 房间ID
    private String roomNo; // 房间号
    private MyPointInfoVo myPointInfoVo; // 我的积分信息
    private List<OtherPointInfoVo> otherPointInfoVoArr; // 用户的积分信息数组
//    private String payeeUserId; // 收款人用户ID
//    private String payeeUserName; // 收款人用户名
//    private List<TransferDetail> tansferDetails; // 转账详情数组
    private String ws; // WebSocket连接
//    private boolean codeClose = true; // 二维码关闭状态
    private boolean showGuide = false; // 是否显示引导
    private int websocketRetryCount; // WebSocket重试计数

    @Data
    public static class Option {
        private String name; // 选项名称
        private String icon; // 选项图标
        private String openType; // 打开类型

        // Add getters and setters here
    }

    @Data
    public static class MyPointInfoVo {
        private Long userId;
        private BigDecimal userPoint; // 用户积分
        private String userAvatar; // 用户头像
        private String userName; // 用户名

        // Add getters and setters here
    }

    @Data
    public static class OtherPointInfoVo {
        private Long userId;
        private BigDecimal userPoint; // 用户积分
        private String userAvatar; // 用户头像
        private String userName; // 用户名

        // Add getters and setters here
    }

    @Data
    public static class TransferDetail {
        // Define the properties for transfer details

        // Add getters and setters here
    }
}
