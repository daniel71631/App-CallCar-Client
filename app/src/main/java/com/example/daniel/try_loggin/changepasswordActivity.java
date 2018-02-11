package com.example.daniel.try_loggin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class changepasswordActivity extends AppCompatActivity {

    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        mtoolbar=(Toolbar)findViewById(R.id.tbar);
        mtoolbar.setTitle("變更密碼");
        mtoolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(changepasswordActivity.this,settingprefernce.class));
            }
        });
    }
}
