package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2018/1/10.
 */

public class FilterDriverList extends AppCompatActivity {

    private SQLiteFilterDriverList dbHelper = null;
    private ListView lv_main;
    private SimpleAdapter adapter = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    //private TextView mtxtFilterDriverDID, mtxtFilterDriverName;
    private String url="http://203.77.73.5:9797/driverinfo";
    private JSONObject fromDBdriverinfo;
    private TextView mtxtFilterDriverName, mtxtFilterDriverSex, mtxtFilterDriverAge, mtxtFilterDriverPhonenumber, mtxtFilterCarKind, mtxtFilterCarColor, mtxtFilterCarBrand, mtxtFilterCarAge,mtxtPassengerCount;
    private String FilterDriverName, FilterDriverSex, FilterDriverAge, FilterDriverPhnoneNumber, FilterCarKind, FilterCarColor, FilterCarBrand,  FilterCarAge,FilterCarPassengerCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_driver_list);

        final RequestQueue queue = Volley.newRequestQueue(FilterDriverList.this);

        final Bundle fromFilterDriver=this.getIntent().getExtras();
        final String ReservecarTime=fromFilterDriver.getString("ReserveTime");

        dbHelper = new SQLiteFilterDriverList(this);
        totalList = getcontent();
        lv_main = (ListView) findViewById(R.id.lv_filter_driver);
        adapter = new SimpleAdapter(this, totalList, R.layout.listview_filter_driver_list, new String[]{"DID", "DriverName"},
                new int[]{R.id.txtFilterDriverDID, R.id.txtFilterChoosenDriverName});
        lv_main.setAdapter(adapter);
        reloadView();
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String filterDriverDID=totalList.get(i).get("DID").toString();
                HashMap<String, String> params = new HashMap<>();
                params.put("D_ID",filterDriverDID);
                JsonObjectRequest jsonFilterRequest = new JsonObjectRequest( url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getJSONObject("driverinfo")!=null){
                                        fromDBdriverinfo=response.getJSONObject("driverinfo");
                                        //Toast.makeText(getApplicationContext(), "傳送成功"+fromDBdriverinfo.toString(), Toast.LENGTH_LONG).show();
                                        FilterDriverName=fromDBdriverinfo.getString("Name");
                                        FilterDriverSex=fromDBdriverinfo.getString("Sexuality");
                                        FilterDriverAge=fromDBdriverinfo.getString("Age");
                                        FilterDriverPhnoneNumber=fromDBdriverinfo.getString("Cellphone");
                                        FilterCarKind=fromDBdriverinfo.getString("Kind");
                                        FilterCarColor=fromDBdriverinfo.getString("Color");
                                        FilterCarBrand=fromDBdriverinfo.getString("Brand");
                                        FilterCarAge=fromDBdriverinfo.getString("Age");
                                        FilterCarPassengerCount=fromDBdriverinfo.getString("Passenger");

                                        AlertDialog.Builder filterDialog =new AlertDialog.Builder(FilterDriverList.this);
                                        View viewfilter = getLayoutInflater().inflate(R.layout.alertdialog_filterdriverdetail, null);
                                        filterDialog.setView(viewfilter).setTitle("司機詳細資料");

                                        mtxtFilterDriverName=(TextView)viewfilter.findViewById(R.id.txtFilterChoosenDriverName);
                                        mtxtFilterDriverName.setText(FilterDriverName);
                                        mtxtFilterDriverSex=(TextView)viewfilter.findViewById(R.id.txtFilterDirverSex);
                                        mtxtFilterDriverSex.setText(FilterDriverSex);
                                        mtxtFilterDriverAge=(TextView)viewfilter.findViewById(R.id.txtFilterDriverAge);
                                        mtxtFilterDriverAge.setText(FilterDriverAge);
                                        mtxtFilterDriverPhonenumber=(TextView)viewfilter.findViewById(R.id.txtFilterDriverPhone);
                                        mtxtFilterDriverPhonenumber.setText(FilterDriverPhnoneNumber);
                                        mtxtFilterCarKind=(TextView)viewfilter.findViewById(R.id.txtFilterCarKind);
                                        mtxtFilterCarKind.setText(FilterCarKind);
                                        mtxtFilterCarColor=(TextView)viewfilter.findViewById(R.id.txtFilterCarColor);
                                        mtxtFilterCarColor.setText(FilterCarColor);
                                        mtxtFilterCarBrand=(TextView)viewfilter.findViewById(R.id.txtFilterCarBrand);
                                        mtxtFilterCarBrand.setText(FilterCarBrand);
                                        mtxtFilterCarAge=(TextView)viewfilter.findViewById(R.id.txtFilterCarAge);
                                        mtxtFilterCarAge.setText(FilterCarAge);
                                        mtxtPassengerCount=(TextView)viewfilter.findViewById(R.id.txtFilterPassengerCount);
                                        mtxtPassengerCount.setText(FilterCarPassengerCount);
                                        filterDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dbHelper.deleteAll();
                                                Bundle toFilterDriverListReserveTime=new Bundle();
                                                toFilterDriverListReserveTime.putString("ReserveTime", ReservecarTime);
                                                toFilterDriverListReserveTime.putString("FilterDriverDID",filterDriverDID);
                                                toFilterDriverListReserveTime.putString("FilterDriverName",FilterDriverName);
                                                toFilterDriverListReserveTime.putString("FilterDriverSex",FilterDriverSex);
                                                toFilterDriverListReserveTime.putString("FilterDriverAge", FilterDriverAge);
                                                toFilterDriverListReserveTime.putString("FilterDriverPhoneNum", FilterDriverPhnoneNumber);
                                                toFilterDriverListReserveTime.putString("FilterCarKind", FilterCarKind);
                                                toFilterDriverListReserveTime.putString("FilterCarColor", FilterCarColor);
                                                toFilterDriverListReserveTime.putString("FilterCarBrand", FilterCarBrand);
                                                toFilterDriverListReserveTime.putString("FilterCarAge", FilterCarAge);
                                                toFilterDriverListReserveTime.putString("FilterPassengerCount", FilterCarPassengerCount);
                                                Intent intent = new Intent();
                                                intent.setClass(FilterDriverList.this, calendar.class);
                                                intent.putExtras(toFilterDriverListReserveTime);
                                                startActivity(intent);
                                            }
                                        });
                                        filterDialog.show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "傳送失敗", Toast.LENGTH_LONG).show();
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
            }
        });

    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_filterdriver", null);
    }

    protected void reloadView() {
        totalList.clear();
        totalList.addAll(getcontent());
        adapter.notifyDataSetChanged();
    }
}
