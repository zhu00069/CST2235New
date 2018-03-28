package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle bundle = getIntent().getBundleExtra("ChatItem");

        //start a FragmentTransaction to add a fragment to the FrameLayout
        MessageFragment myFragment = new MessageFragment();
        myFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.msgDetail_framelayout, myFragment).commit();

    }
}
