package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    private ListView listview;
    private EditText edittext;
    private Button btn_send;
    private final ArrayList<String> chat = new ArrayList<>();
    //add an ArrayList<String> variable to store your chat messages. Add

    private String ACTIVITY_NAME = "ChatWindow";

    //Get a writable database.
    private ChatDatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    //Get table name and column name.
    String tableName = dbHelper.TABLE_NAME;
    String keyMsg = dbHelper.KEY_MESSAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        listview = (ListView)findViewById(R.id.listview_chat);
        edittext = (EditText) findViewById(R.id.edittext_chat);
        btn_send = (Button) findViewById(R.id.button_send);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listview.setAdapter(messageAdapter);

        String query = "SELECT * FROM " + tableName +";";
        Cursor c = db.rawQuery(query, null);

        //Read existed messages from database.
        c.moveToFirst();
        while(!c.isAfterLast()){

            String str = c.getString( c.getColumnIndex( dbHelper.KEY_MESSAGE) );
            chat.add(str);

            //this restarts the process of getCount() & getView() to retrieve chat history
            messageAdapter.notifyDataSetChanged();

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + str );
            c.moveToNext();
        }

        //Print colomn name.
        Log.i(ACTIVITY_NAME, "Cursor’s  column count = " + c.getColumnCount());
        for(int i = 0; c.getColumnCount() > i; i++){
            Log.i(ACTIVITY_NAME, "Coloumn " + i + " : " + c.getColumnName(i));
        }

        btn_send.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view) {

                    //New messages.
                    String msg = edittext.getText().toString();
                    chat.add(msg);

                    //this restarts the process of getCount() & getView()
                    messageAdapter.notifyDataSetChanged();

                    //when the user clicks “Send”, clear the textView
                    edittext.setText("");

                    //Write messages to database.
                    ContentValues cv = new ContentValues();
                    cv.put(keyMsg, msg);
                    db.insert(tableName,null,cv);

                    //another way to write to datasbse using query.
//                String query = "INSERT INTO " + tableName + " (" + keyMsg + ") VALUES ('" + msg + "')";
//                db.execSQL(query);

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
        public View getView(int position, View convertView, ViewGroup parent){
            View result = null;
            TextView message;

            if(position%2 == 0){
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                message = (TextView)result.findViewById(R.id.message_incoming);
            }else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                message = (TextView)result.findViewById(R.id.message_outgoing);
            }

            message.setText(getItem(position)); // get the string at position
            return result;
        }


    public int getCount(){
        return chat.size();// return ArrayList-chat size
    }
    public String getItem(int position){
        return chat.get(position);
    }

    public long getId(int position){
        return position;
    }
}
}