package com.example.ting.mometest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //init();
        new  Thread(this).start();
    }

    public void run(){
        try{
            /**
             * 延迟1秒时间
             */
            Thread.sleep(3000);
            SharedPreferences preferences= getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
            int count = preferences.getInt("count", 0); // 取出数据

            /**
             *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
             */
            if (count == 0) {
                Intent intent1 = new Intent();
                intent1.setClass(WelcomeActivity.this, GuideActivity.class);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                startActivity(intent1);
            } else {
                Intent intent2 = new Intent();
                intent2.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
            finish();

            //实例化Editor对象
            SharedPreferences.Editor editor = preferences.edit();
            //存入数据
            editor.putInt("count", 1); // 存入数据
            //提交修改
            editor.commit();
        } catch (InterruptedException e) {

        }


    }

    /**
     * 初始化
     */
    private void init(){
        //当计时结束,跳转至主界面

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
                //checkHavePassWord();
            }

        }, 2500);
    }

    /*
    /**

     * 检查是否已设置密码
       private  void checkHavePassWord(){

        Intent intent1 = new Intent(WelcomeActivity.this, SecurityActivity.class);
        //MODEL_VERIFY 验证密码模式
        intent1.putExtra("model",SecurityActivity.MODEL_VERIFY);
        startActivity(intent1);
    }
     */
}
