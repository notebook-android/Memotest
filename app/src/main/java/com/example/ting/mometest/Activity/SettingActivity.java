package com.example.ting.mometest.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ting.mometest.Dialog.ContactDialog;
import com.example.ting.mometest.Dialog.EditDialog;
import com.example.ting.mometest.Dialog.MyOnClickListener;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        init();
        init_toolbar();
    }

    private SharedPreferences preferences;
    public void init(){
        Button btn_security = (Button)findViewById(R.id.setting_password);
        btn_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,SecurityActivity.class);
                intent.putExtra("model",SecurityActivity.MODEL_EDIT);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right  , R.anim.out_to_left);
            }
        });

        Button btn_bell = (Button)findViewById(R.id.setting_bell);
        btn_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "暂未开启", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_contact = (Button)findViewById(R.id.setting_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences= getSharedPreferences("nameCall", 0);
                String nameCall = preferences.getString("nameCall", null); // 取出数据
                String phoneCall = preferences.getString("phoneCall",null);

                final ContactDialog dialog = new ContactDialog(SettingActivity.this,nameCall,phoneCall);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.setYesListener(new MyOnClickListener() {
                    @Override
                    public void onClick() {

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("nameCall",dialog.getName());
                        editor.putString("phoneCall",dialog.getPhone());
                        //提交修改
                        editor.commit();
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "已保存", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void init_toolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.pic_back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }
        });

        //toolbar上的标题
        TextView mTitle = (TextView) findViewById(R.id.title_toolbar);
        mTitle.setText("设置");
    }
}
