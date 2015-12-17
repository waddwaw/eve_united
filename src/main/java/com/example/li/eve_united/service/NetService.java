package com.example.li.eve_united.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.li.eve_united.bean.ChatBean;
import com.example.li.eve_united.config;
import com.example.li.eve_united.utils.SPUtils;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetService extends Service{
    private Socket mSocket;
    private DataInputStream mDateInput;
    private DataOutputStream mDataOutput;
    private boolean flog = true;
    private MyReceiver myReceiver;
    public static String USER = "";
    public static final String NETSERVICE_RECEIVER="com.example.li.eve_united.netservice";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化NET 服务
        myReceiver = new MyReceiver();
        IntentFilter intent = new IntentFilter();
        intent.addAction(NETSERVICE_RECEIVER);
        this.registerReceiver(myReceiver, intent);
        //
        registerNetReceiver();

    }

    private void registerNetReceiver() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        this.registerReceiver(new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobNetInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiNetInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {


                    // 网络出现异常
                } else {
                    // 网络恢复自动连接
                    if(!USER.equals("")) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                init(USER);
                            }
                        }).start();
                    }
                }
            }
        }, filter);
    }

    private void init(String user) {

        try {
            mSocket = new Socket(config.MYSOCKET,config.PROT);
            mDateInput = new DataInputStream(mSocket.getInputStream());
            mDataOutput = new DataOutputStream(mSocket.getOutputStream());
            mDataOutput.writeUTF(user);
            if(!mDateInput.readUTF().contains("success")) {
                //进入此方法表示服务器数据异常 进行return 处理
                ErrMessage("SocketErr");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "Thread");
                    while (flog) {
                        String tmp="";
                        try {
                            tmp=mDateInput.readUTF();
                            Log.e("TAG",tmp);
                            //进行分析接到的数据并处理
                            new ReadTypeCtrl(NetService.this,mDataOutput).getType(new Gson().fromJson(tmp, ChatBean.class));
                        } catch (IOException e) {
                            e.printStackTrace();
                            //当异常时给SocketRead线程发生关闭信号
                            flog =false;
                            ErrMessage("ReadErr");
                        }
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            //当异常时给SocketRead线程发生关闭信号
            flog =false;
            Log.e("TAG", "IOException");
            ErrMessage("SocketErr");
        }
    }

    private void sendMsg(String send){
        if(mDataOutput!=null) {
            try {
                mDataOutput.writeUTF(send);

            } catch (IOException e) {
                e.printStackTrace();
                ErrMessage(send);
            }
        }else {
            ErrMessage(send);
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra("ChatBean");
            ChatBean chatBean = (ChatBean) bundle.getSerializable("ChatBean");
            Log.e("TAG",chatBean.getType());
            new ReadTypeCtrl(getApplicationContext()).getTypeMe(chatBean);
            sendMsg(new Gson().toJson(chatBean));
        }
    }

    private void ErrMessage(String err){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "Service onDestroy");
        //退出服务时给SocketRead线程发生关闭信号
        flog =false;
        this.unregisterReceiver(myReceiver);
        if(mSocket!=null){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mDateInput!=null){
            try {
                mDateInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mDataOutput!=null){
            try {
                mDataOutput.close();
                mDataOutput=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}