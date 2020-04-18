package com.example.qqlistviewdemo;

public class QQBean {

    private boolean isTop;//是否置顶
    private boolean isDelete;//是否删除
    private boolean isRead;//是否置顶
    private int oldPosition;//记录置顶前item的position
    private int messageCount;//记录消息的条数
    private int userType;//用户类型 普通用户 0 显示删除、置顶和已读按钮；QQ群 1 显示删除、置顶按钮；企业号 2 显示删除按钮
    private String name;//内容
    private String imageUrl;//图片
    private String content;//显示的内容
    private String date;//日期

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
