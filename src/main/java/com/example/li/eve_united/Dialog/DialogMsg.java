package com.example.li.eve_united.Dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Li on 2015/11/15.
 */
public class DialogMsg {
    public static ProgressDialog dialogMsg(Context mContext,String title,String msg){
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        //设置不能被用户取消
        progressDialog.setCancelable(false);
        //设置最大值
        progressDialog.setMax(100);
        //设置个内置样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //循环显示滚动条
        progressDialog.setIndeterminate(false);
        return progressDialog;
    }
}
