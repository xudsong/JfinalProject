/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * @author hisense
 */
public class JavaTest {

    /**
     * @param args the command line arguments
     *             将数据存入高速缓存ignite内存数据库中
     */
    public static void main(String[] args) {
        try {
            Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
            // Open the JDBC connection.INSERT INTO Person (id, name, city_id) VALUES (3, 'Mary Major', 1);
            Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1");
            // Query names of all people.
            Statement sta = conn.createStatement();

            sta.executeUpdate("DELETE FROM person");
//            sta.executeUpdate("CREATE TABLE person (id LONG, name VARCHAR, city_id LONG, PRIMARY KEY(id)) WITH \"TEMPLATE=replicated\"");
            PreparedStatement pst = conn.prepareStatement("INSERT INTO person (id, name, city_id) VALUES (?, ?, ?)");

            for (int i = 0; i < 100; i++) {
                pst.setInt(1, i);
                pst.setInt(3, 2);
                pst.addBatch();//添加一次预定义参数
                if(i%10==0) {
                    pst.setString(2, "王芳");
                    pst.executeBatch();//批量执行预定义参数
                    pst.clearBatch();//清除缓存
                }else{
                    pst.setString(2, "王宝山");
                }
            }
            pst.executeBatch();
            pst.close();
           // sta.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
