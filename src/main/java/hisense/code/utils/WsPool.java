package hisense.code.utils;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket链接记录
 * Created by zhanghaichao on 2018/5/2.
 */
public class WsPool {

    /**
     * websocket 用户与连接对应关系
     */
    private static final Map<String , Session> userWsMap = new HashMap<String, Session>();

    /**
     * 记录webocketid 与用户之间的关系
     */
    private static final Map<String , String> wsUserMap = new HashMap<String, String>();



    /**
     * 根据userName获取WebSocket,这是一个list,此处取第一个
     * 因为有可能多个websocket对应一个userName（但一般是只有一个，因为在close方法中，我们将失效的websocket连接去除了）
     *
     */
    public static Session getWsByUser(String userkey) {
        return userWsMap.get(userkey);
    }

    /**
     * 向连接池中添加连接
     *
     */
    public static void addUser(String userkey, Session conn) {
        userWsMap.put(userkey, conn); // 添加连接
        wsUserMap.put(conn.getId(),userkey);
    }


    /**
     * 移除连接池中的连接根据 用户的标识
     */
    public static boolean removeByUser(String userkey) {
        Session conn = userWsMap.get(userkey);
        if (conn != null) {
            userWsMap.remove(userkey); // 移除连接
            wsUserMap.remove(conn.getId()); // 移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据链接信息删除对应的的链接
     * @param conn
     * @return
     */
    public static boolean removeBySessionId(Session conn){
        String id = conn.getId();
        String userkey = wsUserMap.get(id);
        if (StringUtils.isNotNull(userkey)) {
            userWsMap.remove(userkey); // 移除连接
            wsUserMap.remove(id); // 移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向特定的用户发送数据
     * @param message
     */
    public static void sendMessageToUser(String userkye, String message) {
        if (null != userkye) {
            Session conn = userWsMap.get(userkye);
            if(null != conn){
                conn.getAsyncRemote().sendText(message);
            }
        }
    }

    /**
     * 推送给所有给用户
     * @param message
     */
    public static void sendToAll(String message){
        for(Session conn : userWsMap.values()){
            conn.getAsyncRemote().sendText(message);
        }
    }

}
