package com.example.administrator.myapplicationtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 15-11-9.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listitems;
    private LayoutInflater listContainerInflater;

    public final class ListItemView{
        public TextView Name;
    }

    public ListViewAdapter(Context context,List<Map<String, Object>> listitems){
        this.context=context;
        //创建视图容器并设置上下文
        listContainerInflater=LayoutInflater.from(context);
        this.listitems=listitems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listitems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListItemView listItemView = null;
        if(convertView == null){
            listItemView = new ListItemView();
            convertView = listContainerInflater.inflate(R.layout.list_item,null);
            listItemView.Name = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(listItemView);
        }else{
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.Name.setText((CharSequence) listitems.get(position).get("text"));
        return convertView;
    }
}
