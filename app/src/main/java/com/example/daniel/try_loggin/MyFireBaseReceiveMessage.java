package com.example.daniel.try_loggin;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Set;

/**
 * Created by daniel on 2018/1/19.
 */

public class MyFireBaseReceiveMessage extends FirebaseMessagingService {

    private SQLTestFireBase dbHelper = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        dbHelper = new SQLTestFireBase(this);

        Set<String> keys = remoteMessage.getData().keySet();

        for(String s:keys) {
            Log.d("fcm data:", remoteMessage.getData().get(s));
        }

        Map data=remoteMessage.getData();

        String key1=(String)data.get("key1");
        String key2=(String)data.get("key2");

        if(key1!=null && key2!=null){
            String sql = "insert into tb_test(key1, key2)values(?,?)";
            boolean flag = dbHelper.execData(sql, new Object[] { key1, key2 });
            if(flag){
                Log.d("SQL", "True");
            }else{
                Log.d("SQL", "False");
            }
        }
    }
}
