package com.example.haofa.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractCollection;
import java.util.ArrayList;

import static com.example.haofa.androidlabs.ChatDatabaseHelper.KEY_ID;
import static com.example.haofa.androidlabs.ChatDatabaseHelper.TABLE_NAME;
import static com.example.haofa.androidlabs.StartActivity.ACTIVITY_NAME;

public class ChatWindow extends Activity {

    protected  static final String ACTIVITY_NAME = "ChatWindow";


    private ArrayList<String> chat = new ArrayList<>();
    private Cursor cursor;
    private boolean isFrameExist;
    private MessageFragment newFragment;
    private FragmentTransaction ftr;
    private ChatDatabaseHelper dbH;
    private SQLiteDatabase db;
    private final int RQCODE = 10;

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

        isFrameExist = (FrameLayout)findViewById(R.id.frame) != null;


        //craete database
        dbH = new ChatDatabaseHelper(this);
        db = dbH.getWritableDatabase();

        cursor = db.rawQuery(" select * from " + dbH.TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(dbH.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message );
            chat.add(message);
            messageAdapter.notifyDataSetChanged();
            cursor.moveToNext();

        }


        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());

        for(int i=0; i<cursor.getColumnCount();i++) {
           System.out.print(cursor.getColumnName(i));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Bundle args = new Bundle();
                String message = messageAdapter.getItem(position);
                args.putString("message", message);
                args.putLong("ID", id);
                args.putBoolean("Boolean", isFrameExist);
                if (isFrameExist) {
                    newFragment = new MessageFragment();
                    newFragment.setArguments(args);

                    ftr = getFragmentManager().beginTransaction();
                    ftr.replace(R.id.frame, newFragment);
                    ftr.addToBackStack(null);
                    ftr.commit();

                }else {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(args);
                    startActivityForResult(intent, RQCODE, args);

                }
            }
        });




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

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {

        if(requsetCode == 10) {
            Log.i(ACTIVITY_NAME,"Return to chatWindow.onActivityResult");
        }
        if(resultCode == ChatWindow.RESULT_OK) {

            long passID = data.getLongExtra("Delete", -1);

            db.delete(dbH.TABLE_NAME,dbH.KEY_ID + "=" + passID, null);
            finish();
            Intent intent = getIntent();
            startActivity(intent);


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbH.close();
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

        cursor.moveToPosition(position);

        return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
    }
}
