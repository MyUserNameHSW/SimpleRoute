package com.xiaoshijie.simpleroute;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaoshijie.sqb.route.annotation.Route;
import com.xiaoshijie.sqb.simpleroute.SimpleRoute;

/**
 * @author heshuai
 * created on: 2020/6/1 2:49 PM
 * description:
 */
@Route(path = "xsj://second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRoute.getInstance().navigation("xsj://main");
            }
        });
    }
}
