package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

protected  static final String ACTIVITY_NAME = "LoginActivity";
private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //java activity start with onCreate() function
        setContentView(R.layout.activity_login); //load XML activity_login layout, create all layout
        Log.i(ACTIVITY_NAME,"In onCreate()");

        //Define Login button as btn
        btn_login = (Button)findViewById(R.id.Loginbutton);//return reference to the object you want, return all View objects, need downcase them

        //Retrieved from: https://developer.android.com/training/data-storage/shared-preferences.html#java
        //Create a SharedPreferences used to read and write e-mail address
                                                                           //file.key
        final SharedPreferences prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        //Read from file prefs to get e-mail address saved before, if not, get the default email@domain.com
        //Display the e-mail address in EditText

        String defaultAddress = prefs.getString(getString(R.string.login_name), "email@domain.com");
        EditText email = (EditText) findViewById(R.id.edit_email_address);
        email.setText(defaultAddress);

        //Get the text in EditText as e-mail address to be saved
        final Editable emailAddr = email.getText();

        //Set the Login button action.
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save the typed e-mail in prefs
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString(getString(R.string.login_name), emailAddr.toString());
                edit.commit();

                //Jump to StartActivity.
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);

            }
        });

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
