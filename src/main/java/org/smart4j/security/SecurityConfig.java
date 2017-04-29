package org.smart4j.security;

import org.smart4j.framework.core.ConfigHelper;

/**
 * Created by Administrator on 2017/4/26 0026.
 */
public final class SecurityConfig {
    public static String getRealms(){
        return ConfigHelper.getString(SecurityConstant.REALMS);
    }
    public static SmartSecurity getSmartSecurity()throws Exception{
        String className = ConfigHelper.getString(SecurityConstant.SMART_SECURITY);
        return (SmartSecurity) ReflectionUtil.newInstance(className);
    }
    public static String getJdbcAuthcQuery(){
        return ConfigHelper.getString(SecurityConstant.JDBC_AUTHC_QUERY);
    }
    public static String getJdbcRolesQuery(){
        return ConfigHelper.getString(SecurityConstant.JDBC_ROLES_QUERY);
    }
    public static String getJdbcPermissionsQuery(){
        return ConfigHelper.getString(SecurityConstant.JDBC_PERMISSION_QUERY);
    }
    public static boolean isCacheable(){
        return ConfigHelper.getBoolean(SecurityConstant.CACHEABLE);
    }
}
