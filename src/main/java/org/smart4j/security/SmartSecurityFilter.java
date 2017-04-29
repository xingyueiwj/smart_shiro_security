package org.smart4j.security;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.CachingSecurityManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/23 0023.
 */
public class SmartSecurityFilter extends ShiroFilter{

    @Override
    public void init() throws Exception {
        super.init();
        WebSecurityManager webSecurityManager = super.getSecurityManager();
        //设置realm,可同时支持多个Ream,并按照先后顺序用逗号分隔
        setRealms(webSecurityManager);
        setCache(webSecurityManager);
    }
    private void setRealms(WebSecurityManager webSecurityManager){
        //读取smart.plugin.security.realms配置项
        String securityRealms = SecurityConfig.getRealms();
        if (securityRealms!=null){
            //根据逗号进行划分
            String[] securityRealmArray = securityRealms.split(",");
            if (securityRealmArray.length>0){
                //使Realms具备唯一性与顺序性
                Set<Realm> realms = new LinkedHashSet<Realm>();
                for (String securityRealm:securityRealmArray){
                    if (securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_JDBC)){
                        //添加基于JDBC的Realm,需配置相关sql查询语句
                        addJdbcRealm(realms);
                    }else if(securityRealm.equalsIgnoreCase(SecurityConstant.REALMS_CUSTOM)){
                        //添加定制化的Realm,需实现SmartSecurity接口
                        addCustomRealm(realms);
                    }
                }
                RealmSecurityManager realmSecurityManager = (RealmSecurityManager)webSecurityManager;
                realmSecurityManager.setRealms(realms);
            }
        }
    }
    private void addJdbcRealm(Set<Realm> realmes){
        //添加自己实现的基于jdbc的Ream
        SmartJdbcRealm smartJdbcRealm = new SmartJdbcRealm();
        realmes.add(smartJdbcRealm);
    }
    private void addCustomRealm(Set<Realm> realms){
        //读取配置项
        SmartSecurity smartSecurity = SecurityConfig.getSmartSecurity();
        //添加自己实现的Realm
        SmartCustomRealm smartCustomRealm = new SmartCustomRealm(smartSecurity);
        realms.add(smartCustomRealm);
    }
    private void setCache(WebSecurityManager webSecurityManager){
        //读取配置项
        if(SecurityConfig.isCacheable()){
            CachingSecurityManager cachingSecurityManager = (CachingSecurityManager)webSecurityManager;
            //使用基于内存的CacheManager
            CacheManager cacheManager = new MemoryConstrainedCacheManager();
            cachingSecurityManager.setCacheManager(cacheManager);
        }
    }
}
