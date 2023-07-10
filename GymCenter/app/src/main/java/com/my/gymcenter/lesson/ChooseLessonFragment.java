package com.my.gymcenter.lesson;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.my.gymcenter.R;
import com.my.gymcenter.adapter.CourseListAdapter;
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

import okhttp3.Call;

public class ChooseLessonFragment extends Fragment {
    private Context mContext;
    private CourseListAdapter adapter;
    private List<Course> mList;
    private com.yanzhenjie.recyclerview.SwipeRecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.list_lesson_with_menu, container,false);
        mContext = getActivity();
        mRecyclerView= v.findViewById(R.id.listcourse2);
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new CourseListAdapter(getActivity(), null);
        mRecyclerView.setAdapter(adapter);
        return v;
    }

    private final OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    delete(mList.get(position).getCourseId());
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
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
        }
    };

    private void delete(int courseId) {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Depend?method=delete";
        OkHttpUtils
                .post()
                .url(url)
                .id(1)
                .addParams("userId", SharedPreferencesUtils.getUserInfo(mContext).getUserId() + "")
                .addParams("fitnessId", courseId + "")
                .build()
                .execute(new MyStringCallback());
    }

    private void reLoadNews() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "Depend?method=mydepend2";
        OkHttpUtils
                .post()
                .url(url)
                .id(2)
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
                        Toast.makeText(mContext, "删除课程成功！", Toast.LENGTH_SHORT).show();
                        reLoadNews();
                    } catch (JsonSyntaxException e) {
                        Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "网络链接出错！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reLoadNews();
    }
}