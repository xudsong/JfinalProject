package hisense.code.mq.base;

/**
 * Created by zhanghaichao on 2018/5/3.
 */

import hisense.code.mq.JmsDemoReceiver;
import hisense.code.utils.StringUtils;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * activeMq插件
 */
public class ActiveMQPlugin implements IPlugin {
    private static final Logger logger = LoggerFactory.getLogger(ActiveMQPlugin.class);
    private String url;
    private String name;
    private String password;
    private static ActiveMQPlugin _instance;

    public static ActiveMQPlugin getInstance() {
        if (_instance == null) {
            _instance = new ActiveMQPlugin();
        }
        return _instance;
    }


    /**
     * 隐藏构造器
     */
    public ActiveMQPlugin() {
    }

    public void initMQ(String url,
                       String name,
                       String password) {
        this.url = url;
        if (StringUtils.isNotNull(name)) {
            this.name = name;
        } else {
            this.name = ActiveMQConnection.DEFAULT_USER;
        }

        if (StringUtils.isNotNull(password)) {
            this.password = password;
        } else {
            this.password = ActiveMQConnection.DEFAULT_USER;
        }


    }


    @Override
    public boolean start() {
        logger.info("初始化activeMQ配置");
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setUserName(name);
        activeMQConnectionFactory.setPassword(password);
        activeMQConnectionFactory.setBrokerURL(url);
        activeMQConnectionFactory.setDispatchAsync(true);//异步发送消息

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);
        pooledConnectionFactory.setMaximumActiveSessionPerConnection(200);
        pooledConnectionFactory.setIdleTimeout(120);
        pooledConnectionFactory.setMaxConnections(5);
        pooledConnectionFactory.setBlockIfSessionPoolIsFull(true);
        try {
            PooledConnection connection = (PooledConnection) pooledConnectionFactory.createConnection();
            connection.start();
            ActiveMQ.addConnection(name, connection);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        //定义消息队列接收者发送者
        try {
            //添加进行推送的推送者名字，可定义多个
            ActiveMQ.addSender(new JmsBaseSender(PropKit.get("MQ_SENDER"), ActiveMQ.getConnection(name), ActiveMQ.Destination.Queue));//定义发送者
            //定义接收者的名字，可定义多个
            ActiveMQ.addReceiver(new JmsDemoReceiver(PropKit.get("MQ_RECEIVER"), ActiveMQ.getConnection(name), ActiveMQ.Destination.Queue, PropKit.get("MQ_SUBJECT")));//定义接受者
        } catch (Exception e) {
            logger.error("启动activemq失败", e);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean stop() {
        System.exit(0);
        return true;
    }
}