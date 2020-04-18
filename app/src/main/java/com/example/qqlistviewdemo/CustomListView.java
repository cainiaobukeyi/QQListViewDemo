package com.example.qqlistviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Scroller;

import java.util.List;

/**
 * 自定义ListView Item中可以左滑删除
 */
public class CustomListView extends ListView {

    private static final String TAG = "CustomListView";

    private Context mContext;
    /**
     * 设置的最小滑动距离
     */
    private int touchSlop;
    /**
     * 记录是否点击事件的标识
     */
    private boolean isPerformClick;

    /**
     * 上次手指按下点的x坐标
     */
    private int lastX;
    /**
     * 上次手指按下点的y坐标
     */
    private int lastY;

    /**
     * 当前手指按下的item的view
     */
    private View mCurrentView;
    /**
     * 当前手指按压的item的position
     */
    private int currentPosition;

    /**
     * 记录item是否已经开始滑动
     */
    private boolean isStartScroll;

    private Scroller mScroller;
    /**
     * 当前item是否正在滑动打开删除和指定按钮
     */
    private boolean isCurrentItemMoving;

    private boolean isDragging;

    /**
     * 指定按钮和删除按钮的宽度
     */
    private int mMaxLength;
    /**
     * 删除和指定menu的状态：0 关闭；1 将要关闭；2 打开；3 将要打开
     */
    private int menuStatus = 0;
    private static final int MENU_CLOSE = 0;
    private static final int MENU_WILL_CLOSE = 1;
    private static final int MENU_OPEN = 2;
    private static final int MENU_WILL_OPEN = 3;

    private OnItemActionListener mListener;

    /**
     * 显示按钮的个数 0 一个；1 两个； 2 三个。对应用户类型
     */
    private static final int ONE_BUTTON = 0;
    private static final int TWO_BUTTONS = 1;
    private static final int THREE_BUTTONS = 2;

    private List<QQBean> mDatas;

