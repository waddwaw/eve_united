package com.example.li.eve_united.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.li.eve_united.R;
import com.example.li.eve_united.activity.ChatActivity;

/**
 * Created by Li on 2015/11/12.
 */
public class HomeGridAdapter extends BaseAdapter {

    private int[] imgs ={R.drawable.home_forum_icon,R.drawable.home_sleep_icon,R.drawable.home_distance_icon,R.drawable.home_menses_icon,R.drawable.home_todo_icon};
    private String[] ctrls={"私密聊天","我睡了","我的位置","小姨妈","专属日记"};
    private Context mContext;
    public HomeGridAdapter(Context mContext){
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return ctrls.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView=View.inflate(mContext,R.layout.item_home_ctrl,null);
            holder.img = (ImageView) convertView.findViewById(R.id.item_home_ctrl_img);
            holder.text = (TextView) convertView.findViewById(R.id.item_home_ctrl_text);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.item_home_ctrl_layout);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
          holder.img.setBackgroundResource(imgs[position]);
          holder.text.setText(ctrls[position]);
          holder.layout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(position==0){
                      Intent in = new Intent(mContext, ChatActivity.class);
                      mContext.startActivity(in);
                  }
              }
          });

        return convertView;
    }
    class Holder{
         ImageView img;
         TextView text;
         LinearLayout layout;
    }
}
