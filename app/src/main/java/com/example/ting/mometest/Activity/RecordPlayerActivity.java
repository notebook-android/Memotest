package com.example.ting.mometest.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Model.RecordPlayer;
import com.example.ting.mometest.View.MsgToast;

public class RecordPlayerActivity extends AppCompatActivity {

    //展示的Note类
    private Note note;
    //Note管理类
    private NoteManager mNoteManager;
    private boolean model = false;
    private RecordPlayer player;
    // 录音类
    private MediaRecorder mediaRecorder;
    private TextView record_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player);

        Intent intent = this.getIntent();
        note = (Note) intent.getSerializableExtra("note");

        init_toolbar();
        init_view();
        init_bottom();
    }

    public void init_view(){
        //Note管理类
        mNoteManager = new NoteManager(this, note.getFolderName());

        record_time = (TextView)findViewById(R.id.time);

        //日期
        final TextView date = (TextView) findViewById(R.id.date_remind);
        if(note.getDate()!=null)
            date.setText(note.getDate().getDetailDate());

        final Button player_btn = (Button)findViewById(R.id.voice_button);
        player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player = new RecordPlayer(RecordPlayerActivity.this);
                if(!model){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pic_voice2, null);
                    player_btn.setBackground(drawable);
                    playRecording();
                    model = true;
                }else {
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.pic_voice1, null);
                    player_btn.setBackground(drawable);
                    pauseplayer();
                    model = false;
                }
            }
        });

        if(note.getIs_important()==1){
            ImageView img_important = (ImageView)findViewById(R.id.content_important);
            img_important.setVisibility(View.VISIBLE);
        }

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
        mTitle.setText(note.getName());
    }

    private long Count = 0;
    final Handler handler=new Handler();
    final Runnable runnable=new Runnable(){
        @Override
        public void run() {
            if(Count == 0){
                return;
            }
            Count--;
            String str = showTimeCount((long)Count);
            record_time.setText(str);
            handler.postDelayed(this, 1000);//每一秒刷新一次
        }
    };

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

    private void playRecording() {
        player.playRecordFile(note.getRecordFile());
        Count = player.getRecordTime();
        runnable.run();
    }

    private void stopplayer() {
        player.stopPalyer();
    }

    private void pauseplayer() {
        player.pausePalyer();
        handler.removeCallbacks(runnable);
    }

    /**
     * 底部栏的初始化 注册监听
     */
    private void init_bottom() {

        //编辑
        findViewById(R.id.edit_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        //删除
        findViewById(R.id.delete_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoteManager.deleteNote(note);
                //已经移动到最近删除
                MsgToast.showToast(RecordPlayerActivity.this,
                        getResources().getString(R.string.move_recycle));
                finish();
            }
        });
    }

    /**
     * 编辑响应
     */
    private  void edit(){

        Intent intent = new Intent(this,CreateActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        intent.putExtras(bundle);

        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
