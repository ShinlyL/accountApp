package com.example.administrator.accountbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2020-10-31 , 0031.
 */

public class AcbookDao {
    private MyHelper myHelper;
    private SQLiteDatabase db;
    public AcbookDao(Context context){ myHelper = new MyHelper(context); }

    //插入记账信息
    public void add(Acbook acbook){
        db = myHelper.getWritableDatabase();
        db.execSQL("insert into tb_acbook(_id,detail,flag,date,money,number) values(?,?,?,?,?,?)",
                new Object[]{acbook.get_id(),acbook.getDetail(),acbook.getFlag(),acbook.getDate(),acbook.getMoney(),acbook.getNumber()});
        db.close();
    }
    //删除指定记账记录
    public void delete(int id){
        db = myHelper.getWritableDatabase();
        db.execSQL("delete from tb_acbook where _id = ?",new Object[]{id});
        db.close();
    }
    //修改指定记账记录
    public void update(Acbook acbook){
        db = myHelper.getWritableDatabase();
        db.execSQL("update tb_acbook set detail=?,flag=?,money=?,date=? where _id=?",
                new Object[]{acbook.getDetail(),acbook.getFlag(),acbook.getMoney(),acbook.getDate(),acbook.get_id()});
        db.close();
    }
    //查找指定记账记录   根据id查找
    public Acbook find(String id){
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_acbook where _id = ?",
                new String[]{id});
        if(cursor.moveToNext()){
            Acbook acbook = new Acbook(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5));
            cursor.close();
            db.close();
            return acbook;
        }else{
            cursor.close();
            db.close();
            return null;
        }
    }
    //根据日期进行查找
    public List<Acbook> findAcbookByDate(String date){
        List<Acbook> list = new ArrayList<Acbook>();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_acbook where date = ?",new String[]{date});
        while(cursor.moveToNext()){
            Acbook acbook = new Acbook(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5));
            list.add(acbook);
        }
        cursor.close();
        db.close();
        return list;
    }

    //获取最大便签数量  用于创建对象时的_id值
    public int getMaxId(){
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(_id) from tb_acbook",null);
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


    /**
     * @des 获取指定账号的记账记录
     */
    public List<Acbook> getData(String number){
        List<Acbook> acbookList = new ArrayList<Acbook>();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_acbook where number = ?",
                new String[]{number});
        while(cursor.moveToNext()){
            acbookList.add(new Acbook(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor
            .getString(3),cursor.getString(4),cursor.getString(5)));
        }
        cursor.close();
        db.close();
        return acbookList;
    }
    /**
     * @des 获取当前日期的所有 记账记录
     */
    public List<Acbook> getAcbookNumByDate(String date,String number){
        List<Acbook> acbookList = new ArrayList<Acbook>();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_acbook where date = ? and number = ?",
                new String[]{date,number});
        while(cursor.moveToNext()){
            acbookList.add(new Acbook(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor
                    .getString(3),cursor.getString(4),cursor.getString(5)));
        }
        cursor.close();
        db.close();
        return acbookList;
    }
    /**
     * @des 获取当前日期段内的所有 记账记录
     */
    public List<Acbook> getAcbookNumByDate2(String number,String proDate,String endDate){
        List<Acbook> acbookList = new ArrayList<Acbook>();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_acbook where number = ? and date between ? and ?",
                new String[]{number,proDate,endDate});
        while(cursor.moveToNext()){
            acbookList.add(new Acbook(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor
                    .getString(3),cursor.getString(4),cursor.getString(5)));
        }
        cursor.close();
        db.close();
        return acbookList;
    }
    /**
     * @des 获取某种类型记账的数量
     */
    public int getNum(String flag,String number){
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(_id) from tb_acbook where flag=? and number=?",
                new String[]{flag,number});
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
    /**
     * @des 获取账单数组中的 不同类别 的数量
     */
    public Map<String,Double> typeTotal(List<Acbook> list){
        Map<String,Double> typeTotal = new HashMap<String,Double>();
        String[] recordStr = {"餐饮/0","宠物/1","父母/2","购物/3","交通/4","居家/5","人情/6","医教/7"};
        String[] incomeStr = {"报销/0","工资/1","红包/2","兼职/3","奖金/4","投资/5"};
        for(Acbook ab:list){
            if(null == typeTotal.get(ab.getFlag())){
                typeTotal.put(ab.getFlag(),Double.parseDouble(ab.getMoney()));
            }else{
                typeTotal.put(ab.getFlag(),typeTotal.get(ab.getFlag())+Double.parseDouble(ab.getMoney()));
            }
        }
        typeTotal.put("支出总和",0.0);
        typeTotal.put("收入总和",0.0);
//        System.out.println("通过Map.entrySet使用iterator遍历key和value：");
        Iterator<Map.Entry<String, Double>> it = typeTotal.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if(isHave(recordStr,entry.getKey())){
                typeTotal.put("支出总和",typeTotal.get("支出总和")+entry.getValue());
            }else if(isHave(incomeStr,entry.getKey())){
                typeTotal.put("收入总和",typeTotal.get("收入总和")+entry.getValue());
            }
        }
        return typeTotal;
    }
    /*这是一个静态函数，不用声明对象就可以用的*/
    private static boolean isHave(String[] strs,String s) {
//        此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
        for (int i = 0; i < strs.length; i++) {
            //循环查找字符串数组中的每个字符串中是否包含所有查找的内容
            if (strs[i].indexOf(s) != -1) {
                return true;//查找到了就返回真，不在继续查询
            }
        }
        return false;
    }
    /**
     * 返回flag的sourceId
     */
    public Map<String,Object> getIvSourceId(String flag){
        Map<String,Object> map = new HashMap<String,Object>();
        int[] incomePic = {R.drawable.reimburse,R.drawable.wage,R.drawable.redpacket,R.drawable.parttime,
                R.drawable.bonus,R.drawable.investment};
        String[] incomeStr = {"报销","工资","红包","兼职","奖金","投资"};
        int[] recordPic = {R.drawable.restaurant,R.drawable.pet,R.drawable.parents,R.drawable.shopping,
                R.drawable.traffic,R.drawable.occupyhome,R.drawable.human,R.drawable.psychiatry};
        String[] recordStr = {"餐饮","宠物","父母","购物","交通","居家","人情","医教"};
        int flagNum = Integer.parseInt(flag.substring(flag.indexOf("/")+1));
        flag = flag.substring(0,flag.indexOf("/"));

        if(flagNum<incomeStr.length && incomeStr[flagNum].equals(flag)){
            map.put("flagId",incomePic[flagNum]);
            map.put("type",1);                          //1 表示收入
            return map;
        }else{
            map.put("flagId",recordPic[flagNum]);
            map.put("type",-1);                          //-1 表示支出
            return map;
        }
    }
}
