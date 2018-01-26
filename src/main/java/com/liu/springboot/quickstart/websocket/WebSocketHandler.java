package com.liu.springboot.quickstart.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketHandler {

    private static Logger logger = Logger.getLogger(WebSocketHandler.class);
    
    private Map<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();
    
    //记录当前链接的总人数 
    private static AtomicInteger sum = new AtomicInteger(0);
    //保存所有的客户端信息
    private ConcurrentHashMap<String, Session> sockClient = new ConcurrentHashMap<String, Session>();
    /**
     * 连接建立成功调用的方法
     * */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
            sockClient.put(userId, session);
            session.getBasicRemote().sendText("连接成功...,当前账户为:"+userId);
            sum.addAndGet(1);
            logger.info(">>>有客户加入，当前总人数为---"+sum+"<<<");
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
    public void onMessage(String message, Session session, @PathParam(value="userId")String userId) {
        System.out.println("来自客户端的消息:" + message);
        Thread a = new TestSendMsgTask(session, message);
        threadMap.put(userId, a);
    }
    
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam(value="userId")String userId) {
        logger.info("客户端退出>>>"+session.getId());
        try {
            TestSendMsgTask c = (TestSendMsgTask) threadMap.get(userId);
            c.close();
            c.interrupt();
            c.join();
            threadMap.remove(userId);
            sockClient.remove(userId);
            sum.addAndGet(-1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("调用webSocket发生错误", error);
    }
    
}
