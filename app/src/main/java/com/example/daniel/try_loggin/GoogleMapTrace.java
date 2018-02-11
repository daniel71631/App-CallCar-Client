package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by daniel on 2017/9/8.
 */

public class GoogleMapTrace extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION=100;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationMgr;
    private Marker currentMarker, itemMarker;
    private double latitude, la2=0.0;
    private double longitude, log2=0.0;
    private ArrayList<LatLng> traceOfMe;
    private Socket mSocket;
    private String CaseNum, DriverName, OnTime, OffTime,CaseFares, CaseRouteLa, CaseRouteLng, CaseStyleQuick;
    private JSONArray CaseRoute;
    private SQLiteCaseRecord dbHelper = null;
    private ArrayList<String> StringRoute=new ArrayList<>();
    public final static String TAG = GoogleMapTrace.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemaptrace);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationMgr=(LocationManager)getSystemService(LOCATION_SERVICE);

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        mSocket.once("new record", caserecord);

        dbHelper = new SQLiteCaseRecord(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.connect();
        enableLocationAndGetLastLocation(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //檢查收到的權限要求編號是否和我們送出的相同
        if(requestCode==REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION){
            if(grantResults.length!=0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Location location=enableLocationAndGetLastLocation(true);
                if(location!=null){
                    //Toast.makeText(MapsActivity.this, "成功取的上一次定位", Toast.LENGTH_LONG).show();
                    onLocationChanged(location);
                }else{
                    //Toast.makeText(MapsActivity.this, "沒有上一次定位資料", Toast.LENGTH_LONG).show();
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
                Toast.makeText(GoogleMapTrace.this, "網路斷線，無法定位", Toast.LENGTH_LONG).show();
                break;
            case CAUSE_SERVICE_DISCONNECTED:
                Toast.makeText(GoogleMapTrace.this, "Google API異常，無法定位", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        Location currentLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(location != null){
            latitude = location.getLatitude(); //經度
            longitude = location.getLongitude(); //緯度
        }
        //這個是要來顯示圖標的方法
        if (currentMarker == null) {
            String la=String.valueOf(latitude);
            String lo= String.valueOf(longitude);
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).flat(true));
            currentMarker.setTitle("目前位置");
            currentMarker.setSnippet(la+" "+lo);
        }
        else {
            currentMarker.setPosition(latLng);
            currentMarker.setTitle("目前位置");
        }
        moveMap(latLng);
        trackToMe(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder().target(place).zoom(17).build();
        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        if (itemMarker != null) {
                            itemMarker.showInfoWindow();
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                });
    }

    private void trackToMe(double lat, double lng){
        if (traceOfMe == null) {
            traceOfMe = new ArrayList<LatLng>();
        }
        traceOfMe.add(new LatLng(lat, lng));
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : traceOfMe) {
            polylineOpt.add(latlng);
        }
        polylineOpt.color(Color.RED);
        Polyline line = mMap.addPolyline(polylineOpt);
        line.setWidth(20);
    }

    private Location enableLocationAndGetLastLocation(boolean on){
        if(ContextCompat.checkSelfPermission(GoogleMapTrace.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            //這項功能尚未取得使用者同意
            //開始執行徵詢使用者的流程
            if(ActivityCompat.shouldShowRequestPermissionRationale(GoogleMapTrace.this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder altDlgBuilder=new AlertDialog.Builder(GoogleMapTrace.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("APP需要啟動定位功能");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(GoogleMapTrace.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
                    }
                });
                altDlgBuilder.show();
                return null;
            }else{
                ActivityCompat.requestPermissions(GoogleMapTrace.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION);
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

    private Emitter.Listener caserecord= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject casedetail  = (JSONObject) args[0];
            try {
                CaseNum=casedetail.getString("order_id");
                Log.d(TAG, CaseNum);
                DriverName=casedetail.getString("name");
                Log.d(TAG, DriverName);
                OnTime=casedetail.getString("ontime");
                Log.d(TAG, OnTime);
                OffTime=casedetail.getString("offtime");
                Log.d(TAG, OffTime);
                CaseFares=casedetail.getString("money");
                Log.d(TAG, CaseFares);
                CaseRoute=casedetail.getJSONArray("record");
                for(int i=0; i<CaseRoute.length(); i++){
                    CaseRouteLa=CaseRoute.getJSONObject(i).getString("lat");
                    CaseRouteLng=CaseRoute.getJSONObject(i).getString("lng");
                    StringRoute.add(CaseRouteLa);
                    StringRoute.add(CaseRouteLng);
                }
                Log.d(TAG, StringRoute.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String sql = "insert into tb_caserecord(CaseNum , DriverName, OnTime, OffTime, Fares, CaseRoute)values(?,?,?,?,?,?)";
            boolean flag = dbHelper.execData(sql, new Object[] { CaseNum, DriverName, OnTime, OffTime, CaseFares, StringRoute.toString()});
            if(flag){
                Log.d(TAG, "Success");
            }else{
                Log.d(TAG, "False");
            }
            startActivity(new Intent(GoogleMapTrace.this,NavigationMainActivity.class));
        }
    };

}
