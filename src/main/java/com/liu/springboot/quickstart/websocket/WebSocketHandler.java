package com.liu.springboot.quickstart.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@ServerEndpoint(value = "/websocket")
public class WebSocketHandler {

    private static Logger logger = Logger.getLogger(WebSocketHandler.class);
    
    private Map<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage("连接成功..."));
            logger.info(">>>有客户加入<<<");
        } catch (IOException e) {
            logger.error("IO异常>>>", e);
        }
    }
    
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        System.out.println("来自客户端的消息:" + message);
        Thread a = new TestSendMsgTask(session, message);
        threadMap.put(session.getId(), a);
    }
    
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(WebSocketSession session) {
        logger.info("客户端退出>>>"+session.getId());
        try {
            TestSendMsgTask c = (TestSendMsgTask) threadMap.get(session.getId());
            c.close();
            c.interrupt();
            c.join();
            threadMap.remove(session.getId());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}
