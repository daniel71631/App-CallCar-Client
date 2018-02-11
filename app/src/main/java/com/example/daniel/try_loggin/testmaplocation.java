package com.example.daniel.try_loggin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by daniel on 2017/8/19.
 */

public class testmaplocation extends AppCompatActivity {

    private TextView mtxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testmaplocation);

        mtxt=(TextView)findViewById(R.id.txtlo) ;

        Bundle bundle=this.getIntent().getExtras();
        final String location=bundle.getString("location");
        mtxt.setText(location);

    }
}
