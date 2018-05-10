package hisense.code.mq.base;

import org.apache.activemq.pool.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * 消息接收者基础继承类
 */
public abstract class JmsBaseAbsReceiver implements MessageListener {
    private String name;
    private Session session;
    private MessageConsumer consumer;
    private static final Logger logger = LoggerFactory.getLogger(JmsBaseAbsReceiver.class);

    public JmsBaseAbsReceiver(String name,
                       PooledConnection connection,
                       ActiveMQ.Destination type,
                       String subject) throws JMSException {
        this.name = name;
        // 事务性会话，自动确认消息
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 消息的目的地（Queue/Topic）
        if (type.equals(ActiveMQ.Destination.Topic)) {
            Topic destination = session.createTopic(subject);
            consumer = session.createConsumer(destination);
        } else {
            Queue destination = session.createQueue(subject);
            consumer = session.createConsumer(destination);
        }
        consumer.setMessageListener(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public void onMessage(Message message) {
        logger.error("未对消息进行处理{}",message);
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}