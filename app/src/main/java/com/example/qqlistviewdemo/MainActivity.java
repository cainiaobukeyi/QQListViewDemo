package com.example.qqlistviewdemo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CustomListView mCustomListView;
    private List<String> mNameDatas;
    private List<QQBean> qqDataBeans;
    private CustomListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomListView = findViewById(R.id.listView);
        mNameDatas = new ArrayList<>(Arrays.asList("zhangsan", "lisi", "wangwu", "zhaoliu", "tianqi", "heiba", "qinjiu", "hushi", "ashiyi", "bshier", "cshisan", "dshisi", "eshiwu", "fshiliu", "gshiqi", "hshiba", "ishijiu", "jershi"));
        adapter = new CustomListViewAdapter(this);
        mCustomListView.setActionListener(new OnItemActionListener() {
            @Override
            public void OnItemClick(int position) {
                Toast.makeText(MainActivity.this, "点击了第" + position + "条item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemTop(int position) {
                QQBean qqBean = qqDataBeans.get(position);
                if (qqBean.isTop()) {
                    Collections.swap(qqDataBeans, position, qqBean.getOldPosition());
                } else {
                    Collections.swap(qqDataBeans, position, 0);
                }
                qqBean.setTop(!qqBean.isTop());
                adapter.setData(qqDataBeans);
            }

            @Override
            public void OnItemRead(int position) {
                QQBean qqBean = qqDataBeans.get(position);
                if (qqBean.isRead()) {
                    //TODO 取消显示消息条数的图标，此处不做赘述
                    Toast.makeText(MainActivity.this, "标为未读", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO 显示消息条数的图标，此处不做赘述
                    Toast.makeText(MainActivity.this, "标为已读", Toast.LENGTH_SHORT).show();
                }
                qqBean.setRead(!qqBean.isRead());
                adapter.setData(qqDataBeans);
            }

            @Override
            public void OnItemDelete(int position) {
                qqDataBeans.remove(position);
                //更改oldPosition的值
                for (int i = 0; i < qqDataBeans.size(); i++) {
                    qqDataBeans.get(i).setOldPosition(i);
                }
                adapter.setData(qqDataBeans);
            }
        });
        mCustomListView.setAdapter(adapter);
        //造一些假数据
        qqDataBeans = new ArrayList<>();
        QQBean bean = null;
        for (int i = 0; i < mNameDatas.size(); i++) {
            bean = new QQBean();
            bean.setName(mNameDatas.get(i));
            bean.setContent("消息内容" + i);
            if (i == 0) {
                bean.setDate("2020-03-31" + (i < 10 && i > 0 ? "0" + i : ""));
            } else {
                bean.setDate("2020-04-" + (i < 10 ? "0" + i : i));
            }
            if (i % 3 == 0) {
                bean.setUserType(CustomListViewAdapter.NORMAL_USER);
            }
            if (i % 3 == 1) {
                bean.setUserType(CustomListViewAdapter.GROUP_USER);
            }
            if (i % 3 == 2) {
                bean.setUserType(CustomListViewAdapter.COMPANY_USER);
            }
            bean.setOldPosition(i);
            bean.setTop(false);
            bean.setDelete(false);
            bean.setRead(false);
            qqDataBeans.add(bean);
        }
        mCustomListView.setData(qqDataBeans);
        adapter.setData(qqDataBeans);
    }
}
