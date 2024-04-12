package com.bestzpr.tally.socket;

import lombok.Data;

/**
 * @className PaymentMessage
 * @Desc 支付消息
 * @Author 张埔枘
 * @Date 2023/12/8 00:54
 * @Version 1.0
 */
@Data
public class PaymentMessage {
    private String type;
    private String data;
}
