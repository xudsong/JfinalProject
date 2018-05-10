package hisense.code.config.mapping;

import hisense.code.model.TUser;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class MappingKit {

    /**
     * 数据库中数据表映射关系
     *
     * @param arp
     */
    public static void mappingMySql(ActiveRecordPlugin arp) {
        arp.addMapping("t_user", "id", TUser.class);                   // 用户信息表

        //从资源文件中加载
        arp.addSqlTemplate("mapper/all.sql");
    }
}
