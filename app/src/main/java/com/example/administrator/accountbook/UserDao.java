package com.example.administrator.accountbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2020-10-31 , 0031.
 */

public class UserDao {
    private MyHelper myHelper;
    private SQLiteDatabase db;
    public UserDao(Context context){ myHelper = new MyHelper(context);}

    //添加用户信息
    public void adduser(User user) {
        db = myHelper.getWritableDatabase();
        db.execSQL("insert into tb_user(_id,account,password,nickname,photo) values(?,?,?,?,?)"
                , new Object[]{user.get_id(), user.getAccount(), user.getPassword(), user.getNickname(), user.getPhoto()});
        db.close();
    }
    //获取最大便签数量  用于创建对象时的_id值
    public int getMaxId(){
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(_id) from tb_user",null);
        while(cursor.moveToNext()){
            int num = cursor.getInt(0);
            cursor.close();
            db.close();
            return num;
        }
        cursor.close();
        db.close();
        return 0;
    }
    //通过账号和密码查找一个用户，返回用户
    public User selectUser(String account,String password){
        User user;
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_user where account = ? and password = ?",
                new String[]{account,password});
        if(cursor.getCount() >0 && cursor.moveToNext()){
            user = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    //通过id查找一个用户，返回用户
    public User selectUserById(String number){
        User user;
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_user where _id = ?",
                new String[]{number});
        if(cursor.getCount() >0 && cursor.moveToNext()){
            user = new User(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            cursor.close();
            db.close();
            return user;
        }
        return null;
    }

    // 通过账号和密码修改一个用户名和头像
    public void updateUser(User user){
        db = myHelper.getWritableDatabase();
        db.execSQL("update tb_user set nickname=?,photo=? where _id = ?",
                new String[]{user.getNickname(),user.getPhoto(), String.valueOf(user.get_id())});
        db.close();
    }

}
