package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;


/**
 * Created by daniel on 2017/7/19.
 */

public class settingprefernce extends PreferenceActivity {

     private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent lastpage = new Intent();
            lastpage = new Intent(settingprefernce.this, NavigationMainActivity.class);
            startActivity(lastpage);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
