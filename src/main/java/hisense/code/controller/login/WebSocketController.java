package hisense.code.controller.login;

import hisense.code.utils.WsPool;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by zhanghaichao on 2018/5/2.
 */
@ServerEndpoint("/websocket/{token}")
public class WebSocketController{
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token")String token) {
        System.out.println("in::"+session.getId());
        WsPool.addUser(token,session);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("out::"+session.getId());
        WsPool.removeBySessionId(session);
    }


    @OnMessage
    public void onMessage(String requestJson, Session session) {
//            session.getAsyncRemote().sendText(requestJson);
        WsPool.sendToAll(requestJson);
    }
}
