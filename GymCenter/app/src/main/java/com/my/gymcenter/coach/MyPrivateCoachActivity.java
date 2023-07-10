package com.my.gymcenter.coach;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.entity.User;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

public class MyPrivateCoachActivity extends AppCompatActivity  {
    private Context mContext;
    private RecyclerView mRecyclerView;
    private final UserListAdapter adapter = new UserListAdapter(this,null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user);
        mRecyclerView = findViewById(R.id.listuser);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mContext=this;
    }

    @Override
    public void onResume(){
        super.onResume();
        reLoadNews();
    }
    private void reLoadNews() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Depend?method=findMyCoach";
        OkHttpUtils
                .post()
                .url(url)
                .id(1)
                .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId() + "")
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
                        Type type = new TypeToken<ArrayList<User>>() {
                        }.getType();
                        List<User> mList = gson.fromJson(response, type);
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
}
