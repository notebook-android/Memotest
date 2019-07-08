package com.example.ting.mometest.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ting.mometest.Activity.R;
import com.example.ting.mometest.Util.StringUtil;


/**
 * 多选对话框
 */

public class ChooseDialog extends Dialog {

    private TextView title;
    private Button btn_no;
    private Button btn_yes;

    private MyOnClickListener listener_no;
    private MyOnClickListener listener_yes;

    public ChooseDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){

        title = (TextView) findViewById(R.id.title_dialog);
        btn_no = (Button)findViewById(R.id.no_dialog);
        btn_yes = (Button)findViewById(R.id.yes_dialog);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_no != null) {
                    listener_no.onClick();
                }
                dismiss();//若没有新设置事件,则默认关闭Dialog

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener_yes != null) {
                    listener_yes.onClick();
                }

            }
        });
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setListener_no(MyOnClickListener listener_no) {
        this.listener_no = listener_no;
    }

    public void setListener_yes(MyOnClickListener listener_yes) {
        this.listener_yes = listener_yes;
    }

}
