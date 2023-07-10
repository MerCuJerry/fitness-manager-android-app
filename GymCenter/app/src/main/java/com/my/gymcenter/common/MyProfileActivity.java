package com.my.gymcenter.common;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.my.gymcenter.R;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText password;
    private EditText name;
    private EditText phonenum;
    private EditText data;
    private EditText weight2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        findViewById();
        initView();
        found();
    }
    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        data=findViewById(R.id.data);
        password =findViewById(R.id.new_my_password);
        name = findViewById(R.id.new_my_name);
        phonenum = findViewById(R.id.new_my_tele);
        weight2=findViewById(R.id.weight);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update();
        }
    }
    private void found() {
        String username = SharedPreferencesUtils.getUserInfo(mContext).getUsername();
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=found";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .id(2)
                .build()
                .execute(new MyStringCallback());
    }
    private void update() {
        String username =SharedPreferencesUtils.getUserInfo(mContext).getUsername();
        String password = this.password.getText().toString().trim();
        String phone = phonenum.getText().toString().trim();
        String name = this.name.getText().toString().trim();
        String weight=weight2.getText().toString().trim();
        String data2=data.getText().toString();
        if ( TextUtils.isEmpty(password)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(name) ||TextUtils.isEmpty(weight)||TextUtils.isEmpty(data2) ) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=update22";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .addParams("phone", phone)
                .addParams("name", name)
                .addParams("weight", weight)
                .addParams("data", data2)
                .id(1)
                .build()
                .execute(new MyStringCallback());
    }


    public class MyStringCallback extends StringCallback {
        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            User user;
            switch (id) {
                case 1:
                    try {
                        user = gson.fromJson(response, User.class);
                        Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    try {
                        user = gson.fromJson(response, User.class);
                        password.setText(user.getPassword());
                        phonenum.setText(user.getPhone());
                        name.setText(user.getName());
                        weight2.setText(user.getWeight()+"");
                        data.setText(user.getData());
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }
}
