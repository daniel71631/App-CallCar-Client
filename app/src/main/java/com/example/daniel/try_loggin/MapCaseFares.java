package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 2017/9/18.
 */

public class MapCaseFares extends AppCompatActivity {

    private TextView mCaseFares;
    private Button mFaresYes, mFaresNo;
    private Socket mSocket;
    private String DID, DriverFares, MapDriverName, MapDriverTime,MapDriverKind,MapDriverColor, MapDriverBrand, MapCaseFares;
    public final static String TAG = MapCaseFares.class.getName();
    private SQLiteMapDriver dbHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapcarefares);

        dbHelper = new SQLiteMapDriver(this);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        Bundle fromMapCarmap2=this.getIntent().getExtras();
        DID=fromMapCarmap2.getString("DID");
        MapDriverName=fromMapCarmap2.getString("DriverName");
        MapDriverTime=fromMapCarmap2.getString("DriverTime");
        MapDriverKind=fromMapCarmap2.getString("DriverKind");
        MapDriverColor=fromMapCarmap2.getString("DriverColor");
        MapDriverBrand=fromMapCarmap2.getString("DriverBrand");
        MapCaseFares=fromMapCarmap2.getString("CaseFares");

        mCaseFares=(TextView)findViewById(R.id.txtMapCarCaseFares);
        mCaseFares.setText(MapCaseFares);

        mFaresYes=(Button)findViewById(R.id.btnFaresYes);
        mFaresYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject confirmyes=new JSONObject();
                try {
                    confirmyes.put("did", DID);
                    confirmyes.put("agree",true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dbHelper.deleteAll();
                mSocket.emit("map confirm", confirmyes);
                Bundle tomapcar_driver_information=new Bundle();
                tomapcar_driver_information.putString("DriverName", MapDriverName);
                tomapcar_driver_information.putString("DriverColor", MapDriverColor);
                tomapcar_driver_information.putString("DriverKind",MapDriverKind);
                tomapcar_driver_information.putString("DriverBrand",MapDriverBrand );
                tomapcar_driver_information.putString("DriverFares", MapCaseFares);
                tomapcar_driver_information.putString("DriverTime", MapDriverTime);
                tomapcar_driver_information.putString("Did", DID);
                Intent intent = new Intent();
                intent.setClass(MapCaseFares.this, mapcar_driver_information.class);
                intent.putExtras(tomapcar_driver_information);
                startActivity(intent);

            }
        });

        mFaresNo=(Button)findViewById(R.id.btnFaresNo);
        mFaresNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject confirmno=new JSONObject();
                try {
                    confirmno.put("did", DID);
                    confirmno.put("agree",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("map confirm", confirmno);
                startActivity(new Intent(MapCaseFares.this,MapDriverList.class));
            }
        });
    }
}
