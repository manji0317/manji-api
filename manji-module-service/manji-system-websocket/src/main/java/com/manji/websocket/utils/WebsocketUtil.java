package com.manji.websocket.utils;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket 工具类
 *
 * @author Bqd
 * @since 2024/5/28 4:11
 */
public class WebsocketUtil {
    /**
     * 用户名和websocket clientId 对应关系
     * */
    public static final Map<String, SocketIOClient> USER_CLIENT_ID_MAP = new ConcurrentHashMap<>();

    /**
     * 类型 普通消息通知
     */
    public static final String NOTIFICATIONS = "Notifications";

    /**
     * 类型 菜单的通知
     */
    public static final String MenuNotifications = "MenuNotifications";

    public static void addUser(String username, SocketIOClient client) {
        USER_CLIENT_ID_MAP.put(username, client);
    }

    public static void removeUser(String username) {
        USER_CLIENT_ID_MAP.remove(username);
    }
}
