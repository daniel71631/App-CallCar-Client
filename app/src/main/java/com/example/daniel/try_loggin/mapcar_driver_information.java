package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daniel on 2017/9/19.
 */

public class mapcar_driver_information extends AppCompatActivity {

    private TextView mtxtMapCarImName, mtxtMapCarIMCarKind, mtxtMapCarIMCarBrand, mtxtMapCarIMCarColor, mtxtDriverTime, mtxtFares;
    private Socket mSocket;
    private String Did;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapcar_driver_information);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        Bundle fromMapCareFares=this.getIntent().getExtras();
        Did=fromMapCareFares.getString("Did");
        String DriverName=fromMapCareFares.getString("DriverName");
        String CarStyle=fromMapCareFares.getString("DriverKind");
        String CarColor=fromMapCareFares.getString("DriverColor");
        String CarBrand=fromMapCareFares.getString("DriverBrand");
        String CaseFares=fromMapCareFares.getString("DriverFares");
        String CarTime=fromMapCareFares.getString("DriverTime");
        Integer Time=Integer.parseInt(CarTime)/60;

        mtxtMapCarImName=(TextView)findViewById(R.id.txtMapCarImName);
        mtxtMapCarIMCarKind=(TextView)findViewById(R.id.txtMapCarIMCarKind);
        mtxtMapCarIMCarBrand=(TextView)findViewById(R.id.txtMapCarIMCarBrand);
        mtxtMapCarIMCarColor=(TextView)findViewById(R.id.txtMapCarIMCarColor);
        mtxtDriverTime=(TextView)findViewById(R.id.txtMapCarIMTime);
        mtxtFares=(TextView)findViewById(R.id.txtMapCarIMFares);

        mtxtMapCarImName.setText(DriverName);
        mtxtMapCarIMCarKind.setText(CarStyle);
        mtxtMapCarIMCarBrand.setText(CarBrand);
        mtxtMapCarIMCarColor.setText(CarColor);
        mtxtFares.setText(CaseFares);
        mtxtDriverTime.setText(Time+"分鐘");

        mSocket.once("driver arrive", DriverResponse);

    }

    private Emitter.Listener DriverResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable(){
                public void run(){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mapcar_driver_information.this);
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
                            startActivity(new Intent(mapcar_driver_information.this,GoogleMapTrace.class));
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
