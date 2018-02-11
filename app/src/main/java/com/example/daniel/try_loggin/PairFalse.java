package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by daniel on 2017/9/12.
 */

public class PairFalse extends AppCompatActivity {

    private Button mbtnStarfg, mbtnquickcar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pairfalse);

        mbtnquickcar=(Button)findViewById(R.id.btnstarfg);
        mbtnquickcar=(Button)findViewById(R.id.btnquickcar);
        mbtnStarfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PairFalse.this,NavigationMainActivity.class));
            }
        });

        mbtnquickcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PairFalse.this,tryMapsActivity.class));
            }
        });
    }
}
