package com.example.li.eve_united.service;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.li.eve_united.R;
import com.example.li.eve_united.bean.ChatBean;
import com.example.li.eve_united.bean.ChatMessageBean;
import com.example.li.eve_united.db.ChatBiz;
import com.example.li.eve_united.fragment.HomeFragment;
import com.example.li.eve_united.utils.SPUtils;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Created by Li on 2015/11/12.
 */
public class ReadTypeCtrl {
    private Handler mHandler ;
    private Context mContext;
    private DataOutputStream mDataOutput;
    public ReadTypeCtrl(Context mContext){
        this.mContext=mContext;
        mHandler=new Handler(mContext.getMainLooper());
    }
    public ReadTypeCtrl(Context mContext,DataOutputStream outputStream){
        this.mContext=mContext;
        mDataOutput=outputStream;
        mHandler=new Handler(mContext.getMainLooper());
    }
    public void getType(final ChatBean chatBean){
        if (chatBean.getType().equals("1")){
            ChatMessageBean chatMessageBean = new ChatMessageBean();
            chatMessageBean.setType(ChatMessageBean.Type.INCOMING);
            chatMessageBean.setMsg(chatBean.getContent());
            chatMessageBean.setName(chatBean.getUser());
            new ChatBiz(mContext).Save(chatMessageBean);
        }
        if (chatBean.getType().equals("2")){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ShowDialog(chatBean);
                }
            });
        }
        //对方同意绑定
        if (chatBean.getType().equals("3")){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SPUtils.put(mContext, "toUser", chatBean.getContent());
                    Toast.makeText(mContext,"绑定成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeFragment.HOMEFRAGMENT_RECEIVER);
                    mContext.sendBroadcast(intent);
                }
            });
        }
        //对方拒绝绑定
        if (chatBean.getType().equals("4")){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,"对方拒绝",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void getTypeMe(ChatBean chatBean){
        if (chatBean.getType().equals("1")){
            ChatMessageBean chatMessageBean = new ChatMessageBean();
            chatMessageBean.setType(ChatMessageBean.Type.OUTCOMING);
            chatMessageBean.setMsg(chatBean.getContent());
            chatMessageBean.setName(chatBean.getUser());
            new ChatBiz(mContext).Save(chatMessageBean);
        }
    }

    private void ShowDialog(final ChatBean chatBean){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppTheme_Dialog_Alert);
        builder.setMessage(chatBean.getContent() + ":请求绑定您")
                .setCancelable(false)
                .setPositiveButton("同意",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if(mDataOutput!=null){
                                    try {
                                        ChatBean writBean = new ChatBean("3",chatBean.getContent(),chatBean.getUser());
                                        mDataOutput.writeUTF(new Gson().toJson(writBean));
                                        SPUtils.put(mContext, "toUser", chatBean.getContent());
                                        Intent intent = new Intent(HomeFragment.HOMEFRAGMENT_RECEIVER);
                                        mContext.sendBroadcast(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                .setNegativeButton("拒绝",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                if(mDataOutput!=null){
                                    try {
                                        ChatBean writBean = new ChatBean("4",chatBean.getContent(),chatBean.getUser());
                                        mDataOutput.writeUTF(new Gson().toJson(writBean));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
        AlertDialog alert = builder.create();
        //设置为系统消息否则服务显示  所有页面均可显示视图
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();

    }
}
