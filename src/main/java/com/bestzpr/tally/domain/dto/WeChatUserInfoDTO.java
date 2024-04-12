package com.bestzpr.tally.domain.dto;

import lombok.Data;

/**
 * @className WeChatUserInfo
 * @Desc    微信接口返回对象
 * @Author 张埔枘
 * @Date 2023/12/11 23:05
 * @Version 1.0
 */
@Data
public class WeChatUserInfoDTO {
    private String session_key;
    private String openid;
    private String unionid;
    private String errcode;
    private String errmsg;

}
