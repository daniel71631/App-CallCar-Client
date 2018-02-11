package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2017/9/17.
 */

public class MapDriverDetail extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView mDriverName, mDriverSex, mDriverAge, mCarKind, mCarAge, mCarBrand, mCarColor, mCaseFares;
    private SQLiteMapDriver dbHelper = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    public final static String TAG = MapDriverDetail.class.getName();
    private Button mYes, mNo;
    private String Did,  MapCarClientDestination, la,lng;
    private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION=100;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationMgr;
    private double latitude=0.0;
    private double longitude =0.0;
    private Socket mSocket;
    private JSONObject client_location_driverchoose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdriverdetail);

        mGoogleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationMgr=(LocationManager)getSystemService(LOCATION_SERVICE);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        MapCarClientDestination=MapCar_ClientDestination.MapCarClientDestiantion;

        dbHelper = new SQLiteMapDriver(this);
        totalList = getcontent();

        Bundle fromMapDriverList=this.getIntent().getExtras();
        String MapDriverIDstring =fromMapDriverList.getString("MapDriverListId");
        final Integer MapDriverID=Integer.parseInt(MapDriverIDstring);
        Log.d(TAG, MapDriverID.toString());

        String DriverName=totalList.get(MapDriverID).get("DriverName").toString();
        String DriverSex=totalList.get(MapDriverID).get("DriverSex").toString();
        String DriverAge=totalList.get(MapDriverID).get("DriverAge").toString();
        String CarKind=totalList.get(MapDriverID).get("CarKind").toString();
        String CarAge=totalList.get(MapDriverID).get("CarAge").toString();
        String CarBrand=totalList.get(MapDriverID).get("CarBrand").toString();
        String CarColor=totalList.get(MapDriverID).get("CarColor").toString();
        Did=totalList.get(MapDriverID).get("Did").toString();

        final Bundle toMapCaseFares=new Bundle();
        toMapCaseFares.putString("Did", Did);

        mYes=(Button)findViewById(R.id.btnMapCarYes);
        mNo=(Button)findViewById(R.id.btnMapCarNo);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("map call",  client_location_driverchoose);
                enableLocationAndGetLastLocation(false);
                Intent intent = new Intent();
                intent.setClass(MapDriverDetail.this, MapCarmap2.class);
                intent.putExtras(toMapCaseFares);
                startActivity(intent);
            }
        });

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapDriverDetail.this,MapDriverList.class));
            }
        });

        mDriverName=(TextView)findViewById(R.id.txtMapDriverNameDetail);
        mDriverSex=(TextView)findViewById(R.id.txtMapDriverSexDetail);
        mDriverAge=(TextView)findViewById(R.id.txtMapDriverAgeDetail);
        mCarKind=(TextView)findViewById(R.id.txtMapDriverCarKindDetail);
        mCarAge=(TextView)findViewById(R.id.txtMapDriverCarAgeDetail);
        mCarBrand=(TextView)findViewById(R.id.txtMapDriverCarBrandDetail);
        mCarColor=(TextView)findViewById(R.id.txtMapDriverCarColorDetail);
        mCaseFares=(TextView)findViewById(R.id.txtCaseFares);

        mDriverName.setText(DriverName);
        mDriverSex.setText(DriverSex);
        mDriverAge.setText(DriverAge);
        mCarKind.setText(CarKind);
        mCarAge.setText(CarAge);
        mCarBrand.setText(CarBrand);
        mCarColor.setText(CarColor);


    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_mapdriver", null);
    }


    @Override
    protected void onStart(){
        super.onStart();
        //啟動google api
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause(){
        super.onPause();
        enableLocationAndGetLastLocation(false);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //檢查收到的權限要求編號是否和我們送出的相同
        if(requestCode==REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION){
            if(grantResults.length!=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Location location=enableLocationAndGetLastLocation(true);
                if(location!=null){
                    onLocationChanged(location);
                }else{
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location=enableLocationAndGetLastLocation(true);
        if(location!=null){
            //Toast.makeText(MapsActivity.this, "成功取的上一次定位", Toast.LENGTH_LONG).show();
            onLocationChanged(location);
        }else{
            //Toast.makeText(MapsActivity.this, "沒有上一次定位資料", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        switch(cause){
            case CAUSE_NETWORK_LOST:
                Toast.makeText(MapDriverDetail.this, "網路斷線，無法定位", Toast.LENGTH_LONG).show();
                break;
            case CAUSE_SERVICE_DISCONNECTED:
                Toast.makeText(MapDriverDetail.this, "Google API異常，無法定位", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Location currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //抓位置的經緯度
        if(location != null){
            latitude = location.getLatitude(); //經度
            longitude = location.getLongitude(); //緯度
            la=Double.toString(latitude);
            lng=Double.toString(longitude);
            Log.d(TAG, la);
            Log.d(TAG, lng);
        }
        client_location_driverchoose = new JSONObject();
          try {
            client_location_driverchoose.put("lat", latitude);
            client_location_driverchoose.put("lng", longitude);
            client_location_driverchoose.put("did", Did);
            client_location_driverchoose.put("destination", MapCarClientDestination);
          } catch (JSONException e) {
            e.printStackTrace();
          }
    }

    private Location enableLocationAndGetLastLocation(boolean on){
        if(ContextCompat.checkSelfPermission(MapDriverDetail.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            //這項功能尚未取得使用者同意
            //開始執行徵詢使用者的流程
            if(ActivityCompat.shouldShowRequestPermissionRationale(MapDriverDetail.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder altDlgBuilder=new AlertDialog.Builder(MapDriverDetail.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("APP需要啟動定位功能");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MapDriverDetail.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                    }
                });
                altDlgBuilder.show();
                return null;
            }else{
                ActivityCompat.requestPermissions(MapDriverDetail.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                return null;
            }
        }
        Location lastLocation=null;
        if(on){
            lastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLocationRequest=LocationRequest.create();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setSmallestDisplacement(5);

            if(mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                //Toast.makeText(MapsActivity.this, "使用GPS定位", Toast.LENGTH_LONG).show();
            }else if(mLocationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                //Toast.makeText(MapsActivity.this, "使用網路定位", Toast.LENGTH_LONG).show();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }else{
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            //Toast.makeText(MapsActivity.this, "停止定位", Toast.LENGTH_LONG).show();
        }
        return lastLocation;
    }
}
