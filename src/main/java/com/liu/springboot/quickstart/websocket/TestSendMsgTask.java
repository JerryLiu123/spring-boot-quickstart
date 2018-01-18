package com.liu.springboot.quickstart.websocket;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class TestSendMsgTask extends Thread{

    private static Logger logger = Logger.getLogger(TestSendMsgTask.class);
    
    private Session session;
    private String clientMsg;
    private boolean isRunning = true;
    
    public TestSendMsgTask(Session session, String clientMsg) {
        super();
        this.session = session;
        this.clientMsg = clientMsg;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(isRunning) {
            try {
                session.getBasicRemote().sendText(">>>"+clientMsg+"<<<");
                logger.info("--------------------"+clientMsg);
                Thread.sleep(3000);
            } catch (Exception e) {
                // TODO: handle exception
                logger.error("错误----", e);
                this.close();
                break;
            }
        }
        logger.info("向客户端发送信息线程退出!!!");
    }
    
    public void close() {
        this.isRunning = false;
    }
    
}
