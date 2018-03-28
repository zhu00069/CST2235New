package com.example.zhubo.androidlabsnew;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    //used to judge if it's a tablet or phone
    public boolean isTablet;

    TextView txt_message;
    TextView txt_id;
    Button btn_delete;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_message_fragment, container, false);

        txt_message = view.findViewById(R.id.Message_fragment);
        txt_id = view.findViewById(R.id.id_fragment);
        btn_delete = view.findViewById(R.id.delete);

        //Get message passed by tablet(chatwindow) or phone(messagedetails)
        bundle = getArguments();

        String message = bundle.getString("Message");
        final long id = bundle.getLong("ID");
        final long id_inChat = bundle.getLong("IDInChat");

        txt_message.setText(message);
        txt_id.setText(String.valueOf(id));

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTablet) {
                    ChatWindow cw = (ChatWindow)getActivity();
                    cw.deleteForTablet(id, id_inChat);
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("DeleteID", id);
                    resultIntent.putExtra("IDInChat", id_inChat);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setIsTablet(boolean b){
        isTablet = b;
    }
}