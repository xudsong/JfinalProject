<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%-- <script type="text/javascript">
	 var myName="<%=session.getAttribute("info")%>";
	 var myuserinfo="<%=session.getAttribute("userinfo")%>";
	 var myuser="<%=session.getAttribute("user")%>";
	 var myusermap="<%=session.getAttribute("mapinfo")%>";
	 alert(myuserinfo);
	 sessionStorage.setItem('info',myName);
	 sessionStorage.setItem('userinfo',myuserinfo);
	 sessionStorage.setItem('user',myuser);
	 sessionStorage.setItem('usermap',myusermap);
	 alert(sessionStorage.getItem('userinfo'));
	 alert(sessionStorage.getItem('info'));
	</script>--%>
	<script type="text/javascript">
	var myName="<%=session.getAttribute("info")%>";
	var myuserroles="<%=session.getAttribute("roles")%>";
	var myuserpermissions="<%=session.getAttribute("permissions")%>";
	 alert(myName);
	 sessionStorage.setItem('info',myName);
	 sessionStorage.setItem('roles',myuserroles);
	 sessionStorage.setItem('permissions',myuserpermissions);
    //将后台json数据对象转成json字符串并存于H5的SessionStorage中
    var myusermap=JSON.stringify(<%=session.getAttribute("mapinfo")%>);
    alert(myusermap);
    sessionStorage.setItem('usermap',myusermap);
	</script>
	${info}
	欢迎你!
	<shiro:hasRole name="admin">
		欢迎有admin角色的用户！<shiro:principal/>
	</shiro:hasRole>
	<shiro:hasPermission name="student:create">
		欢迎有student:create权限的用户！<shiro:principal/>
	</shiro:hasPermission>
	<a href="http://127.0.0.1:8081/shiro-cas">节点1</a>
	
	<!-- <a href="http://127.0.0.1:8082/node2/shiro-cas">节点2</a> -->
	
	<a href="http://127.0.0.1:8081/logout">单点登出</a>
</body>
</html>