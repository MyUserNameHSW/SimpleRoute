package com.xiaoshijie.sqb.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author heshuai
 * created on: 2020/5/29 11:46 AM
 * description: 路由地址注解
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Route {

    String path();

}
