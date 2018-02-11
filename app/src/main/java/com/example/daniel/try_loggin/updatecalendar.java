package com.example.daniel.try_loggin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * Created by daniel on 2017/8/9.
 */

public class updatecalendar extends AppCompatActivity {

    private EditText mupetxt1, mupetxt2, mupetxt3, mupetxt4, mupetxt5;
    private Button mupbtn;
    private SOLitereserve dbHelper = null;
    private TextView muptxtDriverName, mtxtLookForDriverDetail;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatecalendar);

        Bundle bundle=this.getIntent().getExtras();
        mupetxt1=(EditText)findViewById(R.id.upetxt1);
        String date= bundle.getString("Date");
        mupetxt1.setText(date);
        mupetxt2=(EditText)findViewById(R.id.upetxt2);
        String location= bundle.getString("Location");
        mupetxt2.setText(location);
        mupetxt3=(EditText)findViewById(R.id.upetxt3);
        String destination= bundle.getString("Destination");
        mupetxt3.setText(destination);
        mupetxt4=(EditText)findViewById(R.id.upetxt4);
        String time= bundle.getString("Time");
        mupetxt4.setText(time);
        mupetxt5=(EditText)findViewById(R.id.upetxt5);
        final String id=bundle.getString("Id");
        mupetxt5.setText(id);
        String DriverName=bundle.getString("DriverName");
        muptxtDriverName=(TextView)findViewById(R.id.txtUpChooseFilterDriver);
        muptxtDriverName.setText(DriverName);

        mtxtLookForDriverDetail=(TextView)findViewById(R.id.txtUpDriverDetail);
        mtxtLookForDriverDetail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dbHelper = new SOLitereserve(this);
        mupbtn=(Button)findViewById(R.id.upbtn);
        mupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date_sure = mupetxt1.getText() + "";
                String location_sure = mupetxt2.getText() + "";
                String destination_sure = mupetxt3.getText() + "";
                String time_sure = mupetxt4.getText() + "";
                String sql = "update tb_reserve set date=? , location=? , destination=?, time=? where _id=?";
                boolean flag = dbHelper.execData(sql, new Object[] { date_sure, location_sure, destination_sure, time_sure, id });
                if (flag) {
                    toast("更新資料成功！");
                    startActivity(new Intent(updatecalendar.this,Reservecar.class));
                } else {
                    toast("更新資料失敗！");
                }
            }
        });

        mupetxt4.setOnClickListener(new Button.OnClickListener() {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            @Override
            public void onClick(View view) {

                TimePickerDialog timedp=new TimePickerDialog(updatecalendar.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mupetxt4.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timedp.show();
            }
        });
    }

    protected void toast(String string) {
        Toast.makeText(updatecalendar.this, string, Toast.LENGTH_LONG).show();
    }
}
