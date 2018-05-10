package hisense.code.config.dbcp;

import hisense.code.config.mapping.MappingKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 数据库连接池
 *
 * @author durunguo
 */
public class DataSourceConnectPool {

    private static Prop dbconfig;

    static {
        dbconfig = PropKit.use("dbconfig.properties");
    }

    /**
     * 配置Druid数据库连接池插件：MySql连接（非使用，供参考）
     *
     * @return
     */
    public static DruidPlugin getMySqlDruid() {
        Prop dbconfig = PropKit.use("dbconfig.properties");
        return new DruidPlugin(dbconfig.get("MYSQL_URL"), dbconfig.get("MYSQL_USERNAME"), dbconfig.get("MYSQL_PASSWORD").trim());
    }

    /*
     * start方法 为了直接使用main方法测试
     */
    public static void startPlugin() {
        PropKit.use("dbconfig.properties");
        DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("MYSQL_URL"),
                PropKit.get("MYSQL_USERNAME"), PropKit.get("MYSQL_PASSWORD")
                .trim());
        ActiveRecordPlugin arp = new ActiveRecordPlugin("mysql", druidPlugin);
        MappingKit.mappingMySql(arp);
        druidPlugin.start();
        arp.start();
    }
}
