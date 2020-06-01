package com.xiaoshijie.simpleroute;

import android.app.Application;

import com.xiaoshijie.sqb.simpleroute.SimpleRoute;


/**
 * @author heshuai
 * created on: 2020/5/29 11:24 AM
 * description:
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleRoute.init(this);
    }
}
