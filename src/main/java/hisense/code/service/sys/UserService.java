package hisense.code.service.sys;

import hisense.code.model.TUser;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import hisense.code.model.User.User;

import java.util.List;

public class UserService {

    static final TUser dao = TUser.dao;

    /**
     * @return
     */

    //根据传入的 Id 来实现对一个对象的获取
    public String getOne() {
        TUser tUser = dao.findById(1);
        return tUser.getUsername();
    }

    //获取对象集合的方法 返回类型为List<T>
    public List<Record> getList() {
        //List<TUser> tUserList = dao.find("select * from t_user");  //这里调用的方法是 Jfinal 提供的
        String sql = Db.getSql("findUserList");
        List<Record> userList = Db.find(sql, 15, 20);
        return userList;
    }

    //分页查询
    public Page<TUser> getListByPage(int pageNum) {
        //构成所需要的参数
        String sql = "from t_user";
        //int pageNum = getParaToInt("pageNo",1);
        Page<TUser> tUserList = dao.paginate(pageNum, 2, "select *", sql);
        return tUserList;
    }


    //分页查询 ( Mysql  方式1：利用 jfinal 提供的方法 )
    //需要两个参数 1.当前页 2.每页显示条数
    public Page<Record> getUserListByPage(int nowPage, int pageRow) {
        SqlPara sqlPara = Db.getSqlPara("findUserListByPage", 13, 30);
        Page<Record> userList = Db.paginate(nowPage, pageRow, sqlPara);
        return userList;

    }


    //分页查询 ( Mysql ) 方式2：自己写 Sql
    public List<Record> getUserListByPageAnother(int nowPage, int pageRow) {
        int limit1 = (nowPage - 1) * pageRow;
        int limit2 = nowPage * pageRow;
        String sql = Db.getSql("findUserListByPageAnother");
        List<Record> userList = Db.find(sql, limit1, limit2);
        return userList;

    }


    //添加方法
    public Boolean postUser(TUser user) {
        Boolean saveOk = user.save();
        return saveOk;
    }


    //删除方法 (根据 id 删除)
    public Boolean deleteUser(int id) {
        Boolean deleteOk = dao.deleteById(id);
        return deleteOk;
    }

    //修改方法
    public int putUser() {
        String sql = Db.getSql("updateUser");
        int updateOk = Db.update(sql, 20, "toms", 1);
        return updateOk;
    }


    //高级用法测试 (可以动态传参数 sql 自动拼接过滤条件)
    public List<Record> findTest() {
        Kv cond = Kv.by("age > ", 16).set("name= ", "a");
        SqlPara sp = Db.getSqlPara("find", Kv.by("cond", cond));
        List<Record> userList = Db.find(sp);
        return userList;
    }

    //查找用户
    public List<String> findUser(String userName)
    {
        String sql=Db.getSql("findUser");
       // User us=User.dao.findFirst(sql,userName);
       // return User.dao.findFirst(sql,userName);
        return Db.query(sql,userName);
    }

    //根据用户名查找角色信息
    public List<String> findRoles(String userName){
        String sql = Db.getSql("findRoles");
        return Db.query(sql, userName);
    }

    //根据用户名查找权限信息
    public List<String> findPermissions(String userName){
        String sql=Db.getSql("findPermissions");
        return Db.query(sql, userName);
    }

}
