package com.example.ting.mometest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ting.mometest.Dialog.EditDialog;
import com.example.ting.mometest.Dialog.MyOnClickListener;
import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.StringUtil;
import com.example.ting.mometest.View.MsgToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoiceCreateActivity extends AppCompatActivity implements View.OnClickListener{

    // 录音类
    private MediaRecorder mediaRecorder;
    // 以文件的形式保存
    private File recordFile;
    //点击录音按钮的次数（第一次点开始，第二次点结束）
    private int click_count = 0;
    private String currentFolderName;
    //题目
    private EditText title;
    //创建日期
    private com.example.ting.mometest.Model.Date date;
    //是否提醒
    private int is_remind = 0;
    //重要与否
    private int is_important = 0;
    //日期视图
    private TextView date_view;
    //level
    private int level;
    //模式
    private boolean model; // (false 新建模式   true 编辑模式)
    //编辑的Note
    private Note edit_Note;

    private TextView record_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_create);
        init_recorderView();
        actionbarReset();
        setPermisson();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(click_count == 1){
            stopRecording();
        }
    }

    public void init_recorderView(){

        Intent intent = this.getIntent();
        currentFolderName = intent.getStringExtra("currentFolderName");
        if(currentFolderName == null) { //编辑模式
            model = true;//更改状态
            edit_Note = (Note) intent.getSerializableExtra("note");  //获取编辑的note
            currentFolderName = edit_Note.getFolderName();
            is_important = edit_Note.getIs_important();
            is_remind = edit_Note.getIs_remind();
            level = edit_Note.getLevel();
        }
        init_view();
        if(model){//编辑模式
            init_edit();
        }
    }

    /**
     * 视图初始化
     */
    private  void init_view(){

        title = (EditText) findViewById(R.id.title_create);

        date_view = (TextView) findViewById(R.id.date_create);
        date = new com.example.ting.mometest.Model.Date( );
        date_view.setText(date.getDetailDate());

        Button btn_red = (Button) findViewById(R.id.btn_red);
        btn_red.setOnClickListener(this);
        Button btn_orange = (Button) findViewById(R.id.btn_blue);
        btn_orange.setOnClickListener(this);
        Button btn_green = (Button) findViewById(R.id.btn_green);
        btn_green.setOnClickListener(this);
        Button btn_purple = (Button)findViewById(R.id.btn_purple);
        btn_purple.setOnClickListener(this);

        Button btn_setting = (Button)findViewById(R.id.setting_note);
        btn_setting.setOnClickListener(this);

        if(!model){
            Button button1 = (Button)findViewById(R.id.voice_button);
            button1.setOnClickListener(this);
        }
        Button button2 = (Button)findViewById(R.id.btn_save);
        button2.setOnClickListener(this);

        record_time = (TextView)findViewById(R.id.time);
    }
    /**
     * 编辑初始化
     */
    private void init_edit(){

        title.setText( edit_Note.getName() );             //标题
        date_view.setText( edit_Note.getDate().getDetailDate() );     //时间显示
    }

    public void actionbarReset(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.back_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_red:
            case R.id.btn_blue:
            case R.id.btn_purple:
            case R.id.btn_green:
                change_level(v);
                break;
            case R.id.setting_note:
                final EditDialog dialog = new EditDialog(this,is_important,is_remind);
                dialog.show();
                dialog.setYesListener(new MyOnClickListener() {
                    @Override
                    public void onClick() {
                        is_important = dialog.getIs_Important();
                        is_remind = dialog.getIs_remind();
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.voice_button:

                if(click_count == 0){
                    click_count = 1;
                    Button button1 = (Button)findViewById(R.id.voice_button);
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pic_voice2, null);
                    button1.setBackground(drawable);
                    startRecording();
                }else if(click_count ==1){
                    click_count = 0;
                    Button button1 = (Button)findViewById(R.id.voice_button);
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pic_voice1, null);
                    button1.setBackground(drawable);
                    stopRecording();
                }
                break;
            case R.id.btn_save:
                if(model){  //编辑模式
                    NoteManager noteManager = new NoteManager(VoiceCreateActivity.this, currentFolderName);
                    Note newNote = new Note(title.getText().toString(), edit_Note.getDate(), edit_Note.getRecordFile(),currentFolderName,level ,is_important,is_remind);
                    noteManager.update(edit_Note, newNote);
                    MsgToast.showToast(VoiceCreateActivity.this, "已保存");
                    finish();
                }
                else if(recordFile!=null && click_count ==0){   //新建模式
                    String titleName = title.getText().toString();
                    if(StringUtil.isEmpty(titleName)){
                        titleName="未命名";
                    }
                    Note create_note = new Note(titleName, date,recordFile,currentFolderName, level,is_important,is_remind);
                    NoteManager noteManager = new NoteManager(VoiceCreateActivity.this, currentFolderName);
                    noteManager.add(create_note);
                    Intent intent = new Intent(VoiceCreateActivity.this,AllNotesActivity.class);
                    startActivity(intent);
                }
                else {
                    MsgToast.showToast(this,"没有需要保存的录音");
                }
                break;
        }
    }

    /**
     * 改变level
     * @param v
     */
    private void change_level(View v) {

        StringBuilder sb = new StringBuilder(4);
        switch (v.getId()) {
            case R.id.btn_red:
                level = Note.RED_LEVEL;
                sb.append("Red");
                break;
            case R.id.btn_blue:
                level = Note.BLU_LEVEL;
                sb.append("Blue");
                break;
            case R.id.btn_purple:
                level = Note.PUR_LEVEL;
                sb.append("Purple");
                break;
            case R.id.btn_green:
                level = Note.GRE_LEVEL;
                sb.append("Green");
                break;
        }
        MsgToast.showToast(this, sb.toString());
    }

    private int Count = 0;
    final Handler handler=new Handler();
    final Runnable runnable=new Runnable(){
        @Override
        public void run() {
            if(Count == 1800){
                mediaRecorder.stop();
            }
            Count++;
            String str = showTimeCount((long)Count);
            record_time.setText(str);
            handler.postDelayed(this, 1000);//每一秒刷新一次
        }
    };

    private void startRecording() {
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);

        recordFile = new File("/mnt/sdcard",str+"kk.amr");
        mediaRecorder = new MediaRecorder();
        // 判断，若当前文件已存在，则删除
        try{
            if (recordFile.exists()) {
                recordFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());


        try {
            // 准备好开始录音
            mediaRecorder.prepare();

            mediaRecorder.start();
            Count = 0;

            runnable.run();

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //将秒数转换成时间显示格式
    public String showTimeCount(long time) {
        String s = null;
        if(time<=59){
            s ="00:";
            return time<10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time);
        }else{
            return (time%60 <10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time))+":"+(time/60<10 ? s+"0"+String.valueOf(time) : s+String.valueOf(time));
        }
    }

    private void stopRecording() {
        if (recordFile != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            handler.removeCallbacks(runnable);
        }
    }

    public void setPermisson(){

        String[] mPermissions = new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WAKE_LOCK
        };
        /*
        要添加List原因是想判断数组里如果有个别已经授权的权限，就不需要再添加到List中。添加到List中的权限后续将转成数组去申请权限
         */
        List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (int i = 0; i < mPermissions.length; i++) {
                //判断一个权限是否已经允许授权，如果没有授权就会将单个未授权的权限添加到List里面
                if (ContextCompat.checkSelfPermission(this.getApplicationContext(), mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(mPermissions[i]);
                }
            }
            //判断List不是空的，如果有内容就运行获取权限
            if (!permissionsList.isEmpty()){
                String [] permissions = permissionsList.toArray(new String[permissionsList.size()]);
                //执行授权的代码。此处执行后会弹窗授权
                ActivityCompat.requestPermissions(this, permissions, 1);

            }else { //如果是空的说明全部权限都已经授权了，就不授权了,直接执行进入相机或者图库
                return;
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
                        setPermisson();
                    } else {
                        finish();
                        Toast.makeText(getBaseContext(), "授权失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}
