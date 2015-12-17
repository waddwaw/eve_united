package com.example.li.eve_united.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.li.eve_united.bean.ChatBean;
import com.example.li.eve_united.bean.ChatMessageBean;

/**
 * Created by Li on 2015/11/12.
 */
public class ChatProvider extends ContentProvider {


    private static final String AUTHORITY="com.eve_united.chat.provider.ChatProvider";
    public static  final Uri URI_CHAT_ALL =Uri.parse("content://"+AUTHORITY+"/chat");
    private static UriMatcher matcher ;
    private static final int SMS_ALL = 0;
    //private static final int SMS_ONE = 1;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY,"chat",SMS_ALL);
       // matcher.addURI(AUTHORITY,"chat/#",SMS_ONE);
    }

    private ChatOpenHleper mHelper;
    private SQLiteDatabase mDb;
    @Override
    public boolean onCreate() {
        mHelper=ChatOpenHleper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        mDb = mHelper.getReadableDatabase();
        Cursor c = mDb.query(ChatMessageBean.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        c.setNotificationUri(getContext().getContentResolver(),URI_CHAT_ALL);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = matcher.match(uri);
        if(match != SMS_ALL){
            throw  new IllegalArgumentException("Wrong URI : "+ uri);
        }
        mDb = mHelper.getWritableDatabase();
        long rowId = mDb.insert(ChatMessageBean.TABLE_NAME,null,values);
        if(rowId > 0){
            notifyDataSetChanged();
            Log.e("BUG","ok");
            return ContentUris.withAppendedId(uri, rowId);
        }
        Log.e("BUG","err");
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
    private void notifyDataSetChanged() {
        getContext().getContentResolver().notifyChange(URI_CHAT_ALL,null);
    }
}
