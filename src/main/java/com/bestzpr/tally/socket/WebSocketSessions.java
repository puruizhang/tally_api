package com.bestzpr.tally.socket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @className WebSocketSessions
 * @Desc  全局session 维护类
 * @Author 张埔枘
 * @Date 2023/12/20 21:16
 * @Version 1.0
 */
public class WebSocketSessions {
    private static final Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void add(WebSocketSession session) {
        sessions.add(session);
    }

    public static void remove(WebSocketSession session) {
        sessions.remove(session);
    }

    public static Set<WebSocketSession> getSessions() {
        return sessions;
    }
}
