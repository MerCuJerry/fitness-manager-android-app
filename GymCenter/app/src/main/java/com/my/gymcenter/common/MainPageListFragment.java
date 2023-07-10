package com.my.gymcenter.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.CourseListAdapter;
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.entity.Notice;
import com.my.gymcenter.coach.SelectCoachActivity;
import com.my.gymcenter.lesson.LessonsStudentActivity;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

public class MainPageListFragment extends Fragment {
    private Context mContext;
    private CourseListAdapter adapter;
    private List<Course> mList;
    private RecyclerView mRecyclerView;
    private TextView content;
    Intent intent=new Intent();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.list_lesson_with_notice,container,false);
        mContext=getActivity();
        content=v.findViewById(R.id.add_news_et_share_content);
        mRecyclerView= v.findViewById(R.id.listcourse);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new CourseListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((parent,position) -> {
            if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).getRole(), "user")){
                intent.setClass(mContext, SelectCoachActivity.class);
                intent.putExtra("lessonId",mList.get(position).getCourseId());
                startActivity(intent);
            }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).getRole(), "teacher")){
                intent.setClass(mContext, LessonsStudentActivity.class);
                intent.putExtra("CourseId",mList.get(position).getCourseId());
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        found();
        reLoadNews();
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
    private void reLoadNews() {
        if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).getRole(), "user")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=courselist";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(2)
                    .build()
                    .execute(new MyStringCallback());
        }else if(Objects.equals(SharedPreferencesUtils.getUserInfo(mContext).getRole(), "teacher")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=mydepend";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(2)
                    .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId() + "")
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
                    Notice notice=gson.fromJson(response, Notice.class);
                    if(notice!=null){
                        content.setText(notice.getNotice());
                    }
                    break;
                case 2:
                    try {
                        Type type = new TypeToken<ArrayList<Course>>() {
                        }.getType();
                        mList = gson.fromJson(response, type);
                        mRecyclerView.setItemViewCacheSize(mList.size());
                        adapter.updateData(mList);
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        }

        @Override
        public void onError(Call arg0, Exception arg1, int arg2) {
            Toast.makeText(mContext, "网络连接出错", Toast.LENGTH_SHORT).show();
        }
    }
}
