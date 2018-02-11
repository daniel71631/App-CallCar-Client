package com.example.daniel.try_loggin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by daniel on 2017/8/3.
 */

public class applogo extends AppCompatActivity {

    private TextView mtxttitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applogo);

        mtxttitle=(TextView)findViewById(R.id.txttitle);
        mtxttitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoescb.ttf"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent().setClass(applogo.this, MainActivity.class));
            }
        }, 3000);

    }
}
