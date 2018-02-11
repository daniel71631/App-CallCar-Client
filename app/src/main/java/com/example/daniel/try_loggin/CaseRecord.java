package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2017/9/14.
 */
public class CaseRecord extends AppCompatActivity {

    private SQLiteCaseRecord dbHelper = null;
    private ListView lv_main;
    private SimpleAdapter adapter = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    private String  CASEID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caserecord);

        dbHelper = new SQLiteCaseRecord(this);
        totalList = getcontent();
        lv_main = (ListView) findViewById(R.id.listView_caserecord);
        adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_caserecordcardview, new String[]{"DriverName", "OnTime", "OffTime", "Fares", "CaseNum", "_id"},
                       new int[]{R.id.txtmapdriverlist_drivername, R.id.txtmapdriverlist_distance, R.id.txtitem_OffTime, R.id.txtitemCaseFares,  R.id.txtitemCaseNum, R.id.txtcase_id});
        lv_main.setAdapter(adapter);
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String DriverName=totalList.get(i).get("DriverName").toString();
                final String OnTime=totalList.get(i).get("OnTime").toString();
                final String OffTime=totalList.get(i).get("OffTime").toString();
                final String Fares=totalList.get(i).get("Fares").toString();
                String CaseNum=totalList.get(i).get("CaseNum").toString();
                for(int CaseID=0;CaseID<=i;CaseID++){
                   CASEID=Integer.toString(CaseID);
                }
                final String Case_id=totalList.get(i).get("_id").toString();
                Bundle toCaseRecordDetail=new Bundle();
                toCaseRecordDetail.putString("DriverName", DriverName);
                toCaseRecordDetail.putString("OnTime", OnTime);
                toCaseRecordDetail.putString("OffTime", OffTime);
                toCaseRecordDetail.putString("Fares", Fares);
                toCaseRecordDetail.putString("CaseNum", CaseNum);
                //toCaseRecordDetail.putString("Route", Route);
                toCaseRecordDetail.putString("Case_id", CASEID);
                Intent intent = new Intent();
                intent.setClass(CaseRecord.this, CaseRecordDetail.class);
                intent.putExtras(toCaseRecordDetail);
                startActivity(intent);
            }
        });
    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_caserecord", null);
    }


}
