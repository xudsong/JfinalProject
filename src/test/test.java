import com.google.protobuf.InvalidProtocolBufferException;
import hisense.code.model.proto.PeopleDto;
import hisense.code.model.proto.StudentDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by zhanghaichao on 2018/4/23.
 */
public class test {
    public static void main(String[] args) {

//        StudentDto.Student.Builder buidler = StudentDto.Student.newBuilder();
//        buidler.setName("Frank");
//        buidler.setNumber(123456);
//        buidler.setHobby("music");
//
//        PeopleDto.People.Builder people = PeopleDto.People.newBuilder();
//        people.setNameP("哈哈哈哈");
//        people.setNumberP(666);
//
//        buidler.addPeople(people);
//        buidler.addPeople(people);
//
//        StudentDto.Student student = buidler.build();
//        System.out.println(student.toString());
//
//        System.out.println(student.getPeople(0).getNameP());
//
//
//        byte[] array = student.toByteArray();
//
//        try {
//            StudentDto.Student student1 = StudentDto.Student.parseFrom(array);
//            System.out.println("=================");
//            System.out.println(student1.toString());
//            System.out.println(student1.getPeople(1).getNameP());
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }

        try {

            Class.forName("org.apache.ignite.IgniteJdbcThinDriver");

            // Open JDBC connection  打开到集群节点的连接，监听地址为本地
            Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Order_D");
            while (rs.next()) {
                String name = rs.getString(1);
                System.out.println(name);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
