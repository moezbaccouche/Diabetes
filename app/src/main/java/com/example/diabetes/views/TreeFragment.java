package com.example.diabetes.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diabetes.R;

import weka.gui.Main;


/**
 * A simple {@link Fragment} subclass.
 */
public class TreeFragment extends Fragment {

    public TextView mTextTree;
    public TreeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        mTextTree = (TextView) view.findViewById(R.id.textTree);

        mTextTree.setText(MainActivity.tree.toString());
        return view;
    }



}
