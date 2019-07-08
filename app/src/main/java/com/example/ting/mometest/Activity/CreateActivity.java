package com.example.ting.mometest.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ting.mometest.Dialog.EditDialog;
import com.example.ting.mometest.Dialog.MyOnClickListener;
import com.example.ting.mometest.Manager.AlarmReceiver;
import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Model.Date;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.StringUtil;
import com.example.ting.mometest.View.MsgToast;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 2019.01.13
 * 新建备忘录
 */

public class CreateActivity extends AppCompatActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    //题目
    private EditText title;
    //内容
    private EditText mEditor;
    //创建日期
    private Date date;
    //是否提醒
    private int is_remind = 0;
    //重要与否
    private int is_important = 0;
    //日期视图
    private TextView date_view;
    //level
    private int level;
    //新建的文件夹
    private String currentFolderName;
    //模式
    private boolean model; // (false 新建模式   true 编辑模式)
    //编辑的Note
    private Note edit_Note;

    private DatePickerDialog dialogDate;
    private TimePickerDialog dialogTime;
    private Date myDate;
    private Integer year;
    private Integer month;
    private Integer dayOfMonth;
    private Integer hour;
    private Integer minute;
    private boolean timeSetTag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        TextView  model_title = (TextView) findViewById(R.id.title_toolbar);
        Intent intent = this.getIntent();

        //是否带有文件夹名（已存在的Note是带有FolderName的，无需传递文件夹名-->编辑）
        currentFolderName = intent.getStringExtra("currentFolderName");
        timeSetTag = false;
        if(currentFolderName == null){ //编辑模式
            model_title.setText("编辑备忘录");
            model = true;//更改状态
            edit_Note = (Note) intent.getSerializableExtra("note");  //获取编辑的note
            currentFolderName = edit_Note.getFolderName();
            is_important = edit_Note.getIs_important();
            is_remind = edit_Note.getIs_remind();
            level = edit_Note.getLevel();
            if(edit_Note.getRemindDate()!=null){
                Date edit_remind_date = edit_Note.getRemindDate();
                year = edit_remind_date.getYear();
                month = edit_remind_date.getMonth();
                dayOfMonth = edit_remind_date.getDay();
                hour = edit_remind_date.getHour();
                minute = edit_remind_date.getMinute();
                timeSetTag = true;
            }
        }else{
            //新建模式
            model_title.setText("新备忘录");
        }

        init_NoteEditor();
        init_view();
        init_Toolbar();

        if(model){//编辑模式
            init_edit();
        }

    }

    /**
     * 编辑初始化
     */
    private void init_edit(){

        title.setText( edit_Note.getName() );             //标题
        mEditor.setText( edit_Note.getText() );           //内容
        date_view.setText( edit_Note.getDate().getDetailDate() );     //时间显示

    }

    /**
     * 视图初始化
     */
    private  void init_view(){

        title = (EditText) findViewById(R.id.title_create);

        date_view = (TextView) findViewById(R.id.date_create);
        date = new Date( );
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

        init_bottom();
    }

    /**
     * 底部栏的初始化
     */
    private void init_bottom(){

        findViewById(R.id.time_bottom_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNoticeDate();
            }
        });

    }

    /**
     * 初始化Editor
     */
    private  void init_NoteEditor() {
        mEditor = (EditText) findViewById(R.id.editor);
        mEditor.setTextSize(14);

    }

    /**
     * toolbar初始
     */
    private  void init_Toolbar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.pic_deleteall);//设置取消图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this,AllNotesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });

        toolbar.inflateMenu(R.menu.menu_create);//设置右上角的填充菜单

        if(model) {//编辑模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(timeSetTag == true)
                        myDate = new Date(year,month,dayOfMonth,hour,minute);
                    NoteManager noteManager = new NoteManager(CreateActivity.this, currentFolderName);
                    Note newNote = new Note(title.getText().toString(), edit_Note.getDate(),
                           mEditor.getText().toString(), currentFolderName, level ,is_important,myDate,is_remind);

                    noteManager.update(edit_Note, newNote);
                    MsgToast.showToast(CreateActivity.this, "已保存");
                    if(timeSetTag )
                        setAlert();
                    finish();
                    return false;
                }
            });

        }else {//新建模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    String titleName = title.getText().toString();
                    if(StringUtil.isEmpty(titleName)){
                        titleName="未命名";
                    }
                    Note create_note = new Note(titleName, date,
                            mEditor.getText().toString(),
                            currentFolderName, level,is_important,myDate,is_remind);

                    NoteManager noteManager = new NoteManager(CreateActivity.this, currentFolderName);
                    noteManager.add(create_note);
                    hideOrOpenKeyBoard();
                    if(timeSetTag)
                        setAlert();
                    Intent intent = new Intent(CreateActivity.this,AllNotesActivity.class);
                    startActivity(intent);
                    return false;
                }
            });
        }
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
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.setYesListener(new MyOnClickListener() {
                    @Override
                    public void onClick() {
                        is_important = dialog.getIs_Important();    //是否重要
                        int is = dialog.getIs_remind() ;           //是否提醒
                        dialog.dismiss();
                        if(is == 1 && !timeSetTag){
                            setNoticeDate();        //选择时间
                            if(timeSetTag){         //已选择时间
                                is_remind = is;
                            }
                        }
                        else is_remind = is;
                    }
                });
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
                sb.append("红色");
                break;
            case R.id.btn_blue:
                level = Note.BLU_LEVEL;
                sb.append("蓝色");
                break;
            case R.id.btn_purple:
                level = Note.PUR_LEVEL;
                sb.append("紫色");
                break;
            case R.id.btn_green:
                level = Note.GRE_LEVEL;
                sb.append("绿色");
                break;
        }
        MsgToast.showToast(this, sb.toString());
    }

    /**
     * 键盘的显示和隐藏
     */
    private void hideOrOpenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
     *  设置提醒时间函数
     */
    void setNoticeDate(){
        if(timeSetTag){
            dialogDate = new DatePickerDialog(this,android.app.AlertDialog.THEME_HOLO_LIGHT,this,year,month-1,dayOfMonth);
        }else {
            Calendar calendar=Calendar.getInstance();
            dialogDate = new DatePickerDialog(this,
                    android.app.AlertDialog.THEME_HOLO_LIGHT,this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        dialogDate.setTitle("请选择日期");
        dialogDate.setCanceledOnTouchOutside(false);
        dialogDate.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month+1;
        this.dayOfMonth = dayOfMonth;

        int paramHour = 8;
        int paramMinute = 0;

        if(timeSetTag){
            dialogTime = new TimePickerDialog(this,
                    android.app.AlertDialog.THEME_HOLO_LIGHT,this,hour,minute,true);
        }else{
            dialogTime = new TimePickerDialog(this,
                    android.app.AlertDialog.THEME_HOLO_LIGHT,this,
                    paramHour,
                    paramMinute,
                    true);
        }
        dialogTime.setTitle("请选择时间");
        dialogTime.setCanceledOnTouchOutside(false);
        dialogTime.show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        timeSetTag = true;
        myDate = new Date(year,month,dayOfMonth,hour,minute);

    }

    public void setAlert(){

        // 创建将执行广播的PendingIntent
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Title",title.getText().toString());
        intent.putExtra("Time",myDate.getHour()+":"+myDate.getMinute());
        PendingIntent pi= PendingIntent.getBroadcast(CreateActivity.this, 0, intent, 0);

        Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pi);

        Toast.makeText(CreateActivity.this, "提醒时间设置成功！",
                Toast.LENGTH_LONG).show();
    }
}