    public CustomListView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView() {
        mScroller = new Scroller(mContext, new LinearInterpolator());
        touchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //判断scroller是否完成滑动
        if (mScroller.computeScrollOffset()) {
            mCurrentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //这个很重要
            invalidate();
            //如果已经完成就改变状态
        } else if (isStartScroll) {
            isStartScroll = false;
            if (menuStatus == MENU_WILL_CLOSE) {
                menuStatus = MENU_CLOSE;
            }
            if (menuStatus == MENU_WILL_OPEN) {
                menuStatus = MENU_OPEN;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();//触摸点到屏幕左边缘的距离 越往右值越大
        int y = (int) ev.getY();//触摸点到view控件上边界的距离 越往下值越大
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (menuStatus == MENU_CLOSE) {//如果删除按钮是关闭状态 正常逻辑 也就是点击事件
                    //根据坐标点所在获取当前item的布局
                    //获取当前按下的点所在item的位置 也就是list中的position
                    currentPosition = pointToPosition(x, y);
                    mCurrentView = getChildAt(currentPosition - getFirstVisiblePosition());//使用当前按下点所在的position-最上面显示的item的position
                    //初始化需要显示的内容
                    Button btnDelete = mCurrentView.findViewById(R.id.btn_delete);
                    Button btnTop = mCurrentView.findViewById(R.id.btn_top);
                    Button btnRead = mCurrentView.findViewById(R.id.btn_read);
                    switch (mDatas.get(currentPosition).getUserType()) {
                        case ONE_BUTTON:
                            mMaxLength = btnDelete.getWidth();//删除按钮的宽度
                            btnTop.setVisibility(GONE);
                            btnRead.setVisibility(GONE);
                            break;
                        case TWO_BUTTONS:
                            mMaxLength = btnDelete.getWidth() + btnTop.getWidth();//删除按钮和置顶按钮的宽度
                            btnTop.setVisibility(VISIBLE);
                            btnRead.setVisibility(GONE);
                            break;
                        case THREE_BUTTONS:
                            mMaxLength = btnDelete.getWidth() + btnTop.getWidth() + btnRead.getWidth();//删除按钮、置顶和标记已读未读按钮的宽度
                            btnTop.setVisibility(VISIBLE);
                            btnRead.setVisibility(VISIBLE);
                            break;
                    }
                    //在不显示删除和置顶按钮的时候设置 删除和置顶按钮的点击事件
                    btnDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCurrentView.scrollTo(0, 0);
                            menuStatus = MENU_CLOSE;
                            if (mListener != null) {
                                mListener.OnItemDelete(currentPosition);
                            }
                        }
                    });
                    btnTop.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCurrentView.scrollTo(0, 0);
                            menuStatus = MENU_CLOSE;
                            if (mListener != null) {
                                mListener.OnItemTop(currentPosition);
                            }
                        }
                    });
                    btnRead.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCurrentView.scrollTo(0, 0);
                            menuStatus = MENU_CLOSE;
                            if (mListener != null) {
                                mListener.OnItemRead(currentPosition);
                            }
                        }
                    });
                } else if (menuStatus == MENU_OPEN) {//如果删除按钮是打开状态 通过动画的方式滑动至删除按钮消失，并且return false
                    //原点（0,0）x轴坐标减去移动后的View视图左上角x轴坐标的值
                    mScroller.startScroll(mCurrentView.getScrollX(), 0, -mMaxLength, 0, 200);
                    //mCurrentView.scrollTo(0, 0);//如果用scrollTo方法那么没有一个动画效果，在用户看来会不友好
                    isStartScroll = true;
                    menuStatus = MENU_WILL_CLOSE;
                    invalidate();
                    return false;
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = lastX - x;
                int dy = lastY - y;
                //如果是上下滚动，就直接忽略这次手势
                if (isDragging) {
                    lastX = x;
                    lastY = y;
                    return super.onTouchEvent(ev);
                }
                int scrollX = mCurrentView.getScrollX();//往左滑该值为正，往右滑该值为负值
                if (Math.abs(dx) > Math.abs(dy)) {//表明是正在横向滑动
                    isCurrentItemMoving = true;

                    if (scrollX + dx <= 0) {//item的布局原始位置，也就是item的布局不能再向右滑动了
                        mCurrentView.scrollTo(0, 0);
                        return false;
                    }
                    if (scrollX + dx >= mMaxLength) {//item的布局已经完全显示出来删除和置顶按钮了，没必要再向左边滑动了
                        mCurrentView.scrollTo(mMaxLength, 0);
                        return false;
                    }
                    mCurrentView.scrollBy(dx, 0);
                } else {
                    isDragging = true;
                }
                if (isCurrentItemMoving) {
                    lastX = x;
                    lastY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isCurrentItemMoving && !isDragging && mListener != null && Math.abs(lastX - x) < touchSlop) {//没有在移动中
                    //满足item的点击事件
                    mListener.OnItemClick(currentPosition);
                }
                int upScrollX = mCurrentView.getScrollX();
                int deltaX = 0;
                //对于滑动过程中的情况需要自动滑动到初始位置或者删除、置顶按钮显示的状态
                if (upScrollX < mMaxLength / 2) {
                    //mCurrentView.scrollTo(0, 0);//如果用scrollTo方法那么没有一个动画效果，在用户看来会不友好
                    deltaX = -upScrollX;
                    menuStatus = MENU_WILL_CLOSE;
                } else {
                    //mCurrentView.scrollTo(mMaxLength, 0);
                    deltaX = mMaxLength - upScrollX;
                    menuStatus = MENU_WILL_OPEN;
                }
                mScroller.startScroll(upScrollX, 0, deltaX, 0, 200);
                isDragging = false;
                isCurrentItemMoving = false;
                isStartScroll = true;
                //刷新界面
                invalidate();
                break;
        }
        lastX = x;
        lastY = y;
        return super.onTouchEvent(ev);
    }

    public void setActionListener(OnItemActionListener listener) {
        this.mListener = listener;
    }

    /**
     * 给左滑出来的布局设置需要显示的内容
     */
    public void setData(List<QQBean> data) {
        mDatas = data;
    }
}
