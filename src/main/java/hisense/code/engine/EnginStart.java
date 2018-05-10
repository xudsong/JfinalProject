package hisense.code.engine;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import hisense.code.config.dbcp.DataSourceConnectPool;
import hisense.code.config.handler.WebSocketHandler;
import hisense.code.config.mapping.MappingKit;
import hisense.code.controller.login.LoginController;
import hisense.code.controller.login.TestController;
import hisense.code.interceptor.AuthCheckInterceptor;
import hisense.code.interceptor.ExceptionInterception;
import hisense.code.mq.base.ActiveMQPlugin;
import org.apache.log4j.Logger;

public class EnginStart extends JFinalConfig {

    Logger logger = Logger.getLogger(EnginStart.class);

    /**
     * 配置常量
     *
     * @param me
     */
    @Override
    public void configConstant(Constants me) {
        // 加载配置文件，随后可用PropKit.get(...)获取值
        PropKit.use("dbconfig.properties");
        // 设置开发模式，默认使用false
        me.setDevMode(PropKit.getBoolean("devMode", false));
        // 设置编码格式
        me.setEncoding("utf-8");
        // 设置视图类型
//        me.setViewType(ViewType.JSP);
        //文件上传路径
        me.setBaseUploadPath("E:\\APP\\JAVA\\JfinalProject\\classes\\artifacts\\JfinalProject_Web_exploded\\upload\\");
    }

    /**
     * 配置路由
     *
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("/view");//页面文件位置配
        // 使用前台路由、后台路由管理
        // 首页
        me.add("/", LoginController.class);

        me.add("/test", TestController.class, "test");
    }

    @Override
    public void configEngine(Engine engine) {
    }

    /**
     * 配置插件
     *
     * @param me
     */
    @Override
    public void configPlugin(Plugins me) {
        // 数据库连接也可以使用以下的方式(Druid)
        DruidPlugin druid_mySql = DataSourceConnectPool.getMySqlDruid();
        me.add(druid_mySql);
        ActiveRecordPlugin Arp_mySql = new ActiveRecordPlugin("ms", druid_mySql);
        Arp_mySql.setDialect(new MysqlDialect());
        Arp_mySql.setContainerFactory(new CaseInsensitiveContainerFactory());
        Arp_mySql.getEngine().setSourceFactory(new ClassPathSourceFactory());

        MappingKit.mappingMySql(Arp_mySql);
        me.add(Arp_mySql);

        //activemq创建
       /* ActiveMQPlugin.getInstance().initMQ(String.format("failover://(tcp://%s:%s?useInactivityMonitor=false)?initialReconnectDelay=1000",
                PropKit.get("MQ_HOST"),
                PropKit.get("MQ_PORT")),
                PropKit.get("MQ_USERNAME"),
                PropKit.get("MQ_PASSWORD"));

        me.add(ActiveMQPlugin.getInstance());*/

        // 用于缓存bbs模块的redis服务
//        String redispass = PropKit.get("REDIS_PASSWORD");
//        RedisPlugin bbsRedis = null;
//        if (StringUtils.isNotNull(redispass)) {
//            bbsRedis = new RedisPlugin("bbs", PropKit.get("REDIS_HOST"), Integer.parseInt(PropKit.get("REDIS_PORT")),
//                    redispass);
//        } else {
//            bbsRedis = new RedisPlugin("bbs", PropKit.get("REDIS_HOST"), Integer.parseInt(PropKit.get("REDIS_PORT")));
//        }
//        me.add(bbsRedis);

    }

    /**
     * 配置全局拦截器，对所有请求进行拦截,类和方法使用@Before(Class.class)即能使用
     *
     * @param me
     */
    @Override
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new ExceptionInterception());
        me.add(new AuthCheckInterceptor());
    }

    /**
     * 配置处理器
     *
     * @param me
     */
    @Override
    public void configHandler(Handlers me) {
        me.add(new WebSocketHandler("^/websocket"));
    }

    @Override
    public void afterJFinalStart() {
        System.out.println("启动成功");
    }

    @Override
    public void beforeJFinalStop() {
    }
}
