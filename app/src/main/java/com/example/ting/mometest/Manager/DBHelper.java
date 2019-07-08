package com.example.ting.mometest.Manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //默认建表语句 默认文件夹 回收站
    //使用blob类型存对象 有点偷懒..
    private static final String CREATE_NOTE =

            "create table Notes ("
                    + "item blob)";

    private static final String CREATE_RECYCLE =
            "create table recycle ("
                    + "item blob)";

    private Context mContext;


    private static DBHelper dbHelper = null;


    private DBHelper(Context context) {
        //数据库名
        super(context, "Notes.db", null, 1);
        this.mContext = context;
    }

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    /**
     * 初始化
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
        db.execSQL(CREATE_RECYCLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TO DO
    }

    /**
     * 新建文件夹
     * @param name
     */
    public void add_table(String name) {

        String create =
                "create table " + name + "("
                        + "item blob)";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(create);
    }

    /**
     * 文件夹是否存在
     * @param name
     * @return
     */
    public boolean folderIsExist(String name) {

        boolean result = false;
        if (name != null) {

            try {
                SQLiteDatabase db = this.getReadableDatabase();
                String sql = "select count(*) as c from sqlite_master where type ='table' " +
                        "and name ='" +name+ "' ";
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        result = true;
                    }
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;

    }
}
