package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by daniel on 2017/9/17.
 */

public class MapCar_ClientDestination extends AppCompatActivity {

    private EditText mClientDestination;
    public static String MapCarClientDestiantion;
    private Button mbtnDestiantionCheck;
    public final static String TAG = MapCar_ClientDestination.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapcar_clientdestination);

        mClientDestination=(EditText)findViewById(R.id.etxtClientDestination);

        mbtnDestiantionCheck=(Button)findViewById(R.id.btnDestinationCheck);
        mbtnDestiantionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapCarClientDestiantion=mClientDestination.getText().toString();
                if(MapCarClientDestiantion.equals("")){
                    Log.d(TAG, "insert false");
                }
                Log.d(TAG, MapCarClientDestiantion);
                startActivity(new Intent(MapCar_ClientDestination.this,MapCarmap.class));
            }
        });

    }
}
