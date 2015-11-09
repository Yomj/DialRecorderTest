package com.example.administrator.myapplicationtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 15-11-9.
 */
public class FileSelectListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater myLayoutInflater;
    private List<String> items,paths;
    private int selsetedPosition;

    public FileSelectListViewAdapter(Context context, List<String> items,List<String> paths,int position){
        this.context = context;
        myLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.paths = paths;
        this.selsetedPosition = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = myLayoutInflater.inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.fileText = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try{
            File file = new File(paths.get(position).toString());
            if(items.get(position).toString().equals("a1")){
                viewHolder.fileText.setText("返回根目录");
            }else if(items.get(position).toString().equals("a2")){
                viewHolder.fileText.setText("返回上级目录");
            }else{
                viewHolder.fileText.setText(file.getName());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder{
        TextView fileText;
    }

}
