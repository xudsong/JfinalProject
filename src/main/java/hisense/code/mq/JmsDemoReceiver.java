package hisense.code.mq;

import hisense.code.mq.base.ActiveMQ;
import hisense.code.mq.base.JmsBaseAbsReceiver;
import org.apache.activemq.pool.PooledConnection;

import javax.jms.*;
import java.util.Enumeration;

/**
 * 继承基础消息队列监听的demo
 * Created by zhanghaichao on 2018/4/25.
 */
public class JmsDemoReceiver extends JmsBaseAbsReceiver {
    public JmsDemoReceiver(String name, PooledConnection connection, ActiveMQ.Destination type, String subject) throws JMSException {
        super(name, connection, type, subject);
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println(msg.getText());
            } else if (message instanceof MapMessage) {
                MapMessage msg = (MapMessage) message;
                Enumeration enumer = msg.getMapNames();
                while (enumer.hasMoreElements()) {
                    Object obj = enumer.nextElement();
                    System.out.println(msg.getObject(obj.toString()));
                }
            } else if (message instanceof StreamMessage) {
                StreamMessage msg = (StreamMessage) message;
                System.out.println(msg.readString());
                System.out.println(msg.readBoolean());
                System.out.println(msg.readLong());
            } else if (message instanceof ObjectMessage) {
                ObjectMessage msg = (ObjectMessage) message;
                System.out.println(msg);
            } else if (message instanceof BytesMessage) {
                BytesMessage msg = (BytesMessage) message;
                byte[] byteContent = new byte[1024];
                int length = -1;
                StringBuffer content = new StringBuffer();
                while ((length = msg.readBytes(byteContent)) != -1) {
                    content.append(new String(byteContent, 0, length));
                }
                System.out.println(content.toString());
            } else {
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
