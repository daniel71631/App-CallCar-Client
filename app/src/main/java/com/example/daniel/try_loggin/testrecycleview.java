package com.example.daniel.try_loggin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 2017/8/26.
 */

public class testrecycleview extends AppCompatActivity {

    private SOLitereserve dbHelper = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    private android.widget.SimpleAdapter adapter = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testrecycleview);
        dbHelper = new SOLitereserve(this);
        totalList = getcontent();

        SimpleAdapter myAdapter = new SimpleAdapter((List<Map<String, Object>>) adapter);
        RecyclerView mList = (RecyclerView) findViewById(R.id.list_view2);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new android.widget.SimpleAdapter(this, totalList, R.layout.item_listview_calendar2, new String[]{"time", "destination", "date", "location", "_id"},
                new int[]{R.id.reservetime, R.id.reservedes, R.id.reservedate, R.id.reservelocation, R.id.reserveid});
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);
    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_reserve ORDER BY date ASC,  0+ time ASC", null);
    }

    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private List<Map<String, Object>> mData;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_listview_calendar2, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mtime.setText((CharSequence) mData.get(position));
            holder.mdate.setText((CharSequence) mData.get(position));
            holder.mdestination.setText((CharSequence) mData.get(position));
            holder.mlocation.setText((CharSequence) mData.get(position));
            holder.mid.setText((CharSequence) mData.get(position));
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mtime, mdate, mlocation, mdestination, mid;
            public ViewHolder(View v) {
                super(v);
                mtime = (TextView) v.findViewById(R.id.reservetime);
                mdate = (TextView) v.findViewById(R.id.reservedate);
                mlocation = (TextView) v.findViewById(R.id.reservelocation);
                mdestination = (TextView) v.findViewById(R.id.reservedes);
                mid = (TextView) v.findViewById(R.id.reserveid);
            }
        }

        public SimpleAdapter(List<Map<String, Object>> data) {
            mData = data;
        }
    }
}
