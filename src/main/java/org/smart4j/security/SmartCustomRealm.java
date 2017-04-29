package org.smart4j.security;


import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.HashSet;
import java.util.Set;


/**
 * 基于smart的自定义Realm(需要实现SmartSecurity接口)
 * Created by Administrator on 2017/4/24 0024.
 */
public class SmartCustomRealm extends AuthorizingRealm {
    private final SmartSecurity smartSecurity;
    public  SmartCustomRealm(SmartSecurity smartSecurity){
        this.smartSecurity = smartSecurity;
        super.setName(SecurityConstant.REALMS_CUSTOM);
        super.setCredentialsMatcher(new Md5CredentialsMatcher());
    }
    @Override
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)throws AuthenticationException {
        if (token==null){
            throw new AuthenticationException("parameter token is null");
        }
        //通过AuthenticatioinToken对象获取从表单中提交过来的用户名
        String username = ((UsernamePasswordToken)token).getUsername();
        //通过SmartSecurity接口并根据用户名获取数据库中存放的密码
        String password = smartSecurity.getPassword(username);
        //将用户名与密码放入AuthenticationInfo对象中,便于后续的认证操作
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo();
        authenticationInfo.setPrincipals(new SimplePrincipalCollection(username,super.getName()));
        authenticationInfo.setCredentials(password);
        return authenticationInfo;
    }
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection princils){
        if (princils==null){
            throw new AuthenticationException("paramter principals is null");
        }
        //获取已认证的用户
        String username = (String)super.getAvailablePrincipal(princils);
        //通过SmartSecurity接口并根据用户名获取角色名集合
        Set<String> roleNameSet = smartSecurity.getRoleNameSet(username);
        //通过SmartSecurity接口并根据角色名获取其对应的权限名集合
        Set<String> permissionNameSet = new HashSet<String>();
        if (roleNameSet!=null&&roleNameSet.size()>0){
            for (String roleName:roleNameSet){
                Set<String> currentPermissioinNameSet = smartSecurity.getPermissionNameSet(roleName);
                permissionNameSet.addAll(currentPermissioinNameSet);
            }
        }
        //将角色集合与权限名集合放入AuthorizationInfo对象中,便于后续的授权操作
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        authenticationInfo.setRoles(roleNameSet);
        authenticationInfo.setStringPermissions(permissionNameSet);
        return authenticationInfo;
    }
}
