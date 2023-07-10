package com.my.gymcenter.lesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.CourseListAdapter;
import com.my.gymcenter.coach.CoachManageActivity;
import com.my.gymcenter.entity.Course;
import com.my.gymcenter.utils.SharedPreferencesUtils;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;

public class LessonManageActivity extends AppCompatActivity{
    private Context mContext;
    private String introPoint;
    private SwipeRecyclerView mRecyclerView;
    private List<Course> mList;
    CourseListAdapter adapter = new CourseListAdapter(this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_lesson_with_menu);
        introPoint = getIntent().getStringExtra("introPoint");
        mRecyclerView=findViewById(R.id.listcourse2);
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        mContext = this;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        introPoint = getIntent().getStringExtra("introPoint");
        reLoad();
    }

    private final OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection();
            int menuPosition = menuBridge.getPosition();
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    delete(mList.get(position));
                } else if (menuPosition == 1) {
                    Intent intent=new Intent(mContext, LessonModifyActivity.class);
                    intent.putExtra("courseName",mList.get(position).getCoursename());
                    startActivity(intent);
                } else if (menuPosition == 2) {
                    Intent intent=new Intent(mContext, CoachManageActivity.class);
                    intent.putExtra("select",true);
                    intent.putExtra("lessonId",mList.get(position).getCourseId());
                    startActivity(intent);
                }
            }
        }
    };

    private final SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize((R.dimen.dp_70));
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext).setBackground(
                            R.drawable.selector_red)
                    .setText("删除")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);
            if(Objects.equals(introPoint, "admin")) {
                SwipeMenuItem modifyItem = new SwipeMenuItem(mContext).setBackground(
                                R.drawable.selector_yellow)
                        .setText("修改")
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(modifyItem);
                SwipeMenuItem addCoach = new SwipeMenuItem(mContext).setBackground(
                                R.drawable.selector_blue)
                        .setText("添加教练")
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addCoach);
            }
        }
    };

    private void reLoad() {
        if(Objects.equals(introPoint, "admin")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=courselist";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(1)
                    .build()
                    .execute(new MyStringCallback());
        }else if (Objects.equals(introPoint, "teacher")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=mydepend";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(1)
                    .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId() + "")
                    .build()
                    .execute(new MyStringCallback());
        }else if (Objects.equals(introPoint, "coachManage")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=mydepend";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(1)
                    .addParams("userId", getIntent().getIntExtra("coachId",0) + "")
                    .build()
                    .execute(new MyStringCallback());
        }
    }

    private void delete(Course lesson) {
        if(Objects.equals(introPoint, "admin")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "Course?method=delete";
            OkHttpUtils
                    .post()
                    .url(url)
                    .id(2)
                    .addParams("mingcheng", lesson.getCoursename())
                    .build()
                    .execute(new MyStringCallback());
        }else if (Objects.equals(introPoint, "teacher")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=delete";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("fitnessId", lesson.getCourseId()+"")
                    .addParams("userId",SharedPreferencesUtils.getUserInfo(mContext).getUserId()+"")
                    .id(2)
                    .build()
                    .execute(new MyStringCallback());
        }else if (Objects.equals(introPoint, "coachManage")){
            String url = SharedPreferencesUtils.getServerUrl(mContext) + "CoachFitness?method=delete";
            OkHttpUtils
                    .post()
                    .url(url)
                    .addParams("fitnessId", lesson.getCourseId()+"")
                    .addParams("userId",getIntent().getIntExtra("coachId",0) + "")
                    .id(2)
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
                        Type type = new TypeToken<ArrayList<Course>>() {
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
                        Toast.makeText(mContext, "删除项目成功！", Toast.LENGTH_SHORT).show();
                        reLoad();
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
