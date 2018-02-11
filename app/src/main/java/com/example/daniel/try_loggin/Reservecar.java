package com.example.daniel.try_loggin;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2017/8/9.
 */

public class Reservecar extends AppCompatActivity {

    private int mYear, mMonth, mDay, mToday;
    private Button mbtn;
    private SOLitereserve dbHelper = null;
    private ListView lv;
    private SimpleAdapter adapter = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    private TextView txtdate;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservecar);

        final Calendar calendar=Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, final int year, final int monthOfYear, final int dayOfMonth) {
                mbtn=(Button)findViewById(R.id.btn);
                mbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mDay<dayOfMonth){
                            final String time = Integer.toString(year) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                            Bundle bundle = new Bundle();
                            bundle.putString("Time", time);
                            Intent intent = new Intent();
                            intent.setClass(Reservecar.this, FilterDriver.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            toast("不能預約今天以前，請另選日期");
                        }

                    }
                });
            }
        });

        dbHelper = new SOLitereserve(this);
        lv = (ListView) findViewById(R.id.list);
        totalList = getcontent();
        adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_calendar, new String[]{"time", "destination", "date", "location", "_id","DName"},
                new int[]{R.id.reservetime, R.id.reservedes, R.id.reservedate, R.id.reservelocation, R.id.reserveid, R.id.txtFilterChoosenDriverName});
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_reserve ORDER BY date ASC,  0+ time ASC", null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderIcon(android.R.drawable.btn_dialog);
        getMenuInflater().inflate(R.menu.contextmenu_listview_calendar, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String date = totalList.get(info.position).get("date").toString();
        String location = totalList.get(info.position).get("location").toString();
        String destination = totalList.get(info.position).get("destination").toString();
        String time2 = totalList.get(info.position).get("time").toString();
        final String id = totalList.get(info.position).get("_id").toString();
        String DriverName=totalList.get(info.position).get("DName").toString();
        switch(item.getItemId()){
            case R.id.action_delete:
                AlertDialog.Builder builder_dele = createAlertDialog(android.R.drawable.ic_delete, "確定要刪除？");
                builder_dele.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sql = "delete from tb_reserve where _id=?";
                        boolean flag = dbHelper.execData(sql, new Object[] { id });
                        if (flag) {
                            toast("刪除資料成功！");
                            reloadView();
                        } else {
                            toast("刪除資料失敗！");
                        }
                    }
                });
                builder_dele.show();
                break;
            case R.id.action_update:
                Bundle bundle = new Bundle();
                bundle.putString("Date", date);
                bundle.putString("Location", location);
                bundle.putString("Destination", destination);
                bundle.putString("Time", time2);
                bundle.putString("Id", id);
                bundle.putString("DriverName", DriverName);
                Intent intent = new Intent();
                intent.setClass(Reservecar.this, updatecalendar.class);
                intent.putExtras(bundle);
                startActivity(intent);
                //*startActivity(new Intent(MainActivity.this,update.class));
                break;
        }
        return super.onContextItemSelected(item);
    }

    protected void toast(String string) {
        Toast.makeText(Reservecar.this, string, Toast.LENGTH_LONG).show();
    }
    private AlertDialog.Builder createAlertDialog(int icDialogAlert, String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(icDialogAlert);
        builder.setTitle(string);
        builder.setNegativeButton("取消", null);
        return builder;
    }
    protected void reloadView() {
        totalList.clear();
        totalList.addAll(getcontent());
        adapter.notifyDataSetChanged();
    }
}
