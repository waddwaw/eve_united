
package com.example.li.eve_united.activity;



import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.li.eve_united.R;
import com.example.li.eve_united.bean.UserBean;
import com.example.li.eve_united.config;
import com.example.li.eve_united.utils.HttpUtils;
import com.example.li.eve_united.utils.MD5Encoder;
import com.example.li.eve_united.utils.NetUtils;
import com.example.li.eve_united.utils.SPUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements HttpUtils.CallBack{

    private TextView Reg_text;
    private Button Login_bt;
    private View loging;
    private EditText et_user,et_pwd;
    private boolean isMD5=false;//判断是否需要Md5转换
    private String URL = config.URL;
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initViews();
        //恢复上次成功登陆的用户名密码
        String userJson = (String) SPUtils.get(LoginActivity.this,"LoUser","null");
        if(userJson.equals("null")){
            isMD5=true;
        }else {
            UserBean user = new Gson().fromJson(userJson, UserBean.class);
            et_user.setText(user.getUser());
            et_pwd.setText(user.getPwd());
        }
    }

    private void initViews() {
        Reg_text= (TextView) findViewById(R.id.id_login_reg_text);
        loging =findViewById(R.id.id_layout_loading_login);
        et_user = (EditText) findViewById(R.id.id_login_et_user);
        et_pwd = (EditText) findViewById(R.id.id_login_et_pwd);
        et_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pwd.setText("");
                isMD5=true;
            }
        });
        Reg_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisteredActivity.class);
                startActivity(in);
            }
        });
        Login_bt= (Button) findViewById(R.id.id_login_bt);
        Login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd ="";
                if (et_user.getText().toString().equals("")) {
                    showAlertDialog("温馨提示", "用户名密码不能为空");
                    return;
                }
                if (et_pwd.getText().toString().equals("")) {
                    showAlertDialog("温馨提示", "用户名密码不能为空");
                    return;
                }
                if(isMD5){
                    try {
                        pwd= MD5Encoder.encode(et_pwd.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertDialog("温馨提示", "密码不能是非字符");
                        return;
                    }
                }else {
                    pwd = et_pwd.getText().toString();
                }
                URL=config.URL;
                try {
                    URL += "checkuser?user=" + URLEncoder.encode(et_user.getText().toString(), "utf-8") +  "&pwd=" + pwd;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Log.e("TAG", URL);
                HttpUtils.doGetAsyn(URL, LoginActivity.this);
                loging.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onRequestComplete(final String result) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (result == null) {
                    showAlertDialog("温馨提示", "网络错误");
                    loging.setVisibility(View.GONE);
                    return;
                }
                if(result.contains("failure")){
                    showAlertDialog("温馨提示", "用户名或密码错误");
                    loging.setVisibility(View.GONE);
                    return;
                }
                UserBean user =new Gson().fromJson(result,UserBean.class);
                if(!user.getUser().equals("")) {
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    // user.setPwd(et_pwd.getText().toString());//保持没有转为MD5的明文密码
                    SPUtils.put(LoginActivity.this,"LoUser",result);
                    //Log.e("TAG",result);
                    startActivity(in);
                    loging.setVisibility(View.GONE);
                    finish();
                }
            }
        });
    }
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
