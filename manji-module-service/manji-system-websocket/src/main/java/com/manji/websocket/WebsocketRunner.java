package com.manji.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.manji.base.service.UserDetailServiceImpl;
import com.manji.websocket.config.WebsocketConfig;
import com.manji.websocket.utils.WebsocketUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Websocket 服务 启动类
 *
 * @author Bqd
 * @since 2024/5/28 2:59
 */
@Component
@Slf4j
public class WebsocketRunner implements InitializingBean, DisposableBean {

    private SocketIOServer socketIOServer;

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Override
    public void afterPropertiesSet() {
        Configuration configuration = new Configuration();
        configuration.setContext(WebsocketConfig.CONTEXT);
        configuration.setPort(WebsocketConfig.PROP);
        configuration.setWorkerThreads(WebsocketConfig.WORKER_THREADS);
        configuration.setBossThreads(WebsocketConfig.BOSS_THREADS);
        socketIOServer = new SocketIOServer(configuration);


        socketIOServer.addConnectListener(socketIOClient -> {
            HandshakeData handshakeData = socketIOClient.getHandshakeData();
            String username = getUsername(handshakeData);
            if (username != null) {
                try {
                    userDetailService.loadUserByUsername(username);
                    socketIOClient.set(WebsocketConfig.USERNAME, username);
                    WebsocketUtil.addUser(username, socketIOClient);
                    log.info("有新链接加入：username:{} | clinic id: {}", username, socketIOClient.getSessionId());
                } catch (UsernameNotFoundException e) {
                    log.info("请求链接中用户名不存在：{}", username);
                    socketIOClient.disconnect();
                }
            } else {
                log.error("链接中没有username");
                socketIOClient.disconnect();
            }
        });

        socketIOServer.addDisconnectListener((socketIOClient) -> {
            String username = socketIOClient.get(WebsocketConfig.USERNAME);
            if (username != null && username.isBlank()) {
                WebsocketUtil.removeUser(username);
            }
            log.info("有链接断开：username:{} | clinic id: {}", username, socketIOClient.getSessionId());
        });


        socketIOServer.start();
    }

    @Override
    public void destroy() {
        if (socketIOServer != null) {
            socketIOServer.stop();
        }
    }


    private String getUsername(HandshakeData handshakeData) {
        Map<String, List<String>> urlParams = handshakeData.getUrlParams();

        List<String> usernames = urlParams.get(WebsocketConfig.USERNAME);

        if (usernames != null && !usernames.isEmpty()) {
            return usernames.get(0);
        }

        return null;
    }
}
