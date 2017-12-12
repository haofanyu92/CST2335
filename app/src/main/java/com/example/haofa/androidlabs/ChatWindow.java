package com.example.haofa.androidlabs;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractCollection;
import java.util.ArrayList;

import static com.example.haofa.androidlabs.ChatDatabaseHelper.TABLE_NAME;
import static com.example.haofa.androidlabs.StartActivity.ACTIVITY_NAME;

public class ChatWindow extends Activity {

    protected  static final String ACTIVITY_NAME = "ChatWindow";


    private ArrayList<String> chat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FrameLayout fl = (FrameLayout) findViewById(R.id.frame);
        if (fl == null) {
            setContentView(R.layout.activity_chat_window);
        }else {
            Intent intent = new Intent(ChatWindow.this, MessageFragment.class);
        }



        final EditText et = (EditText)findViewById(R.id.edit);
        final Button bt = (Button)findViewById(R.id.button4);
        final ListView lv = (ListView)findViewById(R.id.lv);

        final ChatAdapter messageAdapter = new ChatAdapter( this );
        lv.setAdapter (messageAdapter);


        //craete database
        final ChatDatabaseHelper dbH = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = dbH.getWritableDatabase();

        Cursor cursor = db.rawQuery(" select * from " + dbH.TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(dbH.KEY_MESSAGE));
            Double cursorID = cursor.getDouble(cursor.getColumnIndex(dbH.KEY_ID));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message );
            chat.add(message);=
            chat.add(cursorID);
            messageAdapter.notifyDataSetChanged();
            cursor.moveToNext();

        }
        cursor.moveToPosition();

        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());

        for(int i=0; i<cursor.getColumnCount();i++) {
           System.out.print(cursor.getColumnName(i));
        }




        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sendM = et.getText().toString();

                chat.add(sendM);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()


                ContentValues cv = new ContentValues();
                cv.put(dbH.KEY_MESSAGE, sendM);
                db.insert(dbH.TABLE_NAME, null , cv);

                et.setText("");
            }
        });
    }


    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chat.size();
        }

        public String getItem(int position) {
            return chat.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
            TextView message;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                message = (TextView) result.findViewById(R.id.intext);

            }
            else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                message = (TextView) result.findViewById(R.id.outtext);
            }


            message.setText(getItem(position)); // get the string at position
            return result;

        }
    }
    public long getItemId(int position) {

    }
}
