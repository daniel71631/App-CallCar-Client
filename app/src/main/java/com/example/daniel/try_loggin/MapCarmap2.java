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
 * Created by daniel on 2017/9/17.
 */

public class MapCarmap2 extends AppCompatActivity {

    private Socket mSocket;
    private String DriverFares, DID, MapDriverName, MapDriverTime, MapDriverKind, MapDriverColor,  MapDriverBrand;
    public final static String TAG = MapCarmap2.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapcarmap2);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.once("map response", responseServer);
    }

    private Emitter.Listener responseServer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data=(JSONObject) args[0];
            try {
                DriverFares=data.getString("money");
                Log.d(TAG, DriverFares);
                DID=data.getString("did");
                MapDriverName=data.getString("name");
                MapDriverTime=data.getString("time");
                MapDriverKind=data.getString("kind");
                MapDriverColor=data.getString("color");
                MapDriverBrand=data.getString("brand");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Bundle toMapCaseFares=new Bundle();
            toMapCaseFares.putString("DID", DID);
            toMapCaseFares.putString("DriverName", MapDriverName);
            toMapCaseFares.putString("DriverTime",MapDriverTime);
            toMapCaseFares.putString("DriverKind", MapDriverKind);
            toMapCaseFares.putString("DriverColor", MapDriverColor);
            toMapCaseFares.putString("DriverBrand", MapDriverBrand);
            toMapCaseFares.putString("CaseFares", DriverFares);
            Intent intent = new Intent();
            intent.setClass(MapCarmap2.this, MapCaseFares.class);
            intent.putExtras(toMapCaseFares);
            startActivity(intent);
        }
    };

}
