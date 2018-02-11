package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 2017/8/31.
 */

public class pair extends AppCompatActivity {

    private Socket mSocket;
    private String caseStatus, DriverName, CarKind, CarColor, CarBrand, DriverDid, Drivertime;
    private Boolean Status;
    public final static String TAG = pair.class.getName();
    private Integer DriverTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pair);

        SocketApplication app = ( SocketApplication) getApplication();
        mSocket = app.getSocket();

        mSocket.on("autocall response", DriverResponse);

    }

    private Emitter.Listener DriverResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject DriverInformation = (JSONObject)args[0];
            try {
                Status=DriverInformation.getBoolean("status");
                caseStatus=Status.toString();
                Log.d(TAG, caseStatus);
                DriverName=DriverInformation.getString("name");
                CarKind=DriverInformation.getString("kind");
                CarColor=DriverInformation.getString("color");
                Log.d(TAG, CarColor);
                CarBrand=DriverInformation.getString("brand");
                DriverDid=DriverInformation.getString("did");
                DriverTime=DriverInformation.getInt("time");
                Drivertime=DriverTime.toString();
                Log.d(TAG, Drivertime);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(caseStatus.equals("true")){
               Bundle toquickcardriverinformation=new Bundle();
               toquickcardriverinformation.putString("DriverName", DriverName);
               toquickcardriverinformation.putString("CarKind", CarKind);
               toquickcardriverinformation.putString("CarColor", CarColor);
               toquickcardriverinformation.putString("CarBrand", CarBrand);
               toquickcardriverinformation.putString("DriverDid", DriverDid);
               toquickcardriverinformation.putInt("DirverTime", DriverTime);
                Intent intent = new Intent();
                intent.setClass(pair.this, quickcar_driver_information.class);
                intent.putExtras(toquickcardriverinformation);
                startActivity(intent);
            }else{

            }
        }
    };
}
