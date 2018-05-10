package hisense.code.model.Role;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

public class Role extends Model<Role> {
    public static final Role dao=new Role();

    public void aaa(){
        List<Role> a = dao.find("");
    }
}
