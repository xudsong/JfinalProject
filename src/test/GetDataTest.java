import com.alibaba.fastjson.JSON;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    /**
     * Created by xudasong on 2018/4/23.
     */
    public class GetDataTest {
        public static void main(String[] args) {

            try {

                Class.forName("org.apache.ignite.IgniteJdbcThinDriver");

                // Open JDBC connection  打开到集群节点的连接，监听地址为本地
                Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");

                ResultSet rs = conn.createStatement().executeQuery("select name from person");
                Map<String, Object> map = new HashMap<>();
                //将ResultSet结果集转换成List
                List nameList=convertList(rs);
                //将List结果集存入map中
                map.put("peopleNames",nameList);
                //将map转换成Json格式
                String jsonString= JSON.toJSONString(map);
                System.out.println(jsonString);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static List convertList(ResultSet rs) throws SQLException{
            List list = new ArrayList();
            ResultSetMetaData md = rs.getMetaData();//获取键名
            int columnCount = md.getColumnCount();//获取行的数量
            while (rs.next()) {
                Map rowData = new HashMap();//声明Map
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
                }
                list.add(rowData);
            }
            return list;
        }
    }

