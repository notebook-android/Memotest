package com.example.ting.mometest.Activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ting.mometest.Adapter.MainSwipeAdapter;
import com.example.ting.mometest.Manager.DBManager;
import com.example.ting.mometest.Manager.NoteManager;
import com.example.ting.mometest.Manager.SecurityManager;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.ComparatorUtil;
import com.example.ting.mometest.View.MainCreator;
import com.example.ting.mometest.View.MainScrollview;
import com.example.ting.mometest.View.MsgToast;
import com.example.ting.mometest.View.SwipeListView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Collections;
import java.util.List;

public class AllNotesActivity extends AppCompatActivity {

    private SwipeListView mListView;
    private List<Note> mData;
    private NoteManager noteManager;
    private DrawerLayout mDrawer;
    private String currentFolderName = "Notes";
    private SecurityManager sManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        sManager= new SecurityManager(this);
        actionbarReset();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView_setting();
        findViewById(R.id.action_menu).bringToFront();

    }

    protected void onStart(){
        super.onStart();
        listView_setting();
        findViewById(R.id.action_menu).bringToFront();

    }

    public void init (){
        listView_setting();
        fab_setting();
    }

    private void fab_setting(){

        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.add_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭fab菜单
                FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.action_menu);
                menu.collapse();

                Intent intent = new Intent(AllNotesActivity.this,CreateActivity.class);
                intent.putExtra("currentFolderName",currentFolderName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.in_from_left);
                findViewById(R.id.action_menu).bringToFront();
            }
        });
        FloatingActionButton fab_voice = (FloatingActionButton)findViewById(R.id.add_voice_note);

        fab_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭fab菜单
                FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.action_menu);
                menu.collapse();

                Intent intent = new Intent(AllNotesActivity.this,VoiceCreateActivity.class);
                intent.putExtra("currentFolderName",currentFolderName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                findViewById(R.id.action_menu).bringToFront();

            }

        });
    }

    public void listView_setting(){

        hide_fabMenu();

        mData = new DBManager(this).search(currentFolderName,0,0,0,0);   //返回List<Note>

        Collections.sort(mData,new ComparatorUtil());  //按照时间排序

        MainSwipeAdapter adapter = new MainSwipeAdapter(this,mData);   //添加ListView数据适配器

        noteManager = new NoteManager(this,currentFolderName,mData,adapter);

        MainCreator mainCreator = new MainCreator(this);  //主页面ListView侧滑菜单（移动、删除...）

        mListView = (SwipeListView)findViewById(R.id.list_view);
        mListView.setMenuCreator(mainCreator);      //设置侧滑菜单
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);   //设置侧滑方向
        mListView.setAdapter(adapter);   //设置数据适配器

        MainScrollview scrollview = (MainScrollview)findViewById(R.id.all_scrollView);   //主界面滑动scrollview
        scrollview.setOnScrollListener(new MainScrollview.ScrollViewListener() {
            @Override
            public void onScroll(int dy) {
                if(dy > 0){    //下滑
                    showOrHideFab(false);
                }else if(dy <= 0){   //上滑
                    showOrHideFab(true);
                }
            }
        });
        view_Listener();    //点击监听
        emptyListCheck();   //检测List是否为0
    }

    public void showOrHideFab(boolean show){
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.action_menu);

        if(show){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }
    }

    private void hide_fabMenu(){
        //关闭fab菜单（折叠）
        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.action_menu);
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
                            Intent intent = new Intent(AllNotesActivity.this,SettingActivity.class);
                            startActivity(intent);
                            MsgToast.showToast(AllNotesActivity.this, "请设置密码");
                            overridePendingTransition(R.anim.in_from_right  , R.anim.out_to_left);
                        }else {
                            noteManager.setSecurity(position);
                            MsgToast.showToast(AllNotesActivity.this, "已加密");
                        }
                        break;
                    case 1:    //删除
                        noteManager.deleteClick(position);
                        emptyListCheck();
                }
                return true;
            }
        });
    }

    public void emptyListCheck(){
        /**
         * 检测Note是否删空
         */
        int number = 0;
        if(mData!=null){
            number=mData.size();
        }
        if(number == 0){     //没有Note，显示empty
            mListView.setVisibility(View.GONE);
            RelativeLayout empty = (RelativeLayout)findViewById(R.id.empty);
            empty.setVisibility(View.VISIBLE);

            TextView info = (TextView)findViewById(R.id.text_empty);
            info.setText(R.string.main_empty_info);
        }else{
            mListView.setVisibility(View.VISIBLE);
            RelativeLayout empty = (RelativeLayout)findViewById(R.id.empty);
            empty.setVisibility(View.GONE);
        }
    }

    public void actionbarReset(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.back_allNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllNotesActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        toolbar.inflateMenu(R.menu.menu_main);//设置右上角的填充菜单（搜索）
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search_item:
                        Intent intent1 = new Intent(AllNotesActivity.this,SearchActivity.class);
                        intent1.putExtra("currentFolderName",currentFolderName);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 检查是否已设置密码
     */
    private  void checkHavePassWord(int position){

        Intent intent1 = new Intent(AllNotesActivity.this, SecurityActivity.class);
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
