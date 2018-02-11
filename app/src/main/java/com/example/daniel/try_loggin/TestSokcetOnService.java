package com.example.daniel.try_loggin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by daniel on 2018/1/17.
 */

public class TestSokcetOnService extends Service {

    private Socket mSocket;
    @Override
    public void onCreate() {
        super.onCreate();
        IO.Options options=new IO.Options();
        {
            try {
                options.forceNew=false;
                mSocket = IO.socket("http://203.77.73.5:9797/",options);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
