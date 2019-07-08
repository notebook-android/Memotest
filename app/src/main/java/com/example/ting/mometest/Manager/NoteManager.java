package com.example.ting.mometest.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.BaseAdapter;

import com.example.ting.mometest.Activity.ContentActivity;
import com.example.ting.mometest.Activity.MainActivity;
import com.example.ting.mometest.Activity.R;
import com.example.ting.mometest.Activity.RecordPlayerActivity;
import com.example.ting.mometest.Dialog.EditDialog;
import com.example.ting.mometest.Dialog.MyOnClickListener;
import com.example.ting.mometest.Model.Date;
import com.example.ting.mometest.Model.Note;
import com.example.ting.mometest.Util.StringUtil;
import com.example.ting.mometest.View.MsgToast;

import java.util.List;

/**
 * Note管理类
 */
public class NoteManager {

    private Context mContext;
    private List<Note> list;

    private String currentFolderName;//Note的所属文件夹
    private BaseAdapter adapter; //适配器
    private DBManager dbManager;//数据库管理类

    public NoteManager(Context context,String currentFolderName){
        this.mContext=context;
        this.currentFolderName=currentFolderName;
        dbManager=new DBManager(mContext);
    }

    public NoteManager(Context context,String currentFolderName,
                       List<Note> list,BaseAdapter adapter){
        this(context,currentFolderName);
        this.list=list;
        this.adapter=adapter;
    }

    /**
     * listView的短点击事件
     * @param position
     */
    public void ItemClick(int position){
        final Note select_item = list.get(position);
        ItemClick(select_item);
    }

    /**
     * 打开note
     * @param select_item
     */
    private void ItemClick(Note select_item){

        if(select_item.getRecordFile()!=null){
            Intent intent = new Intent(mContext,RecordPlayerActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("note", select_item);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }else {
            Intent intent = new Intent(mContext,ContentActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("note", select_item);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    /**
     * 点击了删除
     * @param position
     */
    public void deleteClick(int position){

        Note select_item = list.get(position);
        delete(select_item);
        MsgToast.showToast(mContext,"已移至回收站");

    }

    /**
     * 新建Note
     * @param note
     */
    public void add(Note note){
        dbManager.insert(currentFolderName,note);
    }

    /**
     * 数据库交互
     * @param note
     */
    private void delete(Note note) {
        list.remove(note);

        adapter.notifyDataSetChanged();

        dbManager.delete(currentFolderName,note);
    }

    /**
     * 删除Note
     * @param note
     */
    public void deleteNote(Note note) {

        final Note note1 = note;
        dbManager.delete(currentFolderName,note1);
    }

    /**
     * 加密Note
     */
    public void setSecurity(int position){

        Note select_item = list.get(position);
        Note new_item = select_item.getClone();
        new_item.setSecurity(true);
        update(select_item,new_item);

        if (list!=null){
            int index = list.indexOf(select_item);
            list.set(index, new_item);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 是否加密
     */
    public boolean checkSecurity(int position){
        Note select_item = list.get(position);
        if(select_item.isSecurity()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 数据库更新
     * @param preNote
     * @param newNote
     */
    public void update(Note preNote,Note newNote){
        dbManager.upDate(currentFolderName,preNote,newNote);
    }

}
