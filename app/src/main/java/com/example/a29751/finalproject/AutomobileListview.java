package com.example.a29751.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AutomobileListview extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "AutomobileListview";
    public ArrayList<String> info;
    public AutoDatabaseHelper dbH;
    public SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_listview);

        final ListView lv = (ListView)findViewById(R.id.lv);

        final AutoAdapter messageAdapter = new AutoAdapter( this );
        lv.setAdapter (messageAdapter);

        Cursor cursor = db.rawQuery(" select * from " + dbH.TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(dbH.KEY_DATE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message );
            info.add(message);
            messageAdapter.notifyDataSetChanged();
            cursor.moveToNext();

        }



    }

    private class AutoAdapter extends ArrayAdapter<String> {

        public AutoAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return info.size();
        }

        public String getItem(int position) {
            return info.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = AutomobileListview.this.getLayoutInflater();

            View result = null;
            TextView message;
            result = inflater.inflate(R.layout.timelist, null);
            message = (TextView) result.findViewById(R.id.timeview);


            message.setText(getItem(position)); // get the string at position
            return result;

        }
    }

}
