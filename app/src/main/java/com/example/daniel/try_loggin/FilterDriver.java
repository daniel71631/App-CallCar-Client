package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2018/1/5.
 */

public class FilterDriver extends AppCompatActivity {

    private ListView mlistview;
    private SimpleAdapter simpleAdapter;
    private String[] fliterKind = {"性別", "車種", "車的顏色", "品牌","營業範圍"};
    private ToggleButton mtbtnMen, mtbtnWomen, mtbtn0, mtbtn1, mtbtn2, mtbtn3, mtbtn4, mtbtn5, mtbtn6, mtbtn7, mtbtn8,  mtbtn9, mtbtn10, mtbtn11, mtbtn12, mtbtn13, mtbtn14, mtbtn15, mtbtn16, mtbtn17, mtbtn18, mtbtn19;
    private ToggleButton mtbtnStyle0, mtbtnStyle1, mtbtnStyle2, mtbtnStyle3;
    private ToggleButton mtbtnColor0, mtbtnColor1, mtbtnColor2, mtbtnColor3, mtbtnColor4, mtbtnColor5;
    private ToggleButton mtbtnBrand0, mtbtnBrand1, mtbtnBrand2, mtbtnBrand3, mtbtnBrand4, mtbtnBrand5, mtbtnBrand6, mtbtnBrand7;
    private TextView mtxtCheckKind, mtxttest;
    private ArrayList<String> SexKind=new ArrayList<String>();
    private ArrayList<String> AreaKind=new ArrayList<String>();
    private ArrayList<String> StyleKind=new ArrayList<String>();
    private ArrayList<String> ColorKind=new ArrayList<String>();
    private ArrayList<String> BrandKind=new ArrayList<String>();
    private String url="http://203.77.73.5:9797/driverfilter";
    private Button mbtnFilter;
    private JSONArray json_sex=new JSONArray(), json_style=new JSONArray(), json_color=new JSONArray(), json_brand=new JSONArray(), json_area=new JSONArray();
    private JSONArray fromDBdriverList;
    private String getArray;
    private SQLiteFilterDriverList dbHelper = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filterdirver);

        mlistview=(ListView)findViewById(R.id.Filter_listView);
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < fliterKind.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("kind", fliterKind[i]);
            items.add(item);
        }

        dbHelper = new SQLiteFilterDriverList(this);

        final RequestQueue queue = Volley.newRequestQueue(FilterDriver.this);

        Bundle fromReservercar=this.getIntent().getExtras();
        final String ReserverTime=fromReservercar.getString("Time");


        simpleAdapter = new SimpleAdapter(this, items, R.layout.item_filterdriver, new String[]{"kind"}, new int[]{R.id.txtkind});
        mlistview.setAdapter(simpleAdapter);

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int which, long l) {
                mtxtCheckKind=(TextView)findViewById(R.id.txtCheckKind);
                switch(which){

                    case 0:
                        SexKind.clear();
                        AlertDialog.Builder sexDialog =new AlertDialog.Builder(FilterDriver.this);
                        View viewdia = getLayoutInflater().inflate(R.layout.alertdialog_sex, null);
                        sexDialog.setView(viewdia).setTitle("性別");//指定自定義layout
                        mtbtnMen=(ToggleButton)viewdia.findViewById(R.id.tbtnMen);
                        mtbtnMen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnMen.isChecked())
                                {
                                    SexKind.add("男");
                                }
                                else
                                {
                                    SexKind.remove("男");
                                }
                            }
                        });
                        mtbtnWomen=(ToggleButton)viewdia.findViewById(R.id.tbtnWomen);
                        mtbtnWomen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnWomen.isChecked())
                                {
                                    SexKind.add("女");
                                }
                                else
                                {
                                    SexKind.remove("女");
                                }
                            }
                        });
                        sexDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                json_sex=new JSONArray(SexKind);
                            }
                        });
                        sexDialog.show();
                        break;

                    case 1:
                        StyleKind.clear();
                        AlertDialog.Builder StyleDialog =new AlertDialog.Builder(FilterDriver.this);
                        View viewStyle = getLayoutInflater().inflate(R.layout.alertdialog_style, null);
                        StyleDialog.setView(viewStyle).setTitle("車種");

                        mtbtnStyle0=(ToggleButton)viewStyle.findViewById(R.id.tbtnStyle0);
                        mtbtnStyle0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnStyle0.isChecked())
                                {
                                    StyleKind.add("小客車");
                                }else {
                                    StyleKind.remove("小客車");
                                }
                            }
                        });

                        mtbtnStyle1=(ToggleButton)viewStyle.findViewById(R.id.tbtnStyle1);
                        mtbtnStyle1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnStyle1.isChecked())
                                {
                                    StyleKind.add("跑車");
                                }else {
                                    StyleKind.remove("跑車");
                                }
                            }
                        });

                        mtbtnStyle2=(ToggleButton)viewStyle.findViewById(R.id.tbtnStyle2);
                        mtbtnStyle2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnStyle2.isChecked())
                                {
                                    StyleKind.add("無障礙車");
                                }else {
                                    StyleKind.remove("無障礙車");
                                }
                            }
                        });

                        mtbtnStyle3=(ToggleButton)viewStyle.findViewById(R.id.tbtnStyle3);
                        mtbtnStyle3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnStyle3.isChecked())
                                {
                                    StyleKind.add("廂型車");
                                }else {
                                    StyleKind.remove("廂型車");
                                }
                            }
                        });

                        StyleDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                json_style=new JSONArray(StyleKind);
                            }
                        });
                        StyleDialog.show();
                        break;

                    case 2:
                        ColorKind.clear();
                        AlertDialog.Builder ColorDialog =new AlertDialog.Builder(FilterDriver.this);
                        View viewColor = getLayoutInflater().inflate(R.layout.alertdialog_color, null);
                        ColorDialog.setView(viewColor).setTitle("車色");

                        mtbtnColor0=(ToggleButton)viewColor.findViewById(R.id.tbtnColor0);
                        mtbtnColor0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor0.isChecked())
                                {
                                    ColorKind.add("白色");
                                }else{
                                    ColorKind.remove("白色");
                                }
                            }
                        });

                        mtbtnColor1=(ToggleButton)viewColor.findViewById(R.id.tbtnColor1);
                        mtbtnColor1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor1.isChecked())
                                {
                                    ColorKind.add("黃色");
                                }else{
                                    ColorKind.remove("黃色");
                                }
                            }
                        });

                        mtbtnColor2=(ToggleButton) viewColor.findViewById(R.id.tbtnColor2);
                        mtbtnColor2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor2.isChecked())
                                {
                                    ColorKind.add("紅色");
                                }else{
                                    ColorKind.remove("紅色");
                                }
                            }
                        });

                        mtbtnColor3=(ToggleButton)viewColor.findViewById(R.id.tbtnColor3);
                        mtbtnColor3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor3.isChecked())
                                {
                                    ColorKind.add("藍色");
                                }else{
                                    ColorKind.remove("藍色");
                                }
                            }
                        });

                        mtbtnColor4=(ToggleButton)viewColor.findViewById(R.id.tbtnColor4);
                        mtbtnColor4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor4.isChecked())
                                {
                                    ColorKind.add("銀灰色");
                                }else{
                                    ColorKind.remove("銀灰色");
                                }
                            }
                        });

                        mtbtnColor5=(ToggleButton)viewColor.findViewById(R.id.tbtnColor5);
                        mtbtnColor5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnColor5.isChecked())
                                {
                                    ColorKind.add("黑色");
                                }else{
                                    ColorKind.remove("黑色");
                                }
                            }
                        });

                        ColorDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              json_color=new JSONArray(ColorKind);
                            }
                        });
                        ColorDialog.show();
                        break;

                    case 3:
                        BrandKind.clear();
                        AlertDialog.Builder BrandDialog =new AlertDialog.Builder(FilterDriver.this);
                        View viewBrand = getLayoutInflater().inflate(R.layout.alertdialog_brand, null);
                        BrandDialog.setView(viewBrand).setTitle("品牌");

                        mtbtnBrand0=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand0);
                        mtbtnBrand0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand0.isChecked()){
                                    BrandKind.add("Toyota");
                                }else{
                                    BrandKind.remove("Toyota");
                                }
                            }
                        });

                        mtbtnBrand1=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand1);
                        mtbtnBrand1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand1.isChecked()){
                                    BrandKind.add("賓士");
                                }else{
                                    BrandKind.remove("賓士");
                                }
                            }
                        });

                        mtbtnBrand2=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand2);
                        mtbtnBrand2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand2.isChecked()){
                                    BrandKind.add("法拉利");
                                }else{
                                    BrandKind.remove("法拉利");
                                }
                            }
                        });

                        mtbtnBrand3=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand3);
                        mtbtnBrand3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand3.isChecked()){
                                    BrandKind.add("福斯");
                                }else{
                                    BrandKind.remove("福斯");
                                }
                            }
                        });

                        mtbtnBrand4=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand4);
                        mtbtnBrand4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand4.isChecked()){
                                    BrandKind.add("福特");
                                }else{
                                    BrandKind.remove("福特");
                                }
                            }
                        });

                        mtbtnBrand5=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand5);
                        mtbtnBrand5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand5.isChecked()){
                                    BrandKind.add("Honda");
                                }else{
                                    BrandKind.remove("Honda");
                                }
                            }
                        });

                        mtbtnBrand6=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand6);
                        mtbtnBrand6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand6.isChecked()){
                                    BrandKind.add("藍寶堅尼");
                                }else{
                                    BrandKind.remove("藍寶堅尼");
                                }
                            }
                        });

                        mtbtnBrand7=(ToggleButton)viewBrand.findViewById(R.id.tbtnBrand7);
                        mtbtnBrand7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtnBrand7.isChecked()){
                                    BrandKind.add("寶馬");
                                }else{
                                    BrandKind.remove("寶馬");
                                }
                            }
                        });

                        BrandDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               json_brand=new JSONArray(BrandKind);
                            }
                        });

                        BrandDialog.show();
                        break;



                    case 4:
                        AreaKind.clear();
                        AlertDialog.Builder AreaDialog =new AlertDialog.Builder(FilterDriver.this);
                        View viewArea = getLayoutInflater().inflate(R.layout.alertdialog_area, null);
                        AreaDialog.setView(viewArea).setTitle("營業範圍");

                        mtbtn0=(ToggleButton)viewArea.findViewById(R.id.tbtn0);
                        mtbtn0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn0.isChecked())
                                {
                                    AreaKind.add("0");
                                }
                                else{
                                    AreaKind.remove("0");
                                }
                            }
                        });

                        mtbtn1=(ToggleButton)viewArea.findViewById(R.id.tbtn1);
                        mtbtn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn1.isChecked())
                                {
                                    AreaKind.add("1");
                                }
                                else{
                                    AreaKind.remove("1");
                                }
                            }
                        });

                        mtbtn2=(ToggleButton)viewArea.findViewById(R.id.tbtn2);
                        mtbtn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn2.isChecked())
                                {
                                    AreaKind.add("2");
                                }
                                else{
                                    AreaKind.remove("2");
                                }
                            }
                        });

                        mtbtn3=(ToggleButton)viewArea.findViewById(R.id.tbtn3);
                        mtbtn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn3.isChecked())
                                {
                                    AreaKind.add("3");
                                }
                                else{
                                    AreaKind.remove("3");
                                }
                            }
                        });

                        mtbtn4=(ToggleButton)viewArea.findViewById(R.id.tbtn4);
                        mtbtn4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn4.isChecked())
                                {
                                    AreaKind.add("4");
                                }
                                else{
                                    AreaKind.remove("4");
                                }
                            }
                        });

                        mtbtn5=(ToggleButton)viewArea.findViewById(R.id.tbtn5);
                        mtbtn5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn5.isChecked())
                                {
                                    AreaKind.add("5");
                                }
                                else{
                                    AreaKind.remove("5");
                                }
                            }
                        });

                        mtbtn6=(ToggleButton)viewArea.findViewById(R.id.tbtn6);
                        mtbtn6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn6.isChecked())
                                {
                                    AreaKind.add("6");
                                }
                                else{
                                    AreaKind.remove("6");
                                }
                            }
                        });

                        mtbtn7=(ToggleButton)viewArea.findViewById(R.id.tbtn7);
                        mtbtn7.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn7.isChecked())
                                {
                                    AreaKind.add("7");
                                }
                                else{
                                    AreaKind.remove("7");
                                }
                            }
                        });

                        mtbtn8=(ToggleButton)viewArea.findViewById(R.id.tbtn8);
                        mtbtn8.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn8.isChecked())
                                {
                                    AreaKind.add("8");
                                }
                                else{
                                    AreaKind.remove("8");
                                }
                            }
                        });

                        mtbtn9=(ToggleButton)viewArea.findViewById(R.id.tbtn9);
                        mtbtn9.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn9.isChecked())
                                {
                                    AreaKind.add("9");
                                }
                                else{
                                    AreaKind.remove("9");
                                }
                            }
                        });

                        mtbtn10=(ToggleButton)viewArea.findViewById(R.id.tbtn10);
                        mtbtn10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn10.isChecked())
                                {
                                    AreaKind.add("10");
                                }
                                else{
                                    AreaKind.remove("10");
                                }
                            }
                        });

                        mtbtn11=(ToggleButton)viewArea.findViewById(R.id.tbtn11);
                        mtbtn11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn11.isChecked())
                                {
                                    AreaKind.add("11");
                                }
                                else{
                                    AreaKind.remove("11");
                                }
                            }
                        });

                        mtbtn12=(ToggleButton)viewArea.findViewById(R.id.tbtn12);
                        mtbtn12.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn12.isChecked())
                                {
                                    AreaKind.add("12");
                                }
                                else{
                                    AreaKind.remove("12");
                                }
                            }
                        });

                        mtbtn13=(ToggleButton)viewArea.findViewById(R.id.tbtn13);
                        mtbtn13.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn13.isChecked())
                                {
                                    AreaKind.add("13");
                                }
                                else{
                                    AreaKind.remove("13");
                                }
                            }
                        });

                        mtbtn14=(ToggleButton)viewArea.findViewById(R.id.tbtn14);
                        mtbtn14.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn14.isChecked())
                                {
                                    AreaKind.add("14");
                                }
                                else{
                                    AreaKind.remove("14");
                                }
                            }
                        });

                        mtbtn15=(ToggleButton)viewArea.findViewById(R.id.tbtn15);
                        mtbtn15.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn15.isChecked())
                                {
                                    AreaKind.add("15");
                                }
                                else{
                                    AreaKind.remove("15");
                                }
                            }
                        });

                        mtbtn16=(ToggleButton)viewArea.findViewById(R.id.tbtn16);
                        mtbtn16.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn16.isChecked())
                                {
                                    AreaKind.add("16");
                                }
                                else{
                                    AreaKind.remove("16");
                                }
                            }
                        });

                        mtbtn17=(ToggleButton)viewArea.findViewById(R.id.tbtn17);
                        mtbtn17.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn17.isChecked())
                                {
                                    AreaKind.add("17");
                                }
                                else{
                                    AreaKind.remove("17");
                                }
                            }
                        });

                        mtbtn18=(ToggleButton)viewArea.findViewById(R.id.tbtn18);
                        mtbtn18.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn18.isChecked())
                                {
                                    AreaKind.add("18");
                                }
                                else{
                                    AreaKind.remove("18");
                                }
                            }
                        });

                        mtbtn19=(ToggleButton)viewArea.findViewById(R.id.tbtn19);
                        mtbtn10.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mtbtn19.isChecked())
                                {
                                    AreaKind.add("19");
                                }
                                else{
                                    AreaKind.remove("19");
                                }
                            }
                        });

                        AreaDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              json_area=new JSONArray(AreaKind);
                            }
                        });
                        AreaDialog.show();
                        break;

                }

            }
        });

        mbtnFilter=(Button)findViewById(R.id.btnFilter);
        mbtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, JSONArray> params = new HashMap<>();
                params.put("sexuality",json_sex);
                params.put("kind", json_style);
                params.put("color", json_color);
                params.put("brand", json_brand);
                params.put("area", json_area);
                JsonObjectRequest jsonFilterRequest = new JsonObjectRequest( url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    if(response.getJSONArray("driverlist")!=null){
                                        fromDBdriverList=response.getJSONArray("driverlist");
                                        //Toast.makeText(getApplicationContext(), "傳送成功"+fromDBdriverList.toString(), Toast.LENGTH_LONG).show();
                                        for(int i=0; i<fromDBdriverList.length();i++){
                                            try {
                                                JSONObject getArrayItem=fromDBdriverList.getJSONObject(i);
                                                String DriverID=getArrayItem.getString("D_ID");
                                                String DriverName=getArrayItem.getString("D_Name");
                                                String sql = "insert into tb_filterdriver(DID , DriverName)values(?,?)";
                                                Boolean SQL=dbHelper.execData(sql, new Object[] { DriverID,DriverName });
                                                if(SQL){
                                                    //Toast.makeText(getApplicationContext(), "新增成功", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    //Toast.makeText(getApplicationContext(), "新增失敗", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Bundle toFilterDriverListReserveTime=new Bundle();
                                        toFilterDriverListReserveTime.putString("ReserveTime", ReserverTime);
                                        Intent intent = new Intent();
                                        intent.setClass(FilterDriver.this, FilterDriverList.class);
                                        intent.putExtras(toFilterDriverListReserveTime);
                                        startActivity(intent);

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
}
