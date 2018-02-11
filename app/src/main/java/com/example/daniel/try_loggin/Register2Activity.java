package com.example.daniel.try_loggin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class Register2Activity extends AppCompatActivity {


    private Spinner mspin1;
    private EditText mebirthtxt, menametxt, mephonetxt;
    private int mYear, mMonth, mDay;
    private Button mbtnqcar;
    private String url="http://203.77.73.5:9797/c_register";
    private boolean click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mspin1=(Spinner) findViewById(R.id.spin1);
        ArrayAdapter<CharSequence> sexAdapter= ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        mspin1.setAdapter(sexAdapter);

        mebirthtxt=(EditText) findViewById(R.id.ebirthtxt);
        mebirthtxt.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar birth = Calendar.getInstance();
                mYear = birth.get(Calendar.YEAR);
                mMonth = birth.get(Calendar.MONTH);
                mDay = birth.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog birthdp = new DatePickerDialog(Register2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mebirthtxt.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                birthdp.show();
            }
        });

        Bundle bundle=this.getIntent().getExtras();
        final String email=bundle.getString("Mail");
        final String pw =bundle.getString("Password");

        menametxt=(EditText)findViewById(R.id.enametxt);
        mephonetxt=(EditText)findViewById(R.id.ephonetxt);
        final RequestQueue queue = Volley.newRequestQueue(Register2Activity.this);
        click=true;
        mbtnqcar=(Button)findViewById(R.id.btnqcar);
        mbtnqcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click){
                    HashMap<String, String> params = new HashMap<String, String>();
                    final String name=menametxt.getText().toString().trim();
                    final String phone=mephonetxt.getText().toString().trim();
                    params.put("usermail", email);
                    params.put("pw",pw);
                    params.put("username", name);
                    params.put("userphone", phone);
                    params.put("part", "2");

                    JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        if(response.getBoolean("success")){
                                            Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register2Activity.this,MainActivity.class));
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
                    click=false;
                }
                /*HashMap<String, String> params = new HashMap<String, String>();
                final String name=menametxt.getText().toString().trim();
                final String phone=mephonetxt.getText().toString().trim();
                params.put("usermail", email);
                params.put("pw",pw);
                params.put("username", name);
                params.put("userphone", phone);
                params.put("part", "2");

                JsonObjectRequest stringRequest = new JsonObjectRequest( url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getBoolean("success")){
                                        Toast.makeText(getApplicationContext(), "註冊成功", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register2Activity.this,MainActivity.class));
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
                queue.add(stringRequest);*/
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent lastpage = new Intent();
            lastpage = new Intent(Register2Activity.this, RegisterActivity.class);
            startActivity(lastpage);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
