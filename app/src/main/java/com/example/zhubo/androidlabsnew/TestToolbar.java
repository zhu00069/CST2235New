package com.example.zhubo.androidlabsnew;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String newMsg = "You select item 1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final AlertDialog.Builder builder;

        switch(id){
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                //create a snackbar
                android.support.v7.widget.Toolbar toolbarlayout = findViewById(R.id.toolbar);
                Snackbar.make(toolbarlayout, newMsg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.action_two:
                Log.d("Toolbar", "Option 2 selected");
                builder = new AlertDialog.Builder(TestToolbar.this);
                builder.setTitle(R.string.menu_item2);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                break;

            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");

                builder = new AlertDialog.Builder(TestToolbar.this);
                // Get the layout inflater
                LayoutInflater inflater = TestToolbar.this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                View view = inflater.inflate(R.layout.custom_dialog_layout, null);
                final EditText txt_menuItem3 = (EditText)view.findViewById(R.id.newMsg);
                AlertDialog.Builder builder1 = builder.setView(view);

                // Add action buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // type in new message
                        newMsg = txt_menuItem3.getText().toString();
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .create().show();


                break;

            case R.id.about:
                Toast toast = Toast.makeText(TestToolbar.this, "Version 1.0, by Bo", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
