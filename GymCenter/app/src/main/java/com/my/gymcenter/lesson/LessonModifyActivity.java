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

public class LessonModifyActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private EditText newLessonName;
    private EditText newLessonContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_modify);
        found(getIntent().getStringExtra("courseName"));
        findViewById();
        mContext=this;
    }

    private void findViewById() {
        findViewById(R.id.ConfirmModify).setOnClickListener(this);
        newLessonName =findViewById(R.id.new_lesson_name);
        newLessonContent = findViewById(R.id.new_lesson_content);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ConfirmModify) {
            update();
        }
    }

    private void found(String lessonId) {
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=found";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("mingcheng", lessonId)
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
                        Course a = gson.fromJson(response, Course.class);
                        newLessonName.setText(a.getCoursename());
                        newLessonContent.setText(a.getCoursedata());
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    try {
                        Toast.makeText(mContext, "修改项目成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                    }
            }
        }



        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }


    private void update() {
        if (TextUtils.isEmpty(newLessonName.getText().toString().trim()) ||
                TextUtils.isEmpty(newLessonContent.getText().toString().trim()) ) {
            Toast.makeText(mContext, "新的信息不能留空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 服务端验证
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=updatewithoutpic";
        OkHttpUtils
                .post()
                .url(url)
                .addParams("oldmingcheng", getIntent().getStringExtra("courseName"))
                .addParams("mingcheng", newLessonName.getText().toString().trim())
                .addParams("kaluli", 0+"")
                .addParams("neirong", newLessonContent.getText().toString().trim())
                .id(2)
                .build()
                .execute(new MyStringCallback());
    }

}
