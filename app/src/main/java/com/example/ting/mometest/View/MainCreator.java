package com.example.ting.mometest.View;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.example.ting.mometest.Activity.R;

/**
 * 主界面侧滑菜单
 */

public class MainCreator implements com.baoyz.swipemenulistview.SwipeMenuCreator {

    private Context mContext;

    public MainCreator(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 初始化且创建菜单
     * @param menu
     */
    @Override
    public void create(SwipeMenu menu) {

        SwipeMenuItem openItem = new SwipeMenuItem(mContext.getApplicationContext());
        openItem.setBackground(R.color.lightgray);
        openItem.setWidth(dp2px(70));
       // openItem.setTitle("加密");
        openItem.setIcon(R.drawable.pic_lock);
        //openItem.setTitleSize(16);
        openItem.setTitleColor(Color.WHITE);
        menu.addMenuItem(openItem);

        SwipeMenuItem deleteItem = new SwipeMenuItem(mContext.getApplicationContext());
        deleteItem.setBackground(R.color.white);
        deleteItem.setWidth(dp2px(70));
        deleteItem.setIcon(R.drawable.pic_delete);
        deleteItem.setTitleColor(Color.WHITE);
        menu.addMenuItem(deleteItem);

        }

    // 将dp转换为px
    private int dp2px(int value) {
        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）
        // 第二个参数为我们待转的数据的值的大小
        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                mContext.getResources().getDisplayMetrics());
    }


}
