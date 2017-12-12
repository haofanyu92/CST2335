package com.example.haofa.androidlabs;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by haofa on 2017-12-11.
 */

public class MessageFragment extends Fragment{


    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delete_message, parentViewGroup, false);
        return view;
    }
}
