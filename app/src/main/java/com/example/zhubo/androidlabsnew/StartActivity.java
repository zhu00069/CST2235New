package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//java activity start with onCreate() function
        setContentView(R.layout.activity_start);//load XML activity_login layout, create all layout
        Log.i(ACTIVITY_NAME, "In onCreate()");//display sysout info

        //Define Start button as btn_start
        btn_start = (Button) findViewById(R.id.button_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Jump to ListItemsActivity.
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        if(requestCode == 50){
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
        }
        if(responseCode == Activity.RESULT_OK){
            Log.i(ACTIVITY_NAME, "Clicked checkbox and selected OK");
        }

        //Get message passed by ListItemActivity
        String messagePassed = data.getStringExtra("Response");
        Toast toast = Toast.makeText(StartActivity.this, messagePassed, Toast.LENGTH_LONG);
        toast.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
}
