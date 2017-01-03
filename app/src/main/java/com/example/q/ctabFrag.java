package com.example.q.myapplication;


import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.provider.Settings.Secure;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ctabFrag extends Fragment {

    ListView listview;
    EditText editText;
    Button sendButton;
    static String username=null;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    ChatDataArrayAdapter adapter;
    ArrayList<ChatData> chatData=new ArrayList<ChatData>();
    View view;
    static String unique;
    boolean flag=true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            if(flag) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Write your name");
                final EditText input = new EditText(getContext());

                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        value.toString();
                        ctabFrag.username = value;
                    }
                });
                alert.create().show();
                flag=false;
            }
        }else{
            // fragment is no longer visible
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ctab, container, false);


        listview = (ListView) view.findViewById(R.id.listView);
        editText = (EditText) view.findViewById(R.id.editText);
        sendButton = (Button) view.findViewById(R.id.button);


        unique = Secure.getString(getContext().getContentResolver(),
                Secure.ANDROID_ID);
        adapter = new ChatDataArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chatData);
        listview.setAdapter(adapter);
        //alert.setMessage("Message");




        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username==null)
                    username="DefaultUserName";
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
                String time=sdf.format(cal.getTime());
                ChatData chatData = new ChatData(username, editText.getText().toString(), time, unique);  // 유저 이름과 메세지로 chatData 만들기
                databaseReference.child("message").push().setValue(chatData);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
                editText.setText("");
            }
        });

        databaseReference.child("message").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
                adapter.add(chatData);  // adapter에 추가합니다.
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        return view;

    }




}
