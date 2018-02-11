package com.example.daniel.try_loggin;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by daniel on 2017/8/9.
 */

public class calendar extends AppCompatActivity {

    private EditText metxt1, metxt4;
    private SOLitereserve dbHelper = null;
    private Button mbtn;
    private SimpleAdapter adapter = null;
    private TextView mtxtFilterChooseDriver, mtxtLookForDriverDetail;
    private TextView mtxtFilterDriverName,mtxtFilterDriverSex,mtxtFilterDriverAge,mtxtFilterDriverPhonenumber,mtxtFilterCarKind, mtxtFilterCarColor, mtxtFilterCarBrand,  mtxtFilterCarAge, mtxtPassengerCount ;
    private String url="http://203.77.73.5:9797/newappointment";
    private String toDBtimeFormat, CID, AID;
    //private String pat1 = "yyyy-MM-dd HH:mm";//時間模式模板
    private String transferDateFormatToString;
    private Date transferDateFormat;
    private String TAG="AID=";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        final RequestQueue queue = Volley.newRequestQueue(calendar.this);

        Bundle fromFilterDriverList =this.getIntent().getExtras();
        metxt1=(EditText)findViewById(R.id.etxt1);
        final String ReserveDay= fromFilterDriverList.getString("ReserveTime");
        metxt1.setText(ReserveDay);

        final String DID=fromFilterDriverList.getString("FilterDriverDID");
        final String DriverName=fromFilterDriverList.getString("FilterDriverName");
        final String DriverSex=fromFilterDriverList.getString("FilterDriverSex");
        final String DriverAge=fromFilterDriverList.getString("FilterDriverAge");
        final String DriverPhoneNum=fromFilterDriverList.getString("FilterDriverPhoneNum");
        final String DriverCarKind=fromFilterDriverList.getString("FilterCarKind");
        final String DriverCarColor=fromFilterDriverList.getString("FilterCarColor");
        final String DriverCarBrand=fromFilterDriverList.getString("FilterCarBrand");
        final String DriverCarAge=fromFilterDriverList.getString("FilterCarAge");
        final String DriverCarPassengerNum=fromFilterDriverList.getString("FilterPassengerCount");

        mtxtFilterChooseDriver=(TextView)findViewById(R.id.txtChooseFilterDriver);
        mtxtFilterChooseDriver.setText(DriverName);

        mtxtLookForDriverDetail=(TextView)findViewById(R.id.txtLookForDriverDetail);
        mtxtLookForDriverDetail.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder filterDialog =new AlertDialog.Builder(calendar.this);
                View viewfilter = getLayoutInflater().inflate(R.layout.alertdialog_filterdriverdetail, null);
                filterDialog.setView(viewfilter).setTitle("司機詳細資料");

                mtxtFilterDriverName=(TextView)viewfilter.findViewById(R.id.txtFilterChoosenDriverName);
                mtxtFilterDriverName.setText(DriverName);
                mtxtFilterDriverSex=(TextView)viewfilter.findViewById(R.id.txtFilterDirverSex);
                mtxtFilterDriverSex.setText(DriverSex);
                mtxtFilterDriverAge=(TextView)viewfilter.findViewById(R.id.txtFilterDriverAge);
                mtxtFilterDriverAge.setText(DriverAge);
                mtxtFilterDriverPhonenumber=(TextView)viewfilter.findViewById(R.id.txtFilterDriverPhone);
                mtxtFilterDriverPhonenumber.setText(DriverPhoneNum);
                mtxtFilterCarKind=(TextView)viewfilter.findViewById(R.id.txtFilterCarKind);
                mtxtFilterCarKind.setText(DriverCarKind);
                mtxtFilterCarColor=(TextView)viewfilter.findViewById(R.id.txtFilterCarColor);
                mtxtFilterCarColor.setText(DriverCarColor);
                mtxtFilterCarBrand=(TextView)viewfilter.findViewById(R.id.txtFilterCarBrand);
                mtxtFilterCarBrand.setText(DriverCarBrand);
                mtxtFilterCarAge=(TextView)viewfilter.findViewById(R.id.txtFilterCarAge);
                mtxtFilterCarAge.setText(DriverCarAge);
                mtxtPassengerCount=(TextView)viewfilter.findViewById(R.id.txtFilterPassengerCount);
                mtxtPassengerCount.setText(DriverCarPassengerNum);

