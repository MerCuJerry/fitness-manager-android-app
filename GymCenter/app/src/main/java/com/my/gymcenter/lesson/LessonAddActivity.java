package com.my.gymcenter.lesson;

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
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LessonAddActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText lessonName;
    private EditText lessonContent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_add);
        findViewById();
        mContext=this;
    }
    private void findViewById() {
        findViewById(R.id.ConfirmAdd).setOnClickListener(this);
        lessonName =findViewById(R.id.lesson_name);
        lessonContent = findViewById(R.id.lesson_content);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmAdd) {
            register();
        }
    }
    private void register() {
        String lessonname = lessonName.getText().toString().trim();
        String lessoncontent = lessonContent.getText().toString().trim();
        if (TextUtils.isEmpty(lessonname) || TextUtils.isEmpty(lessoncontent) ) {
            Toast.makeText(mContext, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=add";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("mingcheng", lessonname)
                    .addParams("neirong", lessoncontent)
                    .build()
                    .execute(new MyStringCallback());

    }
    public class MyStringCallback extends StringCallback {

        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            try {
                Course check = gson.fromJson(response, Course.class);
                Toast.makeText(mContext, "添加项目成功！", Toast.LENGTH_SHORT).show();
                finish();
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
