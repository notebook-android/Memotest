package com.example.ting.mometest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ting.mometest.Activity.R;
import com.example.ting.mometest.Model.Note;

import java.util.List;

/**
 * 主菜单适配器
 */
public class MainSwipeAdapter extends BaseAdapter {

    private List<Note> mData ;
    private Context mContext;

    public MainSwipeAdapter(Context mContext, List<Note> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    /**
     * item的显示
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView) {
            convertView =LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false);
        }
        Note note = ((Note)getItem(position));

        LinearLayout item_note = (LinearLayout)convertView.findViewById(R.id.item_note);
        TextView time = (TextView) convertView.findViewById(R.id.time_item);
        TextView txt = (TextView) convertView.findViewById(R.id.title_item);
        ImageView star = (ImageView)convertView.findViewById(R.id.item_star);
        ImageView lock = (ImageView)convertView.findViewById(R.id.item_lock);
        ImageView voice_item = convertView.findViewById(R.id.voice_item);

        if(note.getRemindDate()!=null){
            time.setText(note.getRemindDate().getTime());            //显示事件时间
        }

        if(note.isSecurity())
            lock.setVisibility(View.VISIBLE);
        else
            lock.setVisibility(View.INVISIBLE);

        if(note.getIs_important() == 1)              //显示star
            star.setVisibility(View.VISIBLE);
        else
            star.setVisibility(View.INVISIBLE);

        StringBuilder sb= new StringBuilder();          //显示标题
        if(note.getName().length()<7){
            sb.append(note.getName());
        }
        else{
            sb.append(note.getName().substring(0,6));
            sb.append("...");
        }
        txt.setText(sb.toString());

        if(note.getLevel() == Note.GRE_LEVEL) {         //显示颜色level
            item_note.setBackgroundResource(R.color.green);
        }
        else if (note.getLevel() == Note.BLU_LEVEL){
            item_note.setBackgroundResource(R.color.blueblue);
        }
        else if(note.getLevel() == Note.PUR_LEVEL){
            item_note.setBackgroundResource(R.color.purple);
        }
        else {
            item_note.setBackgroundResource(R.color.red);
        }

        if(note.getRecordFile()!=null)
            voice_item.setVisibility(View.VISIBLE);
        else
            voice_item.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
