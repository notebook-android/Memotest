package com.example.ting.mometest.Activity;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ting.mometest.Adapter.MainSwipeAdapter;
import com.example.ting.mometest.Manager.DBManager;
import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Manager.SecurityManager;
import com.example.ting.mometest.Model.MyCalendarBean;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.ComparatorUtil;
import com.example.ting.mometest.View.MainCreator;
import com.example.ting.mometest.View.MainScrollview;
import com.example.ting.mometest.View.MsgToast;
import com.example.ting.mometest.View.SimpleCalendarView;
import com.example.ting.mometest.View.SwipeListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Collections;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    //Note管理类
    private NoteManager noteManager;

    private SwipeListView mListView;
    private List<Note> mData;
    private String currentFolderName = "Notes";

    private MyCalendarBean mBean;
    private SecurityManager sManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        actionbarReset();
        fab_setting();
        sManager= new SecurityManager(this);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SimpleCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDatePickListener(new SimpleCalendarView.OnDatePickListener() {
            @Override
            public void onDatePick(MyCalendarBean bean) {
                mBean = bean.getClone();
                listView_setting();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mBean!=null)
            listView_setting();
    }

    protected void onStart(){
        super.onStart();
        if(mBean!=null)
            listView_setting();
    }

    public void listView_setting(){

        hide_fabMenu();
        mData = new DBManager(this).search(currentFolderName,1,mBean.getYear(),mBean.getMonth(),mBean.getDay());   //返回List<Note>

        Collections.sort(mData,new ComparatorUtil());  //按照时间排序

        MainSwipeAdapter adapter = new MainSwipeAdapter(this,mData);   //添加ListView数据适配器

        noteManager = new NoteManager(this,currentFolderName,mData,adapter);

        MainCreator mainCreator = new MainCreator(this);  //主页面ListView侧滑菜单（移动、删除...）

        mListView = (SwipeListView)findViewById(R.id.calendar_list_view);
        mListView.setMenuCreator(mainCreator);      //设置侧滑菜单
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);   //设置侧滑方向
        mListView.setAdapter(adapter);   //设置数据适配器

        MainScrollview scrollview = (MainScrollview)findViewById(R.id.calendar_scrollView);   //主界面滑动scrollview

        view_Listener();    //点击监听
    }

    private void hide_fabMenu(){
        //关闭fab菜单（折叠）
        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.action_menu_calendar);
        if(menu!=null)
            menu.collapse();
    }

    public void view_Listener(){
        //点击监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(noteManager.checkSecurity(position))
                    checkHavePassWord(position);
                else
                    noteManager.ItemClick(position);
            }
        });
        //侧滑点击监听
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:  //加密
                        if(!sManager.isHavePassWord()){
                            Intent intent = new Intent(CalendarActivity.this,SettingActivity.class);
                            startActivity(intent);
                            MsgToast.showToast(CalendarActivity.this, "请设置密码");
                            overridePendingTransition(R.anim.in_from_right  , R.anim.out_to_left);
                        }else {
                            noteManager.setSecurity(position);
                            MsgToast.showToast(CalendarActivity.this, "已加密");
                        }
                        break;
                    case 1:    //删除
                        noteManager.deleteClick(position);
                }
                return true;
            }
        });
    }

    public void actionbarReset(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.back_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fab_setting(){

        final FloatingActionButton fab_add_note = (FloatingActionButton)findViewById(R.id.add_note_calendar);

        fab_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭fab菜单
                FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.action_menu_calendar);
                menu.collapse();

                Intent intent = new Intent(CalendarActivity.this,CreateActivity.class);
                intent.putExtra("currentFolderName",currentFolderName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.in_from_left);
                findViewById(R.id.action_menu_calendar).bringToFront();
            }
        });
        FloatingActionButton fab_voice_note = (FloatingActionButton)findViewById(R.id.add_voice_note_calendar);

        fab_voice_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭fab菜单
                FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.action_menu_calendar);
                menu.collapse();

                Intent intent = new Intent(CalendarActivity.this,VoiceCreateActivity.class);
                intent.putExtra("currentFolderName",currentFolderName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                findViewById(R.id.action_menu_calendar).bringToFront();

            }

        });
    }

    /**
     * 检查是否已设置密码
     */
    private  void checkHavePassWord(int position){

        //MsgToast.showToast(MainActivity.this, "检测");
        Intent intent1 = new Intent(CalendarActivity.this, SecurityActivity.class);
        //MODEL_VERIFY 验证密码模式
        intent1.putExtra("model",SecurityActivity.MODEL_VERIFY);
        intent1.putExtra("position",position);
        startActivityForResult(intent1,1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    int position = data.getIntExtra("position",0);
                    noteManager.ItemClick(position);
                }
                break;
            default:
        }
    }
}
