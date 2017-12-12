package com.example.haofa.androidlabs;

import android.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by haofa on 2017-12-11.
 */

public class MessageFragment extends Fragment{


   public void onCreateView(){
        RelativeLayout item = (RelativeLayout) item.findViewById();
        View child = getLayoutInflater().inflate(R.layout.child, null);
        item.addView(child);
    }
}
