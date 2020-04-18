package com.example.qqlistviewdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义ListView的 仿QQ左滑删除列表的adapter
 */
public class CustomListViewAdapter extends BaseAdapter {

    public static final int NORMAL_USER = 0;//普通用户
    public static final int GROUP_USER = 1;//QQ群
    public static final int COMPANY_USER = 2;//企业号

    private Context mContext;
    private List<QQBean> mDatas;

    public CustomListViewAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_custom_listview, null);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);
            holder.btnTop = convertView.findViewById(R.id.btn_top);
            holder.btnRead = convertView.findViewById(R.id.btn_read);
            holder.tvName = convertView.findViewById(R.id.tv_name);
            holder.tvContent = convertView.findViewById(R.id.tv_content);
            holder.tvDate = convertView.findViewById(R.id.tv_date);
            holder.ivIcon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QQBean bean = mDatas.get(position);
        //如果是获取的在线数据，最好判空
        holder.tvName.setText(bean.getName());
        holder.tvContent.setText(bean.getContent());
        holder.tvDate.setText(bean.getDate());
        holder.btnDelete.setText("删除");
        if (bean.isTop()) {
            convertView.setBackgroundColor(Color.GRAY);
            holder.btnTop.setText("取消置顶");
        } else {
            convertView.setBackgroundColor(Color.WHITE);
            holder.btnTop.setText("置顶聊天");
        }
        if (bean.isRead()) {
            holder.btnRead.setText("标为未读");
        } else {
            holder.btnRead.setText("标为已读");
        }
        holder.ivIcon.setImageResource(R.mipmap.ic_launcher_round);
        return convertView;
    }

    public void setData(List<QQBean> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
        ImageView ivIcon;
        Button btnDelete;
        Button btnTop;
        Button btnRead;
    }
}
