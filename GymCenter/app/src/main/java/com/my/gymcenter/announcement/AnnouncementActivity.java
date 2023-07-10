package com.my.gymcenter.announcement;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.my.gymcenter.R;
import com.my.gymcenter.entity.Notice;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class AnnouncementActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private EditText content;
    private Button release;
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_notice_publish);
        findViewById();
        initView();
        found();
    }
    protected void findViewById() {
        this.content = findViewById(R.id.add_news_et_share_content);
        this.release = findViewById(R.id.add_news_btn_release);
    }

    protected void initView() {
        mContext = this;
        this.release.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_news_btn_release) {
            checkInfo();
        }
    }


    private void checkInfo() {
        String contentStr = content.getText().toString();
        if (TextUtils.isEmpty(contentStr)) {
            Toast.makeText(mContext, "请输入通知内容", Toast.LENGTH_SHORT).show();
            content.requestFocus();
            return;
        }
        releaseNews();
    }

    private void found() {
        String url;
        url = SharedPreferencesUtils.getServerUrl(mContext) + "Notice?method=found";
        OkHttpUtils
                .post()
                .url(url)
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
                    Notice notice=gson.fromJson(response, Notice.class);
                    if(notice!=null){
                        content.setHint(notice.getNotice());
                    }
                    break;
                case 2:
                    if (response.contains("success")) {
                        Toast.makeText(mContext, "发布成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, "请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
        }
    }
    private void releaseNews() {
        String contentStr = content.getText().toString();
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Notice?method=update";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(2)
                    .addParams("content", contentStr)
                    .build()
                    .execute(new MyStringCallback());
    }
}
