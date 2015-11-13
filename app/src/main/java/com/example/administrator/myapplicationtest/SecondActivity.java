package com.example.administrator.myapplicationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 15-11-9.
 */
public class SecondActivity extends Activity {
    private List<Map<String, Object>> list;
    private ListView listview;
    private ListViewAdapter adapter;
    private TextView titleView;
    private String name;
    private String path,pathSelected;
    private String filelist[];
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        titleView = (TextView) findViewById(R.id.title_text_view2);
        Intent secondIntent = getIntent();
        pathSelected = secondIntent.getStringExtra("path");
        name = secondIntent.getStringExtra("name");
        titleView.setText(name);
        listview = (ListView) findViewById(R.id.list_view2);
        adapter = new ListViewAdapter(this,getData());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (file.list() != null) {
                    filelist = file.list();
                    if (filelist.length != 0) {
                        File audioFile = new File(path, filelist[position]);
                        Uri uri = Uri.parse(audioFile.getAbsolutePath());
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.addCategory(Intent.CATEGORY_APP_MUSIC);
                        intent.setDataAndType(uri, "audio/*");
                        startActivity(intent);
                    }
                }
            }

        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(file.list() != null){
                    filelist = file.list();
                    if(filelist.length != 0){
                        AlertDialog bulider = new AlertDialog.Builder(SecondActivity.this).setTitle("确认删除这个文件吗？").setMessage(filelist[position])
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File deleteFile = new File(path + "/" + filelist[position]);
                                        deleteFile.delete();
                                        list.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create();
                        bulider.show();
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private List<Map<String, Object>> getData(){
        list = new ArrayList<Map<String, Object>>();
        path = pathSelected + "/" + name;
        file = new File(path);
        if(file.list() == null){
            titleView.setText(name + "目前没有录音记录");
        }else{
            filelist = file.list();
            if(filelist.length == 0){
                titleView.setText(name + "目前没有录音记录");
            }else{
                for(int i = 0; i < filelist.length; i++){
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("text", filelist[i]);
                    list.add(map);
                }
            }
        }
        return list;

    }
}
