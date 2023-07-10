package com.my.gymcenter.coach;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.entity.Depend;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SelectCoachActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private UserListAdapter adapter;
    private List<User> mList;
    private final Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coach);
        findViewById(R.id.without_coach).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.select_list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new UserListAdapter(this,null);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position) -> {
            add(mList.get(position).getUserId());
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.without_coach) {
            add(-1);
        }
    }

    private void reLoadNews() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=coursedepend";
        OkHttpUtils
                .post()
                .url(url)
                .id(2)
                .addParams("fitnessId", getIntent().getIntExtra("lessonId",0)+"")
                .build()
                .execute(new MyStringCallback());
    }

    private void add(int coachId) {
        if(coachId==-1){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "Depend?method=add";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(1)
                    .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId()+"")
                    .addParams("fitnessId",getIntent().getIntExtra("lessonId",0)+"")
                    .build()
                    .execute(new MyStringCallback());
        }else{
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "Depend?method=add3";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(1)
                    .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId()+"")
                    .addParams("fitnessId",getIntent().getIntExtra("lessonId",0)+"")
                    .addParams("coachId",coachId+"")
                    .build()
                    .execute(new MyStringCallback());
        }
    }
    public class MyStringCallback extends StringCallback {
        @Override
        public void onResponse(String response, int id) {
            Gson gson = new Gson();
            switch (id) {
                case 1:
                    try {
                        Depend a = gson.fromJson(response, Depend.class);
                        Toast.makeText(mContext, "添加成功！", Toast.LENGTH_SHORT).show();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
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
            }
        }


        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        reLoadNews();
    }
}
