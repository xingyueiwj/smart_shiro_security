package org.smart4j.security;

import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.*;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/23 0023.
 */
public class SmartSecurityPlugin implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    public void onStartup(Set<Class<?>>handlesTypes,ServletContext servletContext)throws ServletException{
        //设置初始化参数
        servletContext.setInitParameter("shiroConfigLocations","class-path:smart-security.ini");
        //注册Listener
        servletContext.addListener(EnvironmentLoaderListener.class);
        //注册Filter
        FilterRegistration.Dynamic smartSecurityFilter = servletContext.addFilter("SmartSecurityFilter",SmartSecurityFilter.class);
        smartSecurityFilter.addMappingForUrlPatterns(null,false,"/*");
    }
}
