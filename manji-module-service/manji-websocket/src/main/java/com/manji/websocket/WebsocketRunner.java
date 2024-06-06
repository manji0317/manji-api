package com.manji.websocket;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.manji.base.basic.entity.BaseEntity;
import com.manji.base.dto.UserDTO;
import com.manji.base.entity.SysUser;
import com.manji.base.mapper.SysUserMapper;
import com.manji.websocket.config.WebsocketConfig;
import com.manji.websocket.utils.WebsocketUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
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
    private SysUserMapper sysUserMapper;

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
            String userId = getUserId(handshakeData);
            if (userId != null) {
                Long userCount = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(BaseEntity::getId, userId));
                if (userCount > 0) {
                    socketIOClient.set(WebsocketConfig.USER_ID, userId);
                    WebsocketUtil.addUser(userId, socketIOClient);
                    log.info("有新链接加入：userId:{} | clinic id: {}", userId, socketIOClient.getSessionId());
                } else {
                    log.info("请求链接中用户名不存在：userId:{}", userId);
                }
            } else {
                log.error("链接中没有username");
                socketIOClient.disconnect();
            }
        });
        socketIOServer.addDisconnectListener((socketIOClient) -> {
            String username = socketIOClient.get(WebsocketConfig.USER_ID);
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

    /**
     * 获取web socket中的username参数
     */
    private String getUserId(HandshakeData handshakeData) {
        Map<String, List<String>> urlParams = handshakeData.getUrlParams();
        List<String> userIds = urlParams.get(WebsocketConfig.USER_ID);
        if (userIds != null && !userIds.isEmpty()) {
            return userIds.get(0);
        }
        return null;
    }
}
