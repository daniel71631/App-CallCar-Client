package com.example.daniel.try_loggin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class forgetpasswordActivity extends AppCompatActivity {

    private EditText metxt1;
    private Button mbtn;
    private String url="http://203.77.73.5:9797/forget";
    private boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        metxt1=(EditText)findViewById(R.id.etxt1);
        mbtn=(Button)findViewById(R.id.btn);
        final RequestQueue queue = Volley.newRequestQueue(forgetpasswordActivity.this);
        click=true;

        mbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final  String forgetpwemail=metxt1.getText().toString().trim();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("usermail", forgetpwemail);
                JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getBoolean("success")){
                                        Toast.makeText(getApplicationContext(), "已寄信", Toast.LENGTH_SHORT).show();
                                    }else if(!response.getBoolean("success")){
                                        Toast.makeText(getApplicationContext(), "信箱不存在", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "信箱不存在", Toast.LENGTH_LONG).show();
                    }
                }) {
                };
                queue.add(stringRequest);
            }
        });
    }
}
