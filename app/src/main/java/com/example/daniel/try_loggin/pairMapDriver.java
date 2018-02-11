package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

/**
 * Created by daniel on 2017/9/17.
 */

public class pairMapDriver extends AppCompatActivity {

    private Socket mSocket;
    private String DriverName,DriverSex,DriverAgeString, CarKind, CarColor, CarBrand, CarAgeString,DriverDid, CarDistanceString,CarDistanceKmString;
    public final static String TAG = pair.class.getName();
    private Integer DriverAge, CarAge, ListId, CarDistance;
    private SQLiteMapDriver dbHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pairmapdriver);

        dbHelper = new SQLiteMapDriver(this);

        SocketApplication app = ( SocketApplication) getApplication();
        mSocket = app.getSocket();

        mSocket.on("map list response", DriverMapResponse);
    }

    private Emitter.Listener DriverMapResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONArray MapDriverInformationarray=(JSONArray)args[0];
            for(ListId=0; ListId<MapDriverInformationarray.length(); ListId++){
                try {
                    DriverName=MapDriverInformationarray.getJSONObject(ListId).getString("name");
                    DriverSex=MapDriverInformationarray.getJSONObject(ListId).getString("sex");
                    DriverAgeString=MapDriverInformationarray.getJSONObject(ListId).getString("birth");
                    CarKind=MapDriverInformationarray.getJSONObject(ListId).getString("kind");
                    CarColor=MapDriverInformationarray.getJSONObject(ListId).getString("color");
                    CarBrand=MapDriverInformationarray.getJSONObject(ListId).getString("brand");
                    CarAgeString=MapDriverInformationarray.getJSONObject(ListId).getString("carbirth");
                    DriverDid=MapDriverInformationarray.getJSONObject(ListId).getString("did");
                    CarDistanceString=MapDriverInformationarray.getJSONObject(ListId).getString("distance");
                    CarDistance=Integer.parseInt(CarDistanceString)/1000;
                    CarDistanceKmString=CarDistance+"公里";
                    //ListId=i;
                    Calendar countAge = Calendar.getInstance();
                    int thisYear = countAge.get(Calendar.YEAR);
                    DriverAge=thisYear-Integer.parseInt(DriverAgeString);
                    CarAge=thisYear-Integer.parseInt(CarAgeString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String sql = "insert into tb_mapdriver(Did , DriverName, DriverAge, DriverSex, CarKind, CarAge, CarBrand, CarColor, CarDistance, listid)values(?,?,?,?,?,?,?,?,?,?)";
                boolean flag = dbHelper.execData(sql, new Object[] { DriverDid,DriverName, DriverAge, DriverSex, CarKind, CarAge, CarBrand, CarColor, CarDistanceKmString, ListId});
                if(flag){
                    Log.d(TAG, "Success");
                }else{
                    Log.d(TAG, "False");
                }
            }
            startActivity(new Intent(pairMapDriver.this,MapDriverList.class));
        }
    };
}
