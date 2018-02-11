package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by daniel on 2017/8/9.
 */

public class Verification extends AppCompatActivity {

    private EditText metxt1;
    private TextView mtxt1;
    private Button mbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        metxt1=(EditText)findViewById(R.id.etxt1);
        mbtn=(Button)findViewById(R.id.btn);

        Bundle bundle=this.getIntent().getExtras();
        final String email=bundle.getString("Mail");
        final String pw =bundle.getString("Password");
        final String ver=bundle.getString("Ver");
        final String ever=metxt1.getText().toString();

        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(metxt1.getText().toString().equals(ver)){
                    Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
            Bundle bundle01=new Bundle();
            bundle01.putString("Mail", email);
            bundle01.putString("Password", pw);
            Intent intent = new Intent();
            intent.setClass(Verification.this, Register2Activity.class);
            intent.putExtras(bundle01);
            startActivity(intent);
                }
            }
        });
    }
}
