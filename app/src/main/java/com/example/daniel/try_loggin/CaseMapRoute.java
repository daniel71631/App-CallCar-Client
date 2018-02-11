package com.example.daniel.try_loggin;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by daniel on 2017/9/14.
 */

public class CaseMapRoute extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private final int REQUEST_PERMISSION_FOR_ACCESS_FINE_LOCATION=100;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager mLocationMgr;
    private Marker currentMarker, itemMarker;
    private double latitude, la2=0.0;
    private double longitude, log2=0.0;
    private ArrayList<LatLng> traceOfMe;
    public final static String TAG = CaseMapRoute.class.getName();
    private String[] RouteMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions polylineOpt = new PolylineOptions();
        for(int i=0; i<RouteMap.length; i+=2){
            Double La=Double.parseDouble(RouteMap[i]);
            Double Lng=Double.parseDouble(RouteMap[i+1]);
            polylineOpt.add(new LatLng(La, Lng));
        }
        polylineOpt.color(Color.RED);
        Polyline line = mMap.addPolyline(polylineOpt);
        line.setWidth(20);
        LatLng Startlocation=new LatLng(Double.parseDouble(RouteMap[0]), Double.parseDouble(RouteMap[1]));
        LatLng Endlocation=new LatLng(Double.parseDouble(RouteMap[RouteMap.length-2]), Double.parseDouble(RouteMap[RouteMap.length-1]));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(Startlocation);
        builder.include(Endlocation);
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.casemaproute);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient=new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationMgr=(LocationManager)getSystemService(LOCATION_SERVICE);

        Bundle fromcaseRecordDetail=this.getIntent().getExtras();
        String Route=fromcaseRecordDetail.getString("Route");

        Route=Route.replace("[", " ");
        Route=Route.replace("]", " ");
        RouteMap=Route.split(",");
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

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
