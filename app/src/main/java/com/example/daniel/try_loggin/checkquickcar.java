package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by daniel on 2017/8/19.
 */

public class checkquickcar extends AppCompatActivity  {

    private Button myes, mno;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkquickcar);

        myes=(Button)findViewById(R.id.btnmapyes);
        mno=(Button)findViewById(R.id.btnmapno);

        myes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(checkquickcar.this,tryMapsActivity.class));
            }
        });

        mno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回到CarFg
                finish();
            }
        });
    }
}
