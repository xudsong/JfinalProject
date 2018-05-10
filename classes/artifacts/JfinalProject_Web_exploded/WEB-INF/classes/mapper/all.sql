-- 查询结果集
#sql("findUserList")
select * from t_user where age > ? and age < ? and weight < 50 ;
#end

-- 分页查询(Mysql)
#sql("findUserListByPage")
SELECT * FROM t_user where age > #para(0) and age < #para(1)
#end

-- 分页查询(Mysql)
#sql("findUserListByPageAnother")
SELECT * FROM t_user LIMIT ?,? ;
#end


-- 更新数据
#sql("updateUser")
UPDATE t_user SET age = ?, username=? WHERE id=?;
#end


-- 测试高级用法
#sql("find")
select * from girl
#for(x : cond)
#(for.index == 0 ? "where ": " and") #(x.key) #para(x.value)
#end
#end

--查找用户
#sql("findUser")
select * from t_user where userName=?;
#end

--查找角色信息
#sql("findRoles")
select roleName from t_user u,t_role r where u.roleId=r.id and u.userName=?;
#end

--查找权限信息
#sql("findPermissions")
select permissionName from t_user u,t_role r,t_permission p where u.roleId=r.id and p.roleId=r.id and u.userName=?;
#end