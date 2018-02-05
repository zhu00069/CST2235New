package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class ListItemsActivity extends Activity {

    protected  static final String ACTIVITY_NAME = "ListItemActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton btn_listitem;
    private Switch switch_listitem;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        //Define ListItem ImageButton as btn_listitem
        btn_listitem = (ImageButton) findViewById(R.id.imagebutton);
        btn_listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        switch_listitem = (Switch) findViewById(R.id.swit);
        switch_listitem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                CharSequence text;
                int duration;

                if(isChecked){
                    text = "Switch is On";
                    duration = Toast.LENGTH_SHORT;
                }else{
                    text = "Switch is Off";
                    duration = Toast.LENGTH_LONG;
                }
                Toast toast = Toast.makeText(ListItemsActivity.this, text, duration);
                toast.show();
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                AlertDialog.Builder builder1 = builder;
                builder1.setMessage(R.string.dialog_message);
                builder1.setTitle(R.string.dialog_title);
                builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button to end the activity then go back to the StartActivity()
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        checkBox.setChecked(false);
                    }
                });
                final AlertDialog response = builder1.show();

            }
        });
    }

    //Take a photo with a camera app. [Webpage]
    //Retrieved from: https://developer.android.com/training/camera/photobasics.html#TaskCaptureIntent
    //A function that invokes an intent to capture a photo.
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            btn_listitem.setImageBitmap(imageBitmap);
        }

    }
    public void onResume(){
        super.onResume();

    }

    public void onStart() {
        super.onStart();


    }

    public void onPause(){
        super.onPause();


    }

    public void onStop(){
        super.onStop();


    }

    public void onDestroy(){
        super.onDestroy();


    }
}
