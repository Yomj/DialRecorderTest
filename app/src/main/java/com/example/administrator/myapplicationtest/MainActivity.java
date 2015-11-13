package com.example.administrator.myapplicationtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView titleText;
    private ListView listView;
    private List<Map<String, Object>> list;
    private ListViewAdapter listViewAdapter;
    private String path;
    File f;
    private String fileList[];
    private String pathSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleText = (TextView) findViewById(R.id.title_text_view);
        if(pathSelected == null){
            path = Environment.getExternalStorageDirectory() + "/recoding";
        }else{
            path = pathSelected + "/recoding";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Intent intent = new Intent(MainActivity.this,MyPhoneService.class);
        intent.putExtra("pathSelected", pathSelected);
        startService(intent);
        listView = (ListView) findViewById(R.id.list_view);
        listViewAdapter = new ListViewAdapter(this, getData());
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if(f.list()!=null){
                    Intent secondItent = new Intent();
                    secondItent.putExtra("path",path);
                    secondItent.putExtra("name", fileList[arg2]);
                    secondItent.setClass(MainActivity.this, SecondActivity.class);
                    startActivity(secondItent);
                }else{}
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(f.list() != null){
                    fileList = f.list();
                    if(fileList.length != 0){
                        AlertDialog bulider = new AlertDialog.Builder(MainActivity.this).setTitle("确认删除这个文件吗？").setMessage(fileList[position])
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File deleteFile = new File(path + "/" + fileList[position]);
                                        deleteFile.delete();
                                        list.remove(position);
                                        listViewAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();
                        bulider.show();
                    }
                }
                listViewAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private List<Map<String, Object>> getData(){
        list = new ArrayList<Map<String, Object>>();
        if(pathSelected == null){
            f = new File(Environment.getExternalStorageDirectory() + "/recoding");
        }else{
            f = new File(pathSelected + "/recoding");
        }
        if (f.list() == null){
            titleText.setText("没有录音记录");
        }else{
            fileList = f.list();
            if(fileList.length == 0){
                titleText.setText("没有录音记录");
            }else{
                for (int i = 0; i < fileList.length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("text", fileList[i]);
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(MainActivity.this, SettingFileSelect.class);
            startActivityForResult(settingIntent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String pathSelected = data.getStringExtra("pathSelected");
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        pathSelected = null;
        list.clear();
        super.onDestroy();
    }
}
