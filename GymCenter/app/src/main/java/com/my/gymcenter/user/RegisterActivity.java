package com.my.gymcenter.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.my.gymcenter.R;
import com.my.gymcenter.common.MainInterfaceActivity;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.AppManager;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText et_username;
    private EditText et_password;
    private EditText et_repassword;
    private EditText et_phone;
    private EditText et_name;
    private EditText weight;
    private String username;
    private String password;
    Intent intent=new Intent();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById();
        initView();
    }

    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.reg_btn_register).setOnClickListener(this);
        et_username=findViewById(R.id.reg_et_username);
        et_password=findViewById(R.id.reg_et_password);
        et_repassword = findViewById(R.id.reg_et_repassword);
        et_phone = findViewById(R.id.reg_et_phone);
        et_name = findViewById(R.id.reg_et_name);
        weight=findViewById(R.id.weight);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_btn_register) {
            register();
        }
    }
    private void register() {
        username =et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        String repassword = et_repassword.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String weight2=weight.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)|| TextUtils.isEmpty(weight2)) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断两次密码
        if (!password.equals(repassword)) {
            Toast.makeText(mContext, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=register";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .addParams("phone", phone)
                .addParams("name", name)
                .addParams("weight", weight2)
                .build()
                .execute(new MyStringCallback());
    }
    public class MyStringCallback extends StringCallback {

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            try {
                User user = gson.fromJson(response, User.class);
                boolean result = SharedPreferencesUtils.saveUserInfo(mContext, user);
                if (result) {
                    Toast.makeText(mContext, "注册成功，已经自动登录！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "用户名密码保存失败", Toast.LENGTH_SHORT).show();
                }
                intent.setClass(getApplicationContext(), MainInterfaceActivity.class);
                startActivity(intent);
                AppManager.getInstance().killAllActivity();
            } catch (JsonSyntaxException e) {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            }
        }



        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }
}
