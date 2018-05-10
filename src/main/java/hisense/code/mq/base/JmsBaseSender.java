package hisense.code.mq.base;


import org.apache.activemq.pool.PooledConnection;

import javax.jms.*;


/**
 * 消息队列发送者
 * Created by zhanghaichao on 2018/4/24.
 */
public class JmsBaseSender {

    private String name;
    private Session session;
    private MessageProducer producer;
    private ActiveMQ.Destination type;

    public JmsBaseSender(String name,
                         PooledConnection connection,
                         ActiveMQ.Destination type) throws JMSException {
        this.type = type;
        this.name = name;
        // 事务性会话，自动确认消息
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void setSubject(String subject) {
        // 消息的目的地（Queue/Topic）
        try {
            if (type.equals(ActiveMQ.Destination.Topic)) {
                Topic destination = session.createTopic(subject);
                producer = session.createProducer(destination);
            } else {
                Queue destination = session.createQueue(subject);
                producer = session.createProducer(destination);
            }
            // 不持久化消息
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }

    public void sendMessage(Message message) throws JMSException {
        producer.send(message);
    }

}
