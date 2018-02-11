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
 * Created by daniel on 2017/9/17.
 */

public class MapDriverList extends AppCompatActivity {

    private SQLiteMapDriver dbHelper = null;
    private ListView lv_main;
    private SimpleAdapter adapter = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdriverlist);

        dbHelper = new SQLiteMapDriver(this);
        totalList = getcontent();
        lv_main = (ListView) findViewById(R.id.listView_mapdriverlist);
        adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_mapdriver, new String[]{"DriverName", "CarDistance", "listid"},
                new int[]{R.id.txtmapdriverlist_drivername, R.id.txtmapdriverlist_distance, R.id.txtmapdriverlist_id});
        lv_main.setAdapter(adapter);
        reloadView();
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String MapDriverlist_id=totalList.get(i).get("listid").toString();
                Bundle toMapDriverDetail=new Bundle();
                toMapDriverDetail.putString("MapDriverListId", MapDriverlist_id);
                Intent intent = new Intent();
                intent.setClass(MapDriverList.this, MapDriverDetail.class);
                intent.putExtras(toMapDriverDetail);
                startActivity(intent);
            }
        });

    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_mapdriver", null);
    }

    protected void reloadView() {
        totalList.clear();
        totalList.addAll(getcontent());
        adapter.notifyDataSetChanged();
    }
}
