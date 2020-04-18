package com.example.qqlistviewdemo;

/**
 * 点击事件的回调接口
 */
public interface OnItemActionListener {
    void OnItemClick(int position);

    void OnItemTop(int position);

    void OnItemRead(int position);

    void OnItemDelete(int position);
}
