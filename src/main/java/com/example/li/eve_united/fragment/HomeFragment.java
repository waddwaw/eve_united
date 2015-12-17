package com.example.li.eve_united.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.li.eve_united.Dialog.DialogMsg;
import com.example.li.eve_united.R;
import com.example.li.eve_united.adapter.HomeGridAdapter;
import com.example.li.eve_united.bean.ChatBean;
import com.example.li.eve_united.bean.UserBean;
import com.example.li.eve_united.config;
import com.example.li.eve_united.service.NetService;
import com.example.li.eve_united.service.ReadTypeCtrl;
import com.example.li.eve_united.utils.HttpUtils;
import com.example.li.eve_united.utils.SPUtils;
import com.google.gson.Gson;

import java.lang.annotation.Target;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

/**
 * Created by Li on 2015/11/9.
 */
public class HomeFragment extends Fragment implements HttpUtils.CallBack {
    private FrameLayout home_Frame_binding;//根据状态改变View
    private Button home_bt_binding;//绑定按钮
    private View frameView;
    private Handler handler = new Handler(); //用于更新UI 第一次这么样用Handler
    private GridView homeCtrlGrid;
    private HomeGridAdapter homeGridApapter;
    private EditText  ed_home_number;
    public static final String HOMEFRAGMENT_RECEIVER="com.example.li.eve_united.homefragment";
    private MyReceiver myReceiver;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frameView =View.inflate(getActivity(), R.layout.home_frame_1, null);
        home_bt_binding= (Button) frameView.findViewById(R.id.id_home_bt);
        ed_home_number= (EditText) frameView.findViewById(R.id.id_ed_home_number);
        home_Frame_binding = (FrameLayout) view.findViewById(R.id.id_home_frame);
        String toUser = (String) SPUtils.get(getActivity(), "toUser", "null");
        if(toUser.equals("null")) {
            home_Frame_binding.addView(frameView);
        }else {
            WebView();
        }
        homeCtrlGrid= (GridView) view.findViewById(R.id.id_grid_home_ctrl);
        ed_home_number.setInputType(InputType.TYPE_CLASS_NUMBER);//限定只能输入数字
        //初始化广播
        myReceiver = new MyReceiver();
        IntentFilter intent = new IntentFilter();
        intent.addAction(HOMEFRAGMENT_RECEIVER);
        getActivity().registerReceiver(myReceiver, intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeGridApapter=new HomeGridAdapter(getActivity());
        homeCtrlGrid.setAdapter(homeGridApapter);
        home_bt_binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog= DialogMsg.dialogMsg(getActivity(),"提示","正在请求中……");
                progressDialog.show();
                if(!ed_home_number.getText().toString().equals("")) {
                    HttpUtils.doGetAsyn(config.URL + "bindcheck?num="+ed_home_number.getText().toString(), HomeFragment.this);
                }else {
                    Toast.makeText(getActivity(),"请填写正确的手机号码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 提前书写方法
     */
    private  void WebView(){
        home_Frame_binding.removeAllViews();
        WebView webView = new WebView(getActivity());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(lp);
        home_Frame_binding.addView(webView);
        webView.loadUrl("http://192.168.3.2:8080/ilovepeipei/index.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                // System.out.println("Cookies = " + CookieStr);

                super.onPageFinished(view, url);
            }

            // webview错误页面跳转信息
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            // 使用Webview 打开网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                // 如果不需要其他对点击链接事件的处理返回true，否则返回false
                return true;

            }
        });
    }

    @Override
    public void onRequestComplete(final String result) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(result==null){
                    Toast.makeText(getActivity(),"网络请求失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!result.contains("failure")){
                    Toast.makeText(getActivity(),"请等待对方响应",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NetService.NETSERVICE_RECEIVER);
                    Bundle bundle = new Bundle();
                    String user = (String) SPUtils.get(getActivity(), "LoUser", "null");
                    UserBean userBean = new Gson().fromJson(user, UserBean.class);
                    ChatBean chatBean = new ChatBean("2",result,userBean.getUser());
                    bundle.putSerializable("ChatBean", chatBean);
                    intent.putExtra("ChatBean", bundle);
                    getActivity().sendBroadcast(intent);
                }else {
                    Toast.makeText(getActivity(),"没有找到对方或对方已经绑定",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            home_Frame_binding.removeAllViews();
            WebView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }
}
