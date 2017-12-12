package com.example.haofa.androidlabs;

import android.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by haofa on 2017-12-11.
 */

public class MessageFragment extends Fragment{


    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, parentViewGroup, false);
    …
        return view;
    }
}
