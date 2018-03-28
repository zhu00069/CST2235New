package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private boolean isTablet = false;
    private Cursor c;

    //Get a writable database.
    private ChatDatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    //Get table name and column name.
    String tableName = dbHelper.TABLE_NAME;
    String keyID = dbHelper.KEY_ID;
    String keyMsg = dbHelper.KEY_MESSAGE;
    //in this case, “this” is the ChatWindow, which is-A Context object
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        listview = (ListView)findViewById(R.id.listview_chat);
        edittext = (EditText) findViewById(R.id.edittext_chat);
        btn_send = (Button) findViewById(R.id.button_send);//check if FrameLayout exists

        isTablet = (findViewById(R.id.tablet_framelayout) != null);
        messageAdapter = new ChatAdapter(this);

        listview.setAdapter(messageAdapter);

        String query = "SELECT * FROM " + tableName +";";
        Cursor c = db.rawQuery(query, null);

        //Read existed messages from database.
        c.moveToFirst();
        while(!c.isAfterLast()){

            String str = c.getString( c.getColumnIndex( dbHelper.KEY_MESSAGE) );
            Long id = c.getLong(c.getColumnIndex(dbHelper.KEY_ID));
            chat.add(str);

            //this restarts the process of getCount() & getView() to retrieve chat history
            messageAdapter.notifyDataSetChanged();

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + str );
            Log.i(ACTIVITY_NAME, "ID: " + id);
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
        //click a message will display the detailed message on another fragment layout.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String msg = messageAdapter.getItem(position);
                long ID = id;
                Long id_inChat = messageAdapter.getId(position);

                //a bundle is used to pass message
                MessageFragment myFragment = new MessageFragment();
                //pass data to fragment
                Bundle bundle = new Bundle();
                bundle.putString("Message", msg);
                bundle.putLong("ID", ID);
                bundle.putLong("IDInChat", id_inChat);

                if(isTablet){//for tablet
                    myFragment.setArguments(bundle);
                    //tell the MessageFragment this is a tablet
                    myFragment.setIsTablet(true);
                    //start a FragmentTransaction to add a fragment to the FrameLayout
                    getFragmentManager().beginTransaction().replace(R.id.tablet_framelayout,myFragment).commit();
                }else{//for phone
                    //tell the MessageFragment this is not a tablet
                    myFragment.setIsTablet(false);
                    //Jump to MessageDetails, pass the message and id information
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtra("ChatItem", bundle);
                    startActivityForResult(intent, 820, bundle);
                }
            }
        });
    }

    //For phone to delete a message
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 820 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            long id = extras.getLong("DeleteID");
            long id_inChat = extras.getLong("IDInChat");
            String query = "DELETE FROM " + tableName +" WHERE " + keyID + " = " + id + ";";
            db.execSQL(query);
            //for debug
            //Log.d("ChatWindow", chat.toString());
            chat.remove((int)id_inChat);
            //for debug
            //Log.d("ChatWindow", chat.toString());
            messageAdapter.notifyDataSetChanged();
        }
    }

    //for tablet to delete a message
    public void deleteForTablet(long idInDatabase, long idInChat){
        long id = idInDatabase;
        long id_inChat = idInChat;
        String query = "DELETE FROM " + tableName +" WHERE " + keyID + " = " + id + ";";
        db.execSQL(query);
        //for debug
        //Log.d("ChatWindow", chat.toString());
        chat.remove((int)id_inChat);
        //for debug
        //Log.d("ChatWindow", chat.toString());
        messageAdapter.notifyDataSetChanged();
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
        @Override
        public long getItemId(int position){
            //every time getItemId, refresh cursor c
            Log.d("ChatWindow", "getItemId" + position);
            String query = "SELECT * FROM " + tableName +";";
            c = db.rawQuery(query, null);
            //move cursor to the position clicked on listview
            c.moveToPosition(position);
            int id = c.getInt(c.getColumnIndex(keyID));
            return id;
        }
}
}