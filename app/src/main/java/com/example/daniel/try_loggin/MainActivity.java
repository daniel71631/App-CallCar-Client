package com.example.daniel.try_loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Button mbtn1, mbtn2, mbtn3, mbtntest;
    public static EditText metxt1, metxt2;
    private String url="http://203.77.73.5:9797/c_login";
    private TextView mtxt1, mtxt2;
    private SharedPreferences settings;
    private static final String logginemail = "LoginEmail";
    private static final String logginpassword = "LoginPassword";
    private static final String data = "DATA";
    private SQLitelogin dbHelper = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    public static String Cid;
    public final static String TAG = MainActivity.class.getName();
    public static SharedPreferences get;
    private Socket mSocket;
    private TextView mtxtMainTitle;
    private String Token, message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences.Editor editor=null;
        metxt1 = (EditText) findViewById(R.id.etxt1);
        metxt2 = (EditText) findViewById(R.id.etxt2);
        mtxt2=(TextView)findViewById(R.id.txtlo);
        mbtn2 = (Button) findViewById(R.id.btnmapno);
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        dbHelper = new SQLitelogin(this);
        totalList = getcontent();

        mtxtMainTitle=(TextView)findViewById(R.id.txtMainTitle);
        mtxtMainTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/segoescb.ttf"));

        SocketApplication app = (SocketApplication) getApplication();
        mSocket = app.getSocket();

        Token= FirebaseInstanceId.getInstance().getToken();

        mbtntest=(Button)findViewById(R.id.btntest);
        mbtntest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NavigationMainActivity.class));
            }
        });

        get=getSharedPreferences(data,0);
        metxt1.setText(get.getString(logginemail, ""));
        metxt2.setText(get.getString(logginpassword, ""));
        if(!metxt1.getText().toString().equals("") && !metxt2.getText().toString().equals("") ){
            HashMap<String, String> params = new HashMap<String, String>();
            final String useremail=metxt1.getText().toString().trim();
            final String password=metxt2.getText().toString().trim();
            params.put("user", useremail);
            params.put("pw",password);
            JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                if(response.getString("cid")!=null){
                                    Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_SHORT).show();
                                    mSocket.on("loginstatus", onNewMessage);
                                    mSocket.on("repeat login", repeatlogin);
                                    mSocket.connect();
                                    Cid = response.getString("cid");
                                    saveData();
                                    String sql = "insert into tb_login(loginuseremail , loginpassword)values(?,?)";
                                    dbHelper.execData(sql, new Object[] { useremail, password });
                                    startActivity(new Intent(MainActivity.this,NavigationMainActivity.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "登入失敗", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), "登入失敗", Toast.LENGTH_LONG).show();
                }
            }) {
            };
            queue.add(stringRequest);
        }
        mtxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NavigationMainActivity.class));
            }
        });


        mbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        mbtn3=(Button)findViewById(R.id.btnmapyes);
        mbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapCarmap2.class));
            }
        });

        final String email = metxt1.getText().toString().trim();
        final String emailPattern = "\\w+([-+.]\\w+)*" + "\\@"
                + "\\w+([-.]\\w+)*" + "\\." + "\\w+([-.]\\w+)*";
        mbtn1 = (Button) findViewById(R.id.btnqcar);
        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (metxt1.getText().toString().equals("") && !metxt2.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(),
                            "帳號欄位是空的", Toast.LENGTH_SHORT).show();
                } else if (metxt2.getText().toString().equals("") && !metxt1.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(),
                            "密碼欄位是空的", Toast.LENGTH_SHORT).show();
                } else if (metxt1.getText().toString().equals("") && metxt2.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(),
                            "帳號和密碼欄位都是空的", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern) && metxt1.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "無效的信箱地址 請重新輸入", Toast.LENGTH_SHORT).show();
                } else if (metxt2.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "無效的密碼 請重新輸入", Toast.LENGTH_SHORT).show();
                } else {
                            HashMap<String, String> params = new HashMap<String, String>();
                            final String useremail=metxt1.getText().toString().trim();
                            final String password=metxt2.getText().toString().trim();
                            params.put("user", useremail);
                            params.put("pw",password);
                            JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try{
                                                Cid = response.getString("cid");
                                                if(response.getString("cid")!=null){
                                                    Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_SHORT).show();
                                                    mSocket.on("loginstatus", onNewMessage);
                                                    mSocket.connect();
                                                    saveData();
                                                    String sql = "insert into tb_login(loginuseremail , loginpassword)values(?,?)";
                                                    dbHelper.execData(sql, new Object[] { useremail, password });
                                                    startActivity(new Intent(MainActivity.this,NavigationMainActivity.class));
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "登入失敗", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //Toast.makeText(getApplicationContext(), "登入失敗", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                            };
                            queue.add(stringRequest);
                }
               }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //readData();
       /* SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM tb_login", null);
        if(!metxt1.getText().toString().equals("") && !metxt2.getText().toString().equals("") ){
            startActivity(new Intent(MainActivity.this,NavigationMainActivity.class));
        }*/
    }

    @Override
    protected void onDestroy() {
        mSocket.connect();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mSocket.connect();
        super.onStop();
    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_login", null);
    }


    public void saveData(){
        settings = getSharedPreferences(data,0);
        if(!metxt1.getText().toString().equals("") && !metxt2.getText().toString().equals("")){
            settings.edit()
                    .putString(logginemail, metxt1.getText().toString().trim())
                    .putString(logginpassword, metxt2.getText().toString().trim())
                    .commit();
            Log.d(TAG,"savesuccess");
        }
    }

    protected void toast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    public void cleardata(){
        get.edit().clear().commit();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            String data=args[0].toString();
            JSONObject DriverLocationDetail2 = new JSONObject();
            try {
                DriverLocationDetail2.put("cid", Cid);
                DriverLocationDetail2.put("token",Token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(data.equals("false")){
                mSocket.emit("add user", DriverLocationDetail2);
            }
        }
    };
    private Emitter.Listener repeatlogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            mSocket.disconnect();
            mSocket.off();
            get.edit().clear().commit();
            MainActivity.metxt1.setText("");
            MainActivity.metxt2.setText("");
        }
    };


}
