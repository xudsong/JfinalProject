package hisense.code.mq.base;

import org.apache.activemq.pool.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * activeMQ工具
 */
public class ActiveMQ {
    public static final ConcurrentHashMap<String, PooledConnection> pooledConnectionMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, JmsBaseSender> senderMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, JmsBaseAbsReceiver> receiverMap = new ConcurrentHashMap<>();



    private static final Logger logger = LoggerFactory.getLogger(ActiveMQ.class);

    public static void addSender(JmsBaseSender sender) {
        senderMap.put(sender.getName(), sender);
    }

    public static JmsBaseSender getSender(String name, String subject) {

        JmsBaseSender jmsBaseSender = senderMap.get(name);
        jmsBaseSender.setSubject(subject);
        return jmsBaseSender;
    }

    public static void addReceiver(JmsBaseAbsReceiver receiver) {
        receiverMap.put(receiver.getName(), receiver);
    }

    public static JmsBaseAbsReceiver getReceiver(String name) {
        return receiverMap.get(name);
    }

    public static void addConnection(String connectionName,
                                     PooledConnection connection) {
        pooledConnectionMap.put(connectionName, connection);
    }

    public static PooledConnection getConnection(String connectionName) {
        return pooledConnectionMap.get(connectionName);
    }


    /**
     * 目标类型
     * Created by zhanghaichao on 2018/4/24.
     */
    public static enum Destination {
        Queue, Topic
    }
}
