package com.xiaoshijie.simpleroute;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaoshijie.sqb.route.annotation.Route;
import com.xiaoshijie.sqb.simpleroute.SimpleRoute;

@Route(path = "xsj://main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleRoute.getInstance().navigation("xsj://second");
            }
        });
    }
}
