package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by daniel on 2017/7/24.
 */

public class Phonecar extends Fragment implements View.OnClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phonecar, container, false);
    }

    private void initview(View view){
        Button mcarbtn=(Button) view.findViewById(R.id.btnmapyes);
        Button mcarstorebtn=(Button) view.findViewById(R.id.btnmapno);
        mcarbtn.setOnClickListener(this);
        mcarstorebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnmapyes:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PhonebookActivity.class);
                startActivity(intent);
                break;
            case R.id.btnmapno:
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), carstoreActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
