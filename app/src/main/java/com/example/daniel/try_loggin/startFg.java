package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class startFg extends Fragment implements View.OnClickListener {


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.startfg, container, false);
    }

    private void initview(View view){
        Button mcarbtn=(Button) view.findViewById(R.id.btncar);
        Button mrecordbtn=(Button) view.findViewById(R.id.btnrecord);
        Button msetbtn=(Button) view.findViewById(R.id.btnset);
        Button mloggout=(Button)view.findViewById(R.id.btnloggout);
        mcarbtn.setOnClickListener(this);
        mrecordbtn.setOnClickListener(this);
        msetbtn.setOnClickListener(this);
        mloggout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.btncar:
                 CarFg Fragment=new CarFg();
                 getFragmentManager().beginTransaction().replace(R.id.frame,new CarFg()).addToBackStack(null).commit();
                 break;
             case R.id.btnrecord:
                 Intent intenttorecord = new Intent();
                 intenttorecord.setClass(getActivity(), CaseRecord.class);
                 startActivity(intenttorecord);
                 break;
             case R.id.btnset:
                 Intent intent = new Intent();
                 intent.setClass(getActivity(), settingprefernce.class);
                 startActivity(intent);
                 break;
             case R.id.btnloggout:
                 MainActivity.metxt1.setText("");
                 MainActivity.metxt2.setText("");
                 MainActivity.get.edit().clear().commit();
                 Intent logout = new Intent();
                 logout.setClass(getActivity(), MainActivity.class);
                 startActivity(logout);
                 break;
         }
    }
}
