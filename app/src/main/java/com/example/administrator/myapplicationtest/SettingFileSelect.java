package com.example.administrator.myapplicationtest;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-11-9.
 */
public class SettingFileSelect extends ListActivity implements View.OnClickListener {

    private TextView myPath;
    private String curPath = "/";
    private String rootPath = "/sdcard/";
    private List<String> items = null;
    private List<String> paths = null;
    private BaseAdapter fileSelectedAdapter;
    public int Selectedposition=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_fileselect);
        myPath = (TextView) findViewById(R.id.path_textview);
        Button confirm = (Button) findViewById(R.id.pathconfirm_button);
        Button cancle = (Button) findViewById(R.id.pathcancle_button);
        View.OnClickListener myClickListener = new View.OnClickListener(){
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.pathcancle_button:
                        finish();
                        break;
                    case R.id.pathconfirm_button:
                        Intent pathSelected = new Intent(SettingFileSelect.this,MainActivity.class);
                        pathSelected.putExtra("pathSelected",curPath);
                        Log.i("TAG",curPath);
                        setResult(RESULT_OK, pathSelected);
                        startActivity(pathSelected);
                        break;
                    default:
                        break;
                }
            }
        };
        confirm.setOnClickListener(myClickListener);
        cancle.setOnClickListener(myClickListener);
        getFileDir(rootPath);
    }

    private void getFileDir(String filePath) {
        // TODO Auto-generated method stub
        myPath.setText(filePath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filePath);
        File[] files = f.listFiles();

        if (!filePath.equals(rootPath)) {
            items.add("a1");
            paths.add(rootPath);
            items.add("a2");
            paths.add(f.getParent());
        }

        if (files==null){
            //to do
        }else{
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                items.add(file.getName());
                paths.add(file.getPath());
            }
        }

        fileSelectedAdapter = new FileSelectListViewAdapter(this, items, paths, Selectedposition);
        setListAdapter(fileSelectedAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        try {
            File file = new File(paths.get(position));
            if (file.isDirectory()) {
                curPath = paths.get(position);
                v.setBackgroundColor(Color.BLUE);
                getFileDir(paths.get(position));
            } else{
                v.setSelected(true);
                Selectedposition=position;
                curPath = paths.get(position);
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }



}
