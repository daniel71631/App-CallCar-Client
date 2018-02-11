package com.example.daniel.try_loggin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by daniel on 2017/7/18.
 */

public class CarFg extends Fragment implements View.OnClickListener {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.carfg, container, false);
    }

    private void initview(View view){
        Button mbtnreserve=(Button) view.findViewById(R.id.btnreservecar);
        Button mbtncar=(Button) view.findViewById(R.id.btnphonebook);
        Button mqcar=(Button)view.findViewById(R.id.btnqcar);
        Button mmapcar=(Button)view.findViewById(R.id.btnmapcar);
        mbtnreserve.setOnClickListener(this);
        mbtncar.setOnClickListener(this);
        mqcar.setOnClickListener(this);
        mmapcar.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnreservecar:
                /*reserveCarFg reserveFragment=new reserveCarFg();
                getFragmentManager().beginTransaction().replace(R.id.frame,new reserveCarFg()).addToBackStack(null).commit();*/
                Intent intent = new Intent();
                intent.setClass(getActivity(), Reservecar.class);
                startActivity(intent);
                break;
            case R.id.btnphonebook:
                Intent phonebook = new Intent();
                phonebook.setClass(getActivity(), PhonebookActivity.class);
                startActivity(phonebook);
                break;
            case R.id.btnqcar:
                Intent quickcar = new Intent();
                quickcar.setClass(getActivity(), checkquickcar.class);
                startActivity(quickcar);
                break;
            case R.id.btnmapcar:
                Intent mapcar = new Intent();
                mapcar.setClass(getActivity(), MapCar_ClientDestination.class);
                startActivity(mapcar);
                break;
        }
    }
}
