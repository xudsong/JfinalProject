
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.cache.expiry.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache.Entry;
import javax.cache.expiry.CreatedExpiryPolicy;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheManager;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Ignite内存中的数据模式有三种，分别是LOCAL、REPLICATED和PARTITIONED
 * 
 */

/**
 * IGnite core 依赖javax.cache1.0.0\org.jetbrains13.0\org.gridgain1.0.0
 *
 */
public class IGniteTest {
    /***
     * 用表格方式插入数据，如果采用单条方式（普通sql和preparestatement方式都是十万条数据33秒左右，
     * 如果用批量插入十万则6秒左右(100万每一千一个批量13秒左右），100万查询速度两秒左右
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void JdbcTest() throws ClassNotFoundException, SQLException
    {        
        //Register JDBC driver
        Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
        // Open the JDBC connection.INSERT INTO Person (id, name, city_id) VALUES (3, 'Mary Major', 1);
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1");
        // Query names of all people.
        Statement sta = conn.createStatement();
        //Query people with specific param using prepared statement
        PreparedStatement pst= conn.prepareStatement("INSERT INTO person (id, name, city_id) VALUES (?, ?, ?)");
//        sta.executeUpdate("DELETE FROM person");
//        //sta.executeUpdate("CREATE TABLE person (id LONG, name VARCHAR, city_id LONG, PRIMARY KEY(id)) WITH \"TEMPLATE=replicated\"");
//        long curTime = System.currentTimeMillis();
//        System.out.println(curTime);
//        //使用addBatch批量执行预定义模式的SQL
//        for(int i = 0; i < 1000000; i++)
//        {
//            pst.setInt(1, i);
//            pst.setInt(3, 2);
//            pst.addBatch();//添加一次预定义参数
//            if (i%1000 == 0)
//            {
//
//                pst.setString(2, "王芳");
//                pst.executeBatch();//批量执行预定义参数
//                pst.clearBatch();//清除缓存
//            }
//            else
//                pst.setString(2, "王宝山");
//        }
//        pst.executeBatch();
       
//        System.out.print("一百万数据插入时间：" + (System.currentTimeMillis() - curTime));
//        //sta.executeUpdate("INSERT INTO person (id, name, city_id) VALUES (2, '王芳', 2)");
//        curTime = System.currentTimeMillis();
        ResultSet rs = sta.executeQuery("select name from person where name = '王宝山'");
        while (rs.next()) {
            String name = rs.getString(1);      
        }
//        System.out.print("一百万数据查询时间：" + (System.currentTimeMillis() - curTime));
        rs.close();
        pst.close();
        sta.close();
        conn.close();
    }
    
    /***
     * 普通的put和get方式十万数据插入时间：41603十万数据读取时间：36809十万数据移除时间：39210(对象和map结构差不多)
     * 异步的put和get方式十万数据插入时间：5467十万数据读取时间：3121十万数据移除时间：4330
     */
    public void cacheTest() throws ClassNotFoundException, SQLException
    {
        //Ignition.start("example-cache.xml");
        Ignition.setClientMode(true);
        //Ignition.start("example-ignite.xml");
        Ignition.start();
        Ignite ignite = Ignition.ignite(); // 当ignite为客户端方式时，启动时会等待服务端启动和连接成功后才会向下继续
        
 
        CacheConfiguration<Integer, Person> cacheOnlyPersonCfg = new CacheConfiguration<Integer, Person>("personCache");
        cacheOnlyPersonCfg.setIndexedTypes(Integer.class,Person.class); // 注意配置注册key和value是为了sql查询
        // Get an instance of named cache.
        final IgniteCache<Integer, Person> cache = ignite.getOrCreateCache(cacheOnlyPersonCfg);//"personCache");
        //cache.withExpiryPolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.MINUTES, 5))); // 设置过期策略（创建开始5分钟）
        
       // cache.getConfiguration(CacheConfiguration.class).setIndexedTypes(Integer.class, Person.class);
        
        
        long curTime = System.currentTimeMillis();
        // Store keys in cache.
        for (int i = 0; i < 10000; i++)
        {
            Person ps = new Person();
            //Map<String, Object> mp = new HashMap();
            //mp.put("id", i);
            
            
            ps.setId(i);
            //ps.setCity_id(5);
            if (i%1000 == 0)
            {                
                ps.setName("王芳");   
                //mp.put("name", "王芳");
                //mp.put("city_id", 5);
                ps.setCity_id(5);
            }
            else
            {
                ps.setName("王宝山");
                //mp.put("name", "王宝山");
                //mp.put("city_id", 6);
                ps.setCity_id(6);
            }
            cache.put(i, ps);
            //cache.put(i, mp);
        }
        //System.out.print("十万数据插入时间：" + (System.currentTimeMillis() - curTime));
      
        curTime = System.currentTimeMillis();
        // Retrieve values from cache.
        for (int i = 0; i < 100000; i++)
        {
           //Person person = cache.getAsync(i).get();
            cache.getAsync(i);
           //System.out.println(person.getName());
        }
        System.out.print("十万数据读取时间：" + (System.currentTimeMillis() - curTime));

        SqlQuery<Integer, Person> sql = new SqlQuery(Person.class, "name = ?");
        sql.setArgs("王芳");
        List<Entry<Integer, Person>> cursor = cache.query(sql).getAll();
        if (cursor != null)
        {
            for( Entry<Integer, Person> p : cursor)
            {
                System.out.println(p.getValue().getName());
            }
        }
            // Execute query to get names of all employees.
//            SqlFieldsQuery sql = new SqlFieldsQuery(
//              "select name from Person where city_id = 5");
//
//            // Iterate over the result set.
//            try (QueryCursor<List<?>> cursor = cache.query(sql)) {
//              for (List<?> row : cursor)
//                System.out.println("personName=" + row.get(0));
//            }

//        try (QueryCursor<Person> cursor = cache.query(new ScanQuery((k, p) -> p.getName() = "王芳"))) {
//          for (Person p : cursor)
//            System.out.println(p.getName());
//        }

        
//        IgniteBiPredicate<Integer, Person> filter;
//        filter = new IgniteBiPredicate<Integer, Person>() {
//            @Override
//            public boolean apply(Integer key, Person p) {
//                return p.getName().equals("王芳");
//            }
//        };
//      try (QueryCursor<Person> cursor = cache.query(new ScanQuery(filter))) {
//        for (Person p : cursor)
//          System.out.println(p.getName());
//      }
        
        //curTime = System.currentTimeMillis();
        // Remove objects from cache.
//        for (int i = 0; i < 100000; i++)
//            cache.removeAsync(i);
       // System.out.print("十万数据移除时间：" + (System.currentTimeMillis() - curTime));
        //cache.removeAllAsync();
        
        // Atomic put-if-absent.
        //cache.putIfAbsent(1, "1");

        // Atomic replace.
        //cache.replace(1, "1", "2");
        
        ignite.destroyCache("personCache");
        ignite.close();
        Ignition.stopAll(true);
    }
}
