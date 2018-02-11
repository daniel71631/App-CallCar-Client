package com.example.daniel.try_loggin;


import android.content.Intent;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private Button mbtn1;
    private EditText memailtxt, mepwtxt, mepwchecktxt;
    private String url="http://203.77.73.5:9797/c_register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        memailtxt=(EditText) findViewById(R.id.emailtxt) ;
        mepwtxt=(EditText) findViewById(R.id.epwtxt);
        mepwchecktxt=(EditText) findViewById(R.id.epwchecktxt);
        mbtn1=(Button) findViewById(R.id.btnmapyes);
        final RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        mbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email =  memailtxt.getText().toString().trim();
                String emailPattern ="\\w+([-+.]\\w+)*" + "\\@"
                        + "\\w+([-.]\\w+)*" + "\\." + "\\w+([-.]\\w+)*";
                if(!email.matches(emailPattern) && !memailtxt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"無效的信箱地址", Toast.LENGTH_SHORT).show();
                }
                else if(!mepwtxt.getText().toString().equals(mepwchecktxt.getText().toString())){
                    Toast.makeText(getApplicationContext(),"密碼輸入不一致", Toast.LENGTH_SHORT).show();
                }
                else if(mepwtxt.getText().toString().length()<6){
                    Toast.makeText(getApplicationContext(),"密碼長度過短 請重新輸入", Toast.LENGTH_SHORT).show();
                }
                else{
                        mbtn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap<String, String> params = new HashMap<String, String>();
                                final String useremail=memailtxt.getText().toString().trim();
                                final String password=mepwtxt.getText().toString().trim();
                                params.put("usermail", useremail);
                                params.put("pw",password);
                                params.put("part", "1");
                                final String email =  memailtxt.getText().toString().trim();
                                final String emailPattern ="\\w+([-+.]\\w+)*" + "\\@"
                                        + "\\w+([-.]\\w+)*" + "\\." + "\\w+([-.]\\w+)*";
                                JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try{
                                                /*JSONObject js=new JSONObject(response);*/
                                                    if(!response.getBoolean("isrepeat") && email.matches(emailPattern) && mepwtxt.getText().toString().length()>=6 && mepwtxt.getText().toString().equals(mepwchecktxt.getText().toString())&& !memailtxt.getText().toString().equals("") && !mepwtxt.getText().toString().equals("") && !mepwchecktxt.getText().toString().equals("")){
                                                        //*Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                                        //*response.getJSONObject("abc");
                                                        String verification=response.getString("random");
                                                        String useremail=memailtxt.getText().toString().trim();
                                                        String password=mepwtxt.getText().toString().trim();
                                                        Bundle bundle=new Bundle();
                                                        bundle.putString("Ver", verification);
                                                        bundle.putString("Mail", useremail);
                                                        bundle.putString("Password", password);
                                                        Intent intent = new Intent();
                                                        intent.setClass(RegisterActivity.this, Verification.class);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        //*startActivity(new Intent(RegisterActivity.this,Verification.class));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), "註冊失敗", Toast.LENGTH_LONG).show();
                                    }
                                }) {
                                };
                                queue.add(stringRequest);
                            }
                        });
                }
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent lastpage = new Intent();
            lastpage = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(lastpage);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
