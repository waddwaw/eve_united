package com.example.li.eve_united.db;

import android.content.ContentValues;
import android.content.Context;

import com.example.li.eve_united.bean.ChatMessageBean;

import java.util.Date;

/**
 * Created by Li on 2015/11/12.
 */
public class ChatBiz {
    private Context mContext;
    public ChatBiz(Context context){
        this.mContext=context;
    }
    public void Save(ChatMessageBean chatMessageBean){
        ContentValues values = new ContentValues();
        values.put(ChatMessageBean.COLUMN_NAME,chatMessageBean.getName());
        values.put(ChatMessageBean.COLUMN_MSG,chatMessageBean.getMsg());
        values.put(ChatMessageBean.COLUMN_DATE,new Date().getTime()+"");
        values.put(ChatMessageBean.COLUMN_TYPE,getType(chatMessageBean));
        mContext.getContentResolver().insert(ChatProvider.URI_CHAT_ALL, values);
    }
    private int getType(ChatMessageBean chatMessageBean){
        if(chatMessageBean.getType()==ChatMessageBean.Type.INCOMING){
            return 0;
        }
        return 1;
    }
}
