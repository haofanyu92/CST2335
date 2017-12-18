package com.example.haofa.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by haofa on 2017-12-11.
 */

public class MessageFragment extends Fragment{

    private ChatDatabaseHelper dbh;
    private SQLiteDatabase db;



    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        /*View view = inflater.inflate(R.layout.delete_message, parentViewGroup, false);*/

        Bundle info = getArguments();
        String message = info.getString("message");
        final long id = info.getLong("ID");
        final boolean isTable = info.getBoolean("Boolean");
        TextView messageT = (TextView)view.findViewById(R.id.textMessage);
        TextView idT = (TextView)view.findViewById(R.id.textID);

        dbh = new ChatDatabaseHelper(getActivity());
        db = dbh.getWritableDatabase();

        messageT.setText(messageT.getText().toString() + " = " + message);
        idT.setText(idT.getText().toString() + " = " + id);

        Button deleteBT = (Button)view.findViewById(R.id.deleteBt);
        deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTable){
                    db.delete(dbh.TABLE_NAME, dbh.KEY_ID + " = " + id, null);
                    getActivity().finish();
                    Intent intent = getActivity().getIntent();
                    startActivity(intent);
                }else {

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Delete", id);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();

                }
            }
        });


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_message, parentViewGroup, false);
        return view;
    }
}
