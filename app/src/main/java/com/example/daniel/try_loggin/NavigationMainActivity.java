package com.example.daniel.try_loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.analytics.FirebaseAnalytics;


public class NavigationMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences settings;
    private static final String data = "DATA";
    private Socket mSocket;
    private SQLiteCaseRecord dbHelper = null;
    private SQLiteMapDriver dbHelperDriver = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new SQLiteCaseRecord(this);
        dbHelperDriver=new SQLiteMapDriver(this);
        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.getAppInstanceId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startFg Fragment=new startFg();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().addToBackStack(null).replace(R.id.frame, new startFg()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(NavigationMainActivity.this,settingprefernce.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_car) {
            CarFg Fragment=new CarFg();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().addToBackStack(null).replace(R.id.frame, new CarFg()).addToBackStack(null).commit();
        } else if (id == R.id.nav_reservecar) {
            startFg reserveFragment=new startFg();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().addToBackStack(null).replace(R.id.frame, new startFg()).addToBackStack(null).commit();

        } else if (id == R.id.nav_record) {
            record reFragment=new record();
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().addToBackStack(null).replace(R.id.frame, new record()).addToBackStack(null).commit();

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this,settingprefernce.class));

        } else if (id == R.id.nav_loggout) {
            dbHelper.deleteAll();
            MainActivity.metxt1.setText("");
            MainActivity.metxt2.setText("");
            MainActivity.get.edit().clear().commit();
            mSocket.disconnect();
            mSocket.off();
            dbHelperDriver.deleteAll();
            startActivity(new Intent(this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent MyIntent = new Intent(Intent.ACTION_MAIN);
            MyIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(MyIntent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if (msg!=null)
            Log.d("FCM", "msg:"+msg);
    }
}
