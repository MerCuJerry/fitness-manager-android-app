package com.my.gymcenter.user;

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
import com.my.gymcenter.adapter.UserListAdapter;
import com.my.gymcenter.entity.User;
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

public class UserManageActivity extends AppCompatActivity{
    private Context mContext;
    private List<User> mList;
    private SwipeRecyclerView mRecyclerView;
    UserListAdapter adapter = new UserListAdapter(this, null);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user_with_menu);
        mRecyclerView = findViewById(R.id.listuser2);
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
        reLoad();
    }

    private final OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuPosition == 0) {
                    delete(mList.get(position).getUsername());
                } else if (menuPosition == 1) {
                    Intent intent=new Intent(mContext, UserModifyActivity.class);
                    intent.putExtra("userName",mList.get(position).getUsername());
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

            SwipeMenuItem modifyItem = new SwipeMenuItem(mContext).setBackground(
                            R.drawable.selector_yellow)
                    .setText("修改")
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(modifyItem);
        }
    };

    private void reLoad() {
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=userlist";
        OkHttpUtils
                .post()
                .url(url)
                .id(1)
                .build()
                .execute(new MyStringCallback());
    }

    private void delete(String username) {
        if (username.equals("admin")) {
            Toast.makeText(mContext, "不可以删除admin管理员", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = SharedPreferencesUtils.getServerUrl(mContext) + "User?method=delete";
        OkHttpUtils
                .post()
                .url(url)
                .id(2)
                .addParams("username", username)
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
                        mList = gson.fromJson(response, type);
                        mRecyclerView.setItemViewCacheSize(mList.size());
                        adapter.updateData(mList);
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    try {
                        Toast.makeText(mContext, "删除用户成功！", Toast.LENGTH_SHORT).show();
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
