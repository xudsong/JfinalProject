package hisense.code.casRealm;

import com.alibaba.fastjson.JSON;
import hisense.code.model.User.User;
import hisense.code.service.sys.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShiroCas extends CasRealm {
    private UserService userService=new UserService();
    //private DbUtil dbUtil=new DbUtil();

    protected final Map<String, SimpleAuthorizationInfo> roles = new ConcurrentHashMap<String, SimpleAuthorizationInfo>();

    /**
     * 设置角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = null;
        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();
            try {
                authorizationInfo.addStringPermissions(userService.findPermissions(userName));
                authorizationInfo.addRoles(userService.findRoles(userName));
                roles.put(userName, authorizationInfo);
			    /*Subject subject=SecurityUtils.getSubject();
			    Session session=subject.getSession();
			    session.setAttribute("info", "session的数据");*/
                //.setAttribute("info", "session的数据");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
        return authorizationInfo;
    }


    /**
     * 1、CAS认证 ,验证用户身份
     * 2、将用户基本信息设置到会话中
     */
    protected final Map<String, List<String>> map = new HashMap<String, List<String>>();
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {

        AuthenticationInfo authc = super.doGetAuthenticationInfo(token);
        String userName = (String) authc.getPrincipals().getPrimaryPrincipal();

        try{

            List<String> user=userService.findUser(userName);
            if(user!=null){
                SecurityUtils.getSubject().getSession().setAttribute("user", user);
                map.put("roles", userService.findRoles(userName));
                map.put("permissions",userService.findPermissions(userName));
                //JSONObject jsonObject = JSONObject.fromObject(map);
                //System.out.println(JSONObject.fromObject(user));
                SecurityUtils.getSubject().getSession().setAttribute("mapinfo", JSON.toJSONString(map));
                SecurityUtils.getSubject().getSession().setAttribute("info", "session的数据");
                SecurityUtils.getSubject().getSession().setAttribute("roles", userService.findRoles(userName));
                SecurityUtils.getSubject().getSession().setAttribute("permissions",userService.findPermissions(userName));
                //AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(user.getUserName(),user.getPassWord(),"xx");
                return authc;
            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
