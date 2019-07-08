package com.example.ting.mometest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ting.mometest.View.MsgToast;

public class CallActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        init_Call_Button();
        actionbarReset();
        setPermisson();
    }

    public void init_Call_Button(){
        Button imageButton = (Button)findViewById(R.id.call_button);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences= getSharedPreferences("nameCall", 0);
                String nameCall = preferences.getString("nameCall", null); // 取出数据
                String phoneCall = preferences.getString("phoneCall",null);
                if(phoneCall!=null)
                    call(phoneCall);
                else {
                    Intent intent = new Intent(CallActivity.this,SettingActivity.class);
                    startActivity(intent);
                    MsgToast.showToast(CallActivity.this, "请设置联系人");
                    overridePendingTransition(R.anim.in_from_right  , R.anim.out_to_left);
                }

            }
        });
    }

    private void call(String phone){
        Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void actionbarReset(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.back_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setPermisson(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }

    /**
     *
     * @param requestCode
     * @param permissions 请求的权限
     * @param grantResults 请求权限返回的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        finish();
                        Toast.makeText(getBaseContext(), "授权失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}
