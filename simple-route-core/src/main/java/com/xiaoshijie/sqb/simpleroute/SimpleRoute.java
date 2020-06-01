package com.xiaoshijie.sqb.simpleroute;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.xiaoshijie.sqb.route.compiler.utils.Consts;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author heshuai
 * created on: 2020/5/29 6:17 PM
 * description:
 */
public class SimpleRoute {

    private static Context mContext;
    private static SimpleRoute instance;

    private final static String TAG = "SimpleRoute";

    private static boolean isHaveInit = false;

    private final static String ROUTE_FILE_PATH = Consts.ROUTE_PACKAGE_PATH + "." + Consts.ROUTE_FILE_NAME;

    public static void init(Application application) {
        mContext = application;
        isHaveInit = true;
        try {
            ((IRouteMap) Class.forName(ROUTE_FILE_PATH).getConstructor().newInstance()).loadInfo(RouteHouse.routeMap);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static SimpleRoute getInstance() {
        if (null == instance) {
            synchronized (SimpleRoute.class) {
                if (null == instance) {
                    instance = new SimpleRoute();
                }
            }
        }
        return instance;
    }

    public void navigation(String path) {
        navigation(path, null);
    }

    public void navigation(String path, Bundle bundle) {
        if (!isHaveInit) {
            Log.e(TAG,"没有初始化路由");
            return;
        }

        Map<String, Class> routeMap = RouteHouse.routeMap;

        if (routeMap.isEmpty()) {
            Log.e(TAG,"没有路径相关注解");
            return;
        }

        Class<?> activityClass = routeMap.get(path);

        if (null == activityClass) {
            Log.e(TAG,"不存在--" + path + "--相关路由配置");
            return;
        }

        Intent intent = new Intent(mContext, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityCompat.startActivity(mContext, intent, bundle);
    }
}
