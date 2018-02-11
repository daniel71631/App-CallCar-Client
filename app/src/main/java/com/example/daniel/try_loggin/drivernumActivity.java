package com.example.daniel.try_loggin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class drivernumActivity extends AppCompatActivity {

    private SQLitedrivernum dbHelper = null;
    private TextView emptyText;
    private ListView lv_main;
    private SimpleAdapter adapter = null;
    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivernum);


        dbHelper = new SQLitedrivernum(this);
        lv_main = (ListView) findViewById(R.id.listView_main);
        emptyText = (TextView) findViewById(R.id.textView_empty);
        totalList = getcontent();
        adapter = new SimpleAdapter(this, totalList, R.layout.item_listview_maindrivernum, new String[]{"phonenumber", "username"},
                new int[]{R.id.textView_item_phonenumber, R.id.txtDriverName});
        lv_main.setAdapter(adapter);
        lv_main.setEmptyView(emptyText);
        registerForContextMenu(lv_main);
        lv_main.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String number = totalList.get(arg2).get("phonenumber").toString();
                AlertDialog.Builder builder = createAlertDialog(android.R.drawable.stat_sys_phone_call, "確定要撥打: " + number + " ？");
                builder.setPositiveButton("撥打", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + number));
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        mtoolbar = (Toolbar) findViewById(R.id.tbar2);
        mtoolbar.inflateMenu(R.menu.main_phonebook);
        mtoolbar.setTitle("常用司機");
        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_insert:
                        AlertDialog.Builder builder_insert = createAlertDialog(android.R.drawable.ic_dialog_alert, "新增司機");
                        View view = getLayoutInflater().inflate(R.layout.dialog_insert, null);
                        final EditText et_name = (EditText) view.findViewById(R.id.editText_dialog_name);
                        final EditText et_number = (EditText) view.findViewById(R.id.editText_dialog_number);
                        builder_insert.setView(view);
                        builder_insert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et_name.getText() + "";
                                String number = et_number.getText() + "";
                                if (name.equals("") || number.equals("")) {
                                    toast("請輸入資料");
                                } else {
                                    String sql = "insert into tb_driver(username, phonenumber)values(?,?)";
                                    boolean flag = dbHelper.execData(sql, new Object[] { name, number });
                                    if (flag) {
                                        toast("插入成功！");
                                        reloadView();
                                    } else {
                                        toast("插入失败！");
                                    }
                                }
                            }
                        });
                        builder_insert.show();
                        break;
                    case R.id.carstorenum:
                        startActivity(new Intent(drivernumActivity.this,carstoreActivity.class));
                        break;
                    case R.id.friendnumber:
                        startActivity(new Intent(drivernumActivity.this,PhonebookActivity.class));
                        break;
                }
                return false;
            }
        });

    }

    private List<Map<String, Object>> getcontent() {
        return dbHelper.selectList("select * from tb_driver", null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderIcon(android.R.drawable.btn_dialog);
        String name = totalList.get(info.position).get("username").toString();
        String number = totalList.get(info.position).get("phonenumber").toString();
        //*menu.setHeaderTitle(name + "|" + number);
        getMenuInflater().inflate(R.menu.contextmenu_listview_main, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String name = totalList.get(info.position).get("username").toString();
        String number = totalList.get(info.position).get("phonenumber").toString();
        final String _id = totalList.get(info.position).get("_id").toString();
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder builder_dele = createAlertDialog(android.R.drawable.ic_delete, "確定要刪除？");
                builder_dele.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sql = "delete from tb_driver where _id=?";
                        boolean flag = dbHelper.execData(sql, new Object[] { _id });
                        if (flag) {
                            toast("删除資料成功！");
                            reloadView();
                        } else {
                            toast("删除資料失敗！");
                        }
                    }
                });
                builder_dele.show();
                break;
            case R.id.action_update:
                AlertDialog.Builder builder_update = createAlertDialog(android.R.drawable.ic_dialog_alert, "修改司機資料");
                View view = getLayoutInflater().inflate(R.layout.dialog_upgrate, null);
                final EditText editText_name = (EditText) view.findViewById(R.id.editText_dialog_name);
                final EditText editText_number = (EditText) view.findViewById(R.id.editText_dialog_number);
                editText_name.setText(name);
                editText_number.setText(number);
                builder_update.setView(view);
                builder_update.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name_sure = editText_name.getText() + "";
                        String number_sure = editText_number.getText() + "";
                        String sql = "update tb_driver set username=? , phonenumber=? where _id=?";
                        boolean flag = dbHelper.execData(sql, new Object[] { name_sure, number_sure, _id });
                        if (flag) {
                            toast("更新資料成功！");
                            reloadView();
                        } else {
                            toast("更新資料失敗！");
                        }
                    }
                });
                builder_update.show();
                break;
            case R.id.action_sendsms:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + number));
                intent.putExtra("sms_body", "dont't call me!");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    protected void reloadView() {
        totalList.clear();
        totalList.addAll(getcontent());
        adapter.notifyDataSetChanged();
    }

    public void clickButton(View view) {
        lv_main.setSelection(0);
    }

    protected void toast(String string) {
        Toast.makeText(drivernumActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private AlertDialog.Builder createAlertDialog(int icDialogAlert, String string) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(icDialogAlert);
        builder.setTitle(string);
        builder.setNegativeButton("取消", null);
        return builder;
    }
}
