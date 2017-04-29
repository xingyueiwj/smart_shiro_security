package org.smart4j.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.smart4j.framework.aop.AspectProxy;

import org.smart4j.framework.mvc.fault.AuthzException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/4/27 0027.
 */
@Aspect(Controller.class)
public class AuthzAnnotationAspect extends AspectProxy {
    /**
     * 定义一个基于授权功能的注解类数组
     */
    private static final Class[] ANNOTATION_CLASS_ARRAY = {
      User.class
    };
    @Override
    public void before(Class<?>cls,Method method,Object[]params)throws Throwable{
        //从目标类与目标方法中获取相应的注解
        Annotation annotation = getAnnocation(cls,method);
        if (annotation!=null){
            //判断授权注解的类型
            Class<?>annotationType = annotation.annotationType();
            if (annotationType.equals(User.class)){
                handlerUser();
            }
        }
    }
    private Annotation getAnnocation(Class<?>cls,Method method){
        //遍历所有的授权注解
        for(Class<? extends Annotation> annotationClass:ANNOTATION_CLASS_ARRAY){
            //首先判断目标方法上是否带有授权注解
            if (method.isAnnotationPresent(annotationClass)){
                return method.getAnnotation(annotationClass);
            }
            //然后判断目标类上是否带有授权注解
            if (cls.isAnnotationPresent(annotationClass)){
                return cls.getAnnotation(annotationClass);
            }
        }
        return null;
    }
    private void handlerUser(){
        Subject currentUser = SecurityUtils.getSubject();
        PrincipalCollection principals = currentUser.getPrincipals();
        if (principals==null&&principals.isEmpty()){
            throw new AuthzException("当前用户尚未登录");
        }
    }
}
