package com.my.gymcenter.user;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class UserAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText yonghuzhanghao;
    private EditText yonghumima;
    private RadioGroup radiorole;
    private EditText yonghuxingming;
    private EditText yonghudianhua;
    private EditText weight2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        findViewById();
        initView();
    }
    private void initView() {
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.confirm_add).setOnClickListener(this);
        yonghuzhanghao=findViewById(R.id.new_user_account);
        yonghumima=findViewById(R.id.user_password);
        yonghudianhua=findViewById(R.id.user_tele);
        yonghuxingming=findViewById(R.id.user_name);
        weight2=findViewById(R.id.weight);
        this.radiorole = (RadioGroup) findViewById(R.id.new_user_permission);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm_add) {
            register();
        }
    }


    private void register() {
        String username =yonghuzhanghao.getText().toString().trim();
        String password = yonghumima.getText().toString().trim();
        String name = yonghuxingming.getText().toString().trim();
        String phone = yonghudianhua.getText().toString().trim();
        String weight=weight2.getText().toString().trim();
        String role = "admin";
        if (radiorole.getCheckedRadioButtonId() == R.id.user) {
            role = "user";
        }
        else if (radiorole.getCheckedRadioButtonId() == R.id.teacher)
        {
            role = "teacher";
        }
        //d.判断用户名密码是否为空，不为空请求服务器（省略，默认请求成功）i
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)|| TextUtils.isEmpty(weight)   ) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=add";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("username", username)
                .addParams("password", password)
                .addParams("phone", phone)
                .addParams("name", name)
                .addParams("role", role)
                .addParams("weight", weight)
                .id(1)
                .build()
                .execute(new MyStringCallback());
    }
    public class MyStringCallback extends StringCallback {

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            switch (id) {
                case 1:
                    try {
                        User a = gson.fromJson(response, User.class);
                        Toast.makeText(mContext, "添加用户成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                        return;
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
