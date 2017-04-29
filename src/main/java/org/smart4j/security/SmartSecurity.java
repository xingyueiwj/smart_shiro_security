package org.smart4j.security;

import java.util.Set;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public interface SmartSecurity {
    //根据用户名获取密码
    String getPassword(String username);
    //根据用户名获取角色名集合
    Set<String>getRoleNameSet(String username);
    //根据角色名获取权限名集合
    Set<String>getPermissionNameSet(String rolename);
}
