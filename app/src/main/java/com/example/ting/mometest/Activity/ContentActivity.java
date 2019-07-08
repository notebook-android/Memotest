package com.example.ting.mometest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ting.mometest.Dialog.ChooseDialog;
import com.example.ting.mometest.Dialog.MyOnClickListener;
import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.ShareUtil;
import com.example.ting.mometest.Util.StringUtil;
import com.example.ting.mometest.View.MsgToast;


/**
 * 内容
 * 由Main触发的活动
 */
public class ContentActivity extends AppCompatActivity {

    //展示的Note类
    private Note note;
    //Note管理类
    private NoteManager mNoteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //获取mainActivity传来的信息
        Intent intent = this.getIntent();
        note = (Note) intent.getSerializableExtra("note");
        init();
    }

    /**
     * ContentActivity初始化
     */
    private void init() {
        init_toolbar();
        init_view();
        init_bottom();
    }

    /**
     * toolbar的初始化
     */
    private void init_toolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_content);

        mToolbar.setNavigationIcon(R.drawable.pic_back);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {                         //分享

                final ChooseDialog dialog = new ChooseDialog(ContentActivity.this);

                dialog.show();
                dialog.setTitle(ContentActivity.this.getResources().getString(R.string.share));

                dialog.setListener_no(new MyOnClickListener() {
                    @Override
                    public void onClick() {
                        return;
                    }
                });

                dialog.setListener_yes(new MyOnClickListener() {
                    @Override
                    public void onClick() {
                        ShareUtil.shareText(ContentActivity.this,
                                StringUtil.clearHtml(note.getText()));
                    }
                });

                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }
        });

        //toolbar上的标题
        TextView  mTitle = (TextView) findViewById(R.id.title_toolbar);
        mTitle.setText(note.getName());

    }

    /**
     * view的初始化
     */
    private void init_view(){
        //Note管理类
        mNoteManager = new NoteManager(this, note.getFolderName());

        //日期
        final TextView date = (TextView) findViewById(R.id.date_remind);
        if(note.getRemindDate()!=null)
            date.setText(note.getRemindDate().getDetailDate());

        EditText content = (EditText) findViewById(R.id.editor);
        content.setText(note.getText());
        content.setFocusable(false);    //不可编辑状态

       if(note.getIs_remind()==1){
           ImageView img_remind = (ImageView)findViewById(R.id.content_remind);
           img_remind.setVisibility(View.VISIBLE);
       }
       if(note.getIs_important()==1){
           ImageView img_important = (ImageView)findViewById(R.id.content_important);
           img_important.setVisibility(View.VISIBLE);
       }

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
                MsgToast.showToast(ContentActivity.this,
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
