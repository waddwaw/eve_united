package com.example.li.eve_united.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.li.eve_united.R;
import com.example.li.eve_united.bean.AddUserBean;
import com.example.li.eve_united.config;
import com.example.li.eve_united.utils.HttpUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class RegisteredActivity extends AppCompatActivity implements HttpUtils.CallBack {

    private View layout_loading;
    private Button send, cancel;
    private EditText user, pwd, pwd2, nick, number;
    private RadioButton sex_man, sex_woman;
    private String URL = config.URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_set_toolbar);
        toolbar.setTitle("用户注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViews();
        initEvents();
    }

    private void initEvents() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().equals("")) {
                    Toast.makeText(RegisteredActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pwd.getText().toString().equals("")) {
                    Toast.makeText(RegisteredActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pwd2.getText().toString().equals("")) {
                    Toast.makeText(RegisteredActivity.this, "重复密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (nick.getText().toString().equals("")) {
                    Toast.makeText(RegisteredActivity.this, "昵称不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (number.getText().toString().equals("")) {
                    Toast.makeText(RegisteredActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!sex_man.isChecked() && !sex_woman.isChecked()) {
                    Toast.makeText(RegisteredActivity.this, "请选择性别", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pwd.getText().toString().equals(pwd2.getText().toString())) {
                    Toast.makeText(RegisteredActivity.this, "两次你密码输入不一致", Toast.LENGTH_LONG).show();
                    return;
                }
                String sex = null;
                if (sex_man.isChecked()) {
                    sex = "男";
                }
                if (sex_woman.isChecked()) {
                    sex = "女";
                }

                try {
                    URL = config.URL;
                    URL += "adduser?user=" + URLEncoder.encode(user.getText().toString(), "utf-8") + "&nickname=" + URLEncoder.encode(nick.getText().toString(), "utf-8") + "&pwd=" + pwd.getText().toString() + "&number="
                            + number.getText().toString() + "&sex=" + URLEncoder.encode(sex, "utf-8");
                    HttpUtils.doGetAsyn(URL, RegisteredActivity.this);
                    layout_loading.setVisibility(View.VISIBLE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisteredActivity.this, "输入信息有误", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void initViews() {
        layout_loading = findViewById(R.id.id_layout_loading);
        send = (Button) findViewById(R.id.id_ed_reg_send);
        cancel = (Button) findViewById(R.id.id_ed_reg_cancel);
        user = (EditText) findViewById(R.id.id_ed_reg_user);
        pwd = (EditText) findViewById(R.id.id_ed_reg_pwd);
        pwd2 = (EditText) findViewById(R.id.id_ed_reg_pwd2);
        nick = (EditText) findViewById(R.id.id_ed_reg_nick);
        number = (EditText) findViewById(R.id.id_ed_reg_nubr);
        sex_man = (RadioButton) findViewById(R.id.id_ed_reg_radio1);
        sex_woman = (RadioButton) findViewById(R.id.id_ed_reg_radio2);
    }

    /**
     *网络请求回调方法
    * @param result
     */
    @Override
    public void onRequestComplete(final String result) {

        RegisteredActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result == null) {
                    layout_loading.setVisibility(View.GONE);
                    showAlertDialog("温馨提示", "网络错误",false);
                    return;
                }
                AddUserBean addUser = new Gson().fromJson(result,AddUserBean.class);
                if (addUser.getState().equals("success")) {
                    layout_loading.setVisibility(View.GONE);
                    showAlertDialog("温馨提示", "注册成功",true);
                    System.out.println(result);
                }
                if (addUser.getState().equals("existing")) {
                    layout_loading.setVisibility(View.GONE);
                    showAlertDialog("温馨提示", "用户名已存在",false);
                    System.out.println(result);
                }
            }
        });
    }
    public void showAlertDialog(String title, String message, final boolean is) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisteredActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(is){
                    finish();
                }
            }
        });
        builder.show();
    }
}
