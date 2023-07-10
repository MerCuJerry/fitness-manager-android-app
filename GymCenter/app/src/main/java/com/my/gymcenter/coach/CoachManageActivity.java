package com.my.gymcenter.coach;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.lesson.LessonManageActivity;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CoachManageActivity extends AppCompatActivity{
    private Context mContext;
    private List<User> mList;
    private RecyclerView mRecyclerView;
    private UserListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user);
        mRecyclerView = findViewById(R.id.listuser);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new UserListAdapter(this,null);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position) -> {
            if(getIntent().getBooleanExtra("select",false)){
                addCoach(mList.get(position).getUserId()+"",getIntent().getIntExtra("lessonId",0)+"");
            }else{
                Intent intent = new Intent(mContext, LessonManageActivity.class);
                intent.putExtra("introPoint","coachManage");
                intent.putExtra("coachId",mList.get(position).getUserId());
                startActivity(intent);
            }
        });

        mContext=this;
    }

    @Override
    public void onResume(){
        super.onResume();
        reLoadNews();
    }

    private void reLoadNews() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=userlist3";
        OkHttpUtils
                .post()
                .url(url)
                .id(1)
                .build()
                .execute(new MyStringCallback());
    }
    private void addCoach(String userId,String lessonId){
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=add";
        OkHttpUtils
                .post()
                .url(url)
                .id(2)
                .addParams("userId",userId)
                .addParams("fitnessId",lessonId)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            switch (id){
                case 1:
                    try {
                        Type type = new TypeToken<ArrayList<User>>() {
                        }.getType();
                        mList = gson.fromJson(response, type);
                        mRecyclerView.setItemViewCacheSize(mList.size());
                        adapter.updateData(mList);
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    try {
                        Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                        finish();
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