                filterDialog.setPositiveButton("查看完畢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                filterDialog.show();
            }
        });

        metxt4=(EditText)findViewById(R.id.etxt4);
        metxt4.setOnClickListener(new Button.OnClickListener() {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            @Override
            public void onClick(View view) {
                TimePickerDialog timedp=new TimePickerDialog(calendar.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        metxt4.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timedp.show();
            }
        });

        dbHelper = new SOLitereserve(this);

        mbtn=(Button)findViewById(R.id.btn);
        mbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final EditText mmetxt1=(EditText)findViewById(R.id.etxt1);
                final EditText mmetxt2=(EditText)findViewById(R.id.etxt2);
                final EditText mmetxt3=(EditText)findViewById(R.id.etxt3);
                //final EditText mmetxt4=(EditText)findViewById(R.id.etxt4);
                final String Date=mmetxt1.getText()+ "";
                final String Location=mmetxt2.getText()+ "";
                final String Destination=mmetxt3.getText()+ "";
                final String Time=metxt4.getText()+ "";
                final String DriverNameText=mtxtFilterChooseDriver.getText()+"";
                CID=MainActivity.Cid;
                toDBtimeFormat=ReserveDay+" "+Time;
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-M-d H:m") ;
                try {
                    transferDateFormat=sdf1.parse(toDBtimeFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf1.applyPattern("yyyy-MM-dd HH:mm");
                transferDateFormatToString=sdf1.format(transferDateFormat);
                Toast.makeText(getApplicationContext(), transferDateFormatToString, Toast.LENGTH_SHORT).show();

                Bundle bundleback=new Bundle();
                bundleback.putString("Date", Date);

                HashMap<String, String> params = new HashMap<>();
                params.put("C_ID",CID);
                params.put("D_ID", DID);
                params.put("Time",transferDateFormatToString);
                params.put("Location", Location);
                params.put("Destination", Destination);
                JsonObjectRequest jsonFilterRequest = new JsonObjectRequest( url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getString("A_ID")!=null){
                                        AID=response.getString("A_ID");
                                        Log.d(TAG,AID);
                                        Toast.makeText(getApplicationContext(), "傳送成功"+ AID, Toast.LENGTH_SHORT).show();
                                        if(Date.equals("") || Location.equals("") || Destination.equals("") || Time.equals("")){
                                            toast("請輸入資料");
                                        }else{
                                            String sql = "insert into tb_reserve(date, location, destination, time,  DID, DName)values(?,?,?,?,?,?)";
                                            boolean flag = dbHelper.execData(sql, new Object[] { Date, Location, Destination, Time, DID, DriverName });
                                            if(flag && AID!=null){
                                                toast("預約成功");
                                                startActivity(new Intent(calendar.this,Reservecar.class));
                                            }else{
                                                toast("預約失敗");
                                            }
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "傳送失敗", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                };
                queue.add(jsonFilterRequest);

                /*if(Date.equals("") || Location.equals("") || Destination.equals("") || Time.equals("")){
                    toast("請輸入資料");
                }else{
                    String sql = "insert into tb_reserve(date, location, destination, time,  DID, DName)values(?,?,?,?,?,?)";
                    boolean flag = dbHelper.execData(sql, new Object[] { Date, Location, Destination, Time, DID, DriverName });
                    if(flag && AID!=null){
                        toast("預約成功");
                        startActivity(new Intent(calendar.this,Reservecar.class));
                    }else{
                        toast("預約失敗");
                    }
                }*/
            }
        });
    }

    protected void toast(String string) {
        Toast.makeText(calendar.this, string, Toast.LENGTH_LONG).show();
    }
}
