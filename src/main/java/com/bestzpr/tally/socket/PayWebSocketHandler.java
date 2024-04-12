package com.bestzpr.tally.socket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * @className PayWebSocketHandler
 * @Desc 支付
 * @Author 张埔枘
 * @Date 2023/12/8 23:22
 * @Version 1.0
 */
@Slf4j
public class PayWebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomNum = session.getUri().getPath().substring(session.getUri().getPath().lastIndexOf("/") + 1);
        log.info("socket 连接加入:{},房间号:{}",session.getId(),roomNum);
        WebSocketSessions.add(session);
        TextMessage textMessage = new TextMessage("socket 连接加入:"+session.getId()+"房间号:"+roomNum);
        notifyAllSessions(textMessage,roomNum,session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        WebSocketSessions.remove(session);
        log.info("socket 连接关闭:{}",session.getId());
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 接收到文本消息时触发
        String receivedMessage = message.getPayload();
        log.info("收到socket消息:{}",receivedMessage);

        String responseMessage = "success";
        session.sendMessage(new TextMessage(responseMessage));

        String roomNum = session.getUri().getPath().substring(session.getUri().getPath().lastIndexOf("/") + 1);
        log.info("通知房间编号:{}",roomNum);
        // 通知当前房间的人，除了本人
        notifyAllSessions(message,roomNum,session.getId());
    }

    private void notifyAllSessions(TextMessage message,String roomNum,String currentSessionId) {
        log.info("开始socket通知，房间:{}",roomNum);
        for (WebSocketSession session : WebSocketSessions.getSessions()) {
            try {
                // 不给自己通知
                if(session.getId().equals(currentSessionId)){
                    continue;
                }
                String roomNumTmp = session.getUri().getPath().substring(session.getUri().getPath().lastIndexOf("/") + 1);
                log.info("socket 通知房间:{},sessionId:{}",roomNumTmp,session.getId());
                if(StringUtils.equals(roomNumTmp,roomNum)){
                    session.sendMessage(message);
                }

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

}
