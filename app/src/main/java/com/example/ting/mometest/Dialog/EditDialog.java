package com.example.ting.mometest.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.ting.mometest.Activity.R;

/**
 * 编辑对话框
 */
public class EditDialog extends android.app.Dialog {
    private int is_remind = 0;
    private int is_important = 0;
    private Button yes;
    private Button no;
    private Context mContext;
    private RadioGroup remind;
    private RadioGroup important;

    private MyOnClickListener noListener;//取消按钮被点击了的监听器
    private MyOnClickListener yesListener;//确定按钮被点击了的监听器

    /**
     *
     * @param context
     */
    public EditDialog(Context context,int is_important,int is_remind) {
        super(context);
        mContext=context;
        this.is_important = is_important;
        this.is_remind = is_remind;
    }

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        initView();
        initEvent();

    }

    /**
     * 初始化界面
     */
    protected void initView(){

        yes = (Button) findViewById(R.id.yes_dialog);
        no = (Button) findViewById(R.id.no_dialog);

        important = (RadioGroup) findViewById(R.id.btn_important);
        remind = (RadioGroup) findViewById(R.id.btn_remind);

        if(is_important == 1)
            important.check(R.id.important_yes);
        if(is_remind == 1)
            remind.check(R.id.remind_yes);

        important.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.important_yes:
                        is_important = 1;
                        break;
                    case R.id.important_no:
                        is_important = 0;
                        break;
                }
            }
        });

        remind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case  R.id.remind_yes:
                        is_remind = 1;
                        break;
                    case R.id.remind_no:
                        is_remind = 0;
                        break;
                }
            }
        });
    }

    /**
     * 注册监听事件
     */
    protected  void initEvent() {
        //确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesListener != null) {
                    yesListener.onClick();
                }
            }
        });
        //取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noListener != null) {
                    noListener.onClick();
                }else{
                    dismiss();//若没有新设置事件,则默认关闭Dialog
                }
            }
        });
    }

    /**
     *
     * @param listener
     */
    public void setYesListener (MyOnClickListener listener){
        this.yesListener = listener;
    }

    /**
     *
     * @param listener
     */
    public void setNoListener (MyOnClickListener listener){
        this.noListener = listener;
    }

    /**
     *是否重要
     * @return
     */
    public int getIs_Important() {

        return is_important;
    }

    /**
     * 是否提醒
     * @return
     */
    public int getIs_remind() {
        return is_remind;
    }

}
