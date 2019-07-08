package com.example.ting.mometest.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.ting.mometest.Activity.R;

/**
 * 编辑对话框
 */
public class ContactDialog extends android.app.Dialog {
    private String name;
    private String phone;
    private Button yes;
    private Button no;
    private Context mContext;
    private EditText nameCall;
    private EditText phoneCall;

    private MyOnClickListener noListener;//取消按钮被点击了的监听器
    private MyOnClickListener yesListener;//确定按钮被点击了的监听器

    /**
     *
     * @param context
     */
    public ContactDialog(Context context,String name,String phone) {
        super(context);
        mContext=context;
        this.name = name;
        this.phone = phone;
    }

    /**
     * 初始化
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact);
        initView();
        initEvent();

    }

    /**
     * 初始化界面
     */
    protected void initView(){

        yes = (Button) findViewById(R.id.yes_dialog);
        no = (Button) findViewById(R.id.no_dialog);

        nameCall =(EditText)findViewById(R.id.name_call);
        phoneCall = (EditText)findViewById(R.id.name_phone);

        if(name!=null)
            nameCall.setText(name);
        if(phone!=null)
            phoneCall.setText(phone);
    }

    public String getName() {
        if(nameCall.getText()!=null)
            return nameCall.getText().toString();
        else
            return "未知";
    }

    public String getPhone() {
        if(phoneCall.getText()!=null)
            return phoneCall.getText().toString();
        else
            return null;
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

}
