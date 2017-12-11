package com.example.haofa.androidlabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by haofa on 2017-12-11.
 */

public class MessageFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onCreateView(){
        RelativeLayout item = (RelativeLayout) item.findViewById();
        View child = getLayoutInflater().inflate(R.layout.child, null);
        item.addView(child);
    }
}
