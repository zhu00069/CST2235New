package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.os.Bundle;


public class StartActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//java activity start with onCreate() function
        setContentView(R.layout.activity_start);//load XML activity_login layout, create all layout

    }
}
