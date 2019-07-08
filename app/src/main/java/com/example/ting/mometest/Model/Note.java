package com.example.ting.mometest.Model;

import com.example.ting.mometest.Util.StringUtil;

import java.io.File;
import java.io.Serializable;

public class Note extends Item implements Serializable,Comparable{

    public static final  int RED_LEVEL = 3;
    public static final  int BLU_LEVEL = 2;
    public static final  int PUR_LEVEL = 1;
    public static final  int GRE_LEVEL = 0;

    private String text;
    //是否提醒
    private int is_remind;
    //重要与否
    private int is_important;
    //提醒日期
    private Date remindDate;
    private Date deleteDate;
    private int level;
    private boolean compareW = false;

    private int text_or_voice;  //1为文字，2为录音
    private File recordFile;
    private boolean security = false;

    public Note(String name, Date date, String folderName) {
        super(name,date,folderName);
        this.text = "";
        this.level  = GRE_LEVEL;
        this.is_important = 0;
        this.is_remind = 0;
        this.text_or_voice = 1;
    }

    public Note(String name, Date date,
                String text,
                String folderName) {

        super(name, date, folderName);
        this.text = text;
    }

    public Note(String name,Date date,File recordFile,String folderName,int level,int is_important,int is_remind){
        super(name,date,folderName);
        this.text_or_voice = 2;
        this.recordFile = recordFile;
        this.level = level;
        this.is_important = is_important;
        this.is_remind = is_remind;
    }

    public Note(String name, Date date,
                String text,
                String folderName,
                int level) {
        super(name, date, folderName);
        this.level = level;
        this.text = text;
    }

    public Note(String name, Date date, String text,
                String folderName,
                int level, int is_important, Date remindDate, int is_remind) {
        super(name, date, folderName);
        this.level = level;
        this.text = text;
        this.is_important = is_important;
        this.remindDate = remindDate;
        this.is_remind = is_remind;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public Note getClone(){

        if(getRecordFile()!=null)
            return  new Note(getName(),getDate(),getRecordFile(),getFolderName(),getLevel(),getIs_important(),getIs_remind());
        else
            return new Note(getName(),getDate(),getText(),getFolderName(),getLevel(),getIs_important(),getRemindDate(),getIs_remind());
    }

    //@Override
    public int compareTo(Object o) {
        java.util.Date d1;
        java.util.Date d2;

        if(compareW){
            d1 = new java.util.Date(getRemindDate().getYear(),
                    getRemindDate().getMonth(),getRemindDate().getDay(),getRemindDate().getHour(),getRemindDate().getMinute());

            Note o2 = (Note) o;
            d2 = new java.util.Date(o2.getRemindDate().getYear(),
                    o2.getRemindDate().getMonth(),o2.getRemindDate().getDay(),o2.getRemindDate().getHour(),o2.getRemindDate().getMinute());
        }else {
            d1 = new java.util.Date(getDate().getYear(),
                    getDate().getMonth(),getDate().getDay(),getDate().getHour(),getDate().getMinute());

            Note o2 = (Note) o;
            d2 = new java.util.Date(o2.getDate().getYear(),
                    o2.getDate().getMonth(),o2.getDate().getDay(),o2.getDate().getHour(),o2.getDate().getMinute());
        }

        if(d1.before(d2))
            return  -1;
        else return 1;

    }

    public Date getDate() {
        return super.getDate();
    }

    public void setDate(Date date) {
        super.setDate(date);
    }

    public String getName() {
        return StringUtil.clearEnter(super.getName());
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeleteDate(Date delteDate) {
        this.deleteDate = delteDate;
    }

    public int getLevel() {
        return level;
    }


    public int getIs_remind() {
        return is_remind;
    }

    public void setIs_remind(int is_remind) {
        this.is_remind = is_remind;
    }

    public int getIs_important() {
        return is_important;
    }

    public void setCompareW(boolean compareW) {
        this.compareW = compareW;
    }

    public File getRecordFile() {
        return recordFile;
    }

    public boolean isSecurity() {
        return security;
    }
    public void setSecurity(boolean security) {
        this.security = security;
    }

}
