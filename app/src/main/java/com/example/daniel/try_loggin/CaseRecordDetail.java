package com.example.daniel.try_loggin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2017/9/14.
 */
public class CaseRecordDetail extends AppCompatActivity {

    private TextView mDriverName, mOnTime, mOffTime, mFares;
    private Button mbtnmaproute;
    private SQLiteCaseRecord dbHelper = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    public final static String TAG = CaseRecordDetail.class.getName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caserecorddetail);

        dbHelper = new SQLiteCaseRecord(this);
        totalList = getcontent();

        Bundle fromCaseRecord=this.getIntent().getExtras();
        String DriverName=fromCaseRecord.getString("DriverName");
        String OnTime=fromCaseRecord.getString("OnTime");
        String OffTime=fromCaseRecord.getString("OffTime");
        String Fares=fromCaseRecord.getString("Fares");
        String CaseNum=fromCaseRecord.getString("CaseNum");
        /*CaseNum=CaseNum.replace("O", "");
        CaseNum=CaseNum.replace("0", "");*/
        String Case_idString=fromCaseRecord.getString("Case_id");
        //final String Route=fromCaseRecord.getString("Route");
        final Integer Case_id=Integer.parseInt(Case_idString);
        //final Integer Case_Num=Integer.parseInt(CaseNum);
        Log.d(TAG,Case_id.toString());

        mDriverName=(TextView)findViewById(R.id.txtMapDriverNameDetail);
        mDriverName.setText(DriverName);
        mOnTime=(TextView)findViewById(R.id.txtMapDriverSexDetail);
        mOnTime.setText(OnTime);
        mOffTime=(TextView)findViewById(R.id.txtMapDriverAgeDetail);
        mOffTime.setText(OffTime);
        mFares=(TextView)findViewById(R.id.txtMapDriverCarKindDetail);
        mFares.setText(Fares);

        mbtnmaproute=(Button)findViewById(R.id.btnRoute);
        mbtnmaproute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Route=totalList.get(Case_id).get("CaseRoute").toString();
                Bundle toCaseMapRoute=new Bundle();
                toCaseMapRoute.putString("Route", Route);
                Intent intent = new Intent();
                intent.setClass(CaseRecordDetail.this, CaseMapRoute.class);
                intent.putExtras(toCaseMapRoute);
                startActivity(intent);
            }
        });
    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_caserecord", null);
    }
}
