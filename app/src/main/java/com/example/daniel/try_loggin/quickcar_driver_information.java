package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 2017/8/31.
 */

public class quickcar_driver_information extends AppCompatActivity {

    private TextView mDriverName, mCarKind, mCarColor, mCarBrand, mDriverTime;
    private Socket mSocket;
    private String Did;
    public final static String TAG = quickcar_driver_information.class.getName();
    //private JSONObject confirmfalse, confirmtrue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quickcar_driver_information);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        Bundle frompair=this.getIntent().getExtras();
        String DriverName=frompair.getString("DriverName");
        String CarKind=frompair.getString("CarKind");
        String CarColor=frompair.getString("CarColor");
        String CarBrand=frompair.getString("CarBrand");
        Did=frompair.getString("DriverDid");
        //String Time=frompair.getString("DriverTime");
        Integer Time=frompair.getInt("DirverTime")/60;
        Log.d(TAG, Time.toString());
        mDriverName=(TextView)findViewById(R.id.txtdrivername);
        mDriverName.setText(DriverName);
        mCarKind=(TextView)findViewById(R.id.txtcarstyle);
        mCarKind.setText(CarKind);
        mCarColor=(TextView)findViewById(R.id.txtcarColor);
        mCarColor.setText(CarColor);
        mCarBrand=(TextView)findViewById(R.id.txtcarbrand);
        mCarBrand.setText(CarBrand);
        mDriverTime=(TextView)findViewById(R.id.txtDriverTime);
        mDriverTime.setText(Time.toString()+"分鐘");

        mSocket.once("driver arrive", DriverResponse);
        Log.d(TAG, "Success");

    }



    private Emitter.Listener DriverResponse = new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable(){
                        public void run(){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(quickcar_driver_information.this);
                            dialog.setTitle("司機到達");
                            dialog.setMessage("請盡速上車");
                            dialog.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final JSONObject confirmtrue=new JSONObject();
                                    try {
                                        confirmtrue.put("confirm", true);
                                        confirmtrue.put("did", Did);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mSocket.emit("arrive confirm", confirmtrue);
                                    startActivity(new Intent(quickcar_driver_information.this,GoogleMapTrace.class));
                                }
                            });
                            dialog.setNegativeButton("沒看到", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final JSONObject confirmfalse=new JSONObject();
                                    try {
                                        confirmfalse.put("confirm", false);
                                        confirmfalse.put("did", Did);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mSocket.once("driver arrive", DriverResponse);
                                    mSocket.emit("arrive confirm", confirmfalse) ;
                                    return;
                                }
                            });
                            dialog.show();
                        }
                    });
        }
    };
}
