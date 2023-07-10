package com.my.gymcenter.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class UserModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private TextView newaccount;
    private EditText newpassword;
    private TextView permission;
    private EditText newname;
    private EditText newtele;
    private EditText newweight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modify);
        found(getIntent().getStringExtra("userName"));
        findViewById();
        initView();
    }
    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        newaccount =findViewById(R.id.user_account);
        newpassword =findViewById(R.id.new_password);
        permission = findViewById(R.id.permission);
        newname = findViewById(R.id.new_name);
        newtele = findViewById(R.id.new_tele);
        newweight =findViewById(R.id.weight);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update();
        }
    }

    private void found(String username) {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=found";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .id(1)
                .build()
                .execute(new MyStringCallback());
    }
    public class MyStringCallback extends StringCallback {

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            User user = null;
            try {
                user = gson.fromJson(response, User.class);
            } catch (JsonSyntaxException e) {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            }
            switch (id) {
                case 1:
                    newaccount.setText(user.getUsername());
                    newpassword.setText(user.getPassword());
                    switch (user.getRole()){
                        case "admin":
                            permission.setText("管理员"); break;
                        case "user":
                            permission.setText("会员");break;
                        case "teacher":
                            permission.setText("教练");break;
                    }
                    newtele.setText(user.getPhone());
                    newname.setText(user.getName());
                    newweight.setText(user.getWeight()+"");
                    break;
                case 2:
                    Toast.makeText(mContext, "修改用户成功！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }



        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }


    private void update() {
        String username = newaccount.getText().toString().trim();
        String password = newpassword.getText().toString().trim();
        String phone = newtele.getText().toString().trim();
        String name = newname.getText().toString().trim();
        String weight= newweight.getText().toString().trim();
        if ( TextUtils.isEmpty(password)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(name)||TextUtils.isEmpty(weight)  ) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.equals("admin")) {
            Toast.makeText(mContext, "不可以修改admin管理员", Toast.LENGTH_SHORT).show();
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
                    .id(2)
                    .build()
                    .execute(new MyStringCallback());

    }
}
