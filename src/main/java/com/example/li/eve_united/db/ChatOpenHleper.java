package com.example.li.eve_united.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.li.eve_united.bean.ChatMessageBean;

/**
 * Created by Li on 2015/11/12.
 */
public class ChatOpenHleper extends SQLiteOpenHelper {
    private static final String DB_NAME="chat.db";
    private static final int DB_VERSION=1;
    private static  ChatOpenHleper mHelper;

    private ChatOpenHleper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }


    public static  ChatOpenHleper getInstance(Context context){
        if(mHelper ==null){
            synchronized (SQLiteOpenHelper.class){
                if(mHelper ==null){
                    mHelper= new ChatOpenHleper(context);
                }
            }
        }
        return  mHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="create table "+ ChatMessageBean.TABLE_NAME +"("+
                "_id integer primary key autoincrement , "+
                ChatMessageBean.COLUMN_NAME +" text , "+
                ChatMessageBean.COLUMN_MSG + " text , "+
                ChatMessageBean.COLUMN_TYPE + " integer , "+
                ChatMessageBean.COLUMN_DATE + " text "+
                ")";
        Log.e("BUG",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
