package com.manji.websocket.handler;

import com.corundumstudio.socketio.annotation.OnConnect;
import org.springframework.stereotype.Component;

/**
 * @author Bqd
 * @since 2024/5/28 3:30
 */
@Component
public class WebsocketHandler {

    @OnConnect
    private void connect() {

    }

}
