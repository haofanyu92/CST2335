package com.example.haofa.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Bundle info = new Bundle();

        MessageFragment newFr = new MessageFragment();
        newFr.setArguments(info);
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        ft.add(R.id.message_detail, newFr);
        ft.addToBackStack(null);
        ft.commit();
    }
}
