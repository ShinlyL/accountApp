package com.example.administrator.accountbook;


import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class bookFragment extends Fragment implements View.OnClickListener{
    private ListView listView;
    private TextView tvDate,tvExpend,tvIncome;
    private Button btnRecord;

    private User user;
    private Acbook acbook;
    private UserDao userDao;
    private AcbookDao acbookDao;

    private Bundle bundle;

    private int year,month,day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        bundle = getArguments();
        user = (User) bundle.getSerializable("user");
        getDate();
        initView(view);
        initTopView();
//        System.out.println(user);
        btnRecord.setOnClickListener(this);
        MyAdapter myAdapter = new MyAdapter(getContext(),getData());
        listView.setAdapter(myAdapter);

        return  view;
    }

    private void initTopView() {
        tvDate.setText(year+"年"+month+"月"+day+"日");
        //设置 支出和收入的显示总和内容
        String date = year+"-"+month+"-"+day;
        String number = String.valueOf(user.get_id());
        AcbookDao acbookDao = new AcbookDao(getContext());
        List<Acbook> acbookList = acbookDao.getAcbookNumByDate(date,number);
/*
        for(Acbook acbook:acbookList){
            System.out.println(acbook);
        }*/
        double income = 0.0;
        double expend = 0.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        for(Acbook acbook:acbookList){
            int type = (int) acbookDao.getIvSourceId(acbook.getFlag()).get("type");
            if(type > 0){
                income += Double.parseDouble(acbook.getMoney());
            }else if(type < 0){
                expend += Double.parseDouble(acbook.getMoney());
            }
        }
        tvIncome.setText("收入："+df.format(income)+"元");
        tvExpend.setText("支出："+df.format(expend)+"元");

    }
    public void getDate(){
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;  //Calendar.MONTH获取到的月份是从0开始计数的
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    public void initView(View v){
        tvDate = v.findViewById(R.id.rgbook_tv_date);
        tvExpend = v.findViewById(R.id.fgbook_tv_expend);
        tvIncome = v.findViewById(R.id.fgbook_tv_income);
        btnRecord = v.findViewById(R.id.fgbook_btn_record);
        listView = v.findViewById(R.id.frgbook_lv);
    }

    private List<Map<String,Object>> getData(){
        String date = year+"-"+month+"-"+day;
        String number = String.valueOf(user.get_id());
        AcbookDao acbookDao = new AcbookDao(getContext());
        List<Acbook> acbookList = acbookDao.getAcbookNumByDate(date,number);
        if(acbookList.size() == 0){
            Toast.makeText(getContext(),"今日没有记账记录！",Toast.LENGTH_SHORT).show();
        }
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(Acbook acbook:acbookList){
            Map<String, Object> map = new HashMap<String,Object>();
            String flag = acbook.getFlag();
            map.put("cv",acbookDao.getIvSourceId(flag).get("flagId"));
            map.put("detail",acbook.getDetail());
            map.put("id",acbook.get_id());
            map.put("date",acbook.getDate());
            map.put("money",acbook.getMoney());
            map.put("number",acbook.getNumber());
            map.put("type",acbookDao.getIvSourceId(flag).get("type"));
            list.add(map);
        }
        return list;
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.fgbook_btn_record:
                intent = new Intent(getContext(),RecordActivity.class);
                bundle = getArguments();
                user = (User) bundle.getSerializable("user");
                userDao = new UserDao(getContext());
                User userFull = userDao.selectUser(user.getAccount(), user.getPassword());
//                System.out.println(userFull.toString());
                bundle.putSerializable("user",userFull);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
    private final class ViewHolder{
        public CircleImageView cv;
        public TextView detail,date,money;
    }
    public class MyAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        private List<Map<String,Object>> mdata;
        private int[] color = new int[]{0xFFFF69B4,0xFFAFEEEE};

        public MyAdapter(Context context,List<Map<String,Object>> mdata){
            layoutInflater = LayoutInflater.from(context);
            this.mdata = mdata;
        }

        @Override
        public int getCount() {
            return mdata.size();
        }

        @Override
        public Object getItem(int position) {
            return mdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.itemfrgbook,null);
            // 得到各个控件的实例化对象
            holder.cv = convertView.findViewById(R.id.itemfrgbook_iv_kind);
            holder.detail = convertView.findViewById(R.id.itemfrgbook_tv_detail);
            holder.date = convertView.findViewById(R.id.itemfrgbook_tv_date);
            holder.money = convertView.findViewById(R.id.itemfrgbook_tv_money);
            // 设置显示的内容，把动态数组中的数据存在这些对象中
            final Map<String,Object> map = mdata.get(position);
            holder.cv.setImageResource((Integer)map.get("cv"));
            holder.detail.setText("详细信息："+(String)map.get("detail"));
            String[] date = String.valueOf(map.get("date")).split("-");
            holder.date.setText("时间："+date[0]+"年"+date[1]+"月"+date[2]+"日");
            if(String.valueOf(map.get("type")).equals("-1")){
                holder.money.setText("支出："+(String)map.get("money")+"元");
            }else{
                holder.money.setText("收入："+(String)map.get("money")+"元");
            }
//            将每一条记录添加背景颜色  type -1  1
            int type = (Integer.parseInt(String.valueOf(map.get("type")))+1)/2;
//            System.out.println("============="+type);
            convertView.setBackgroundColor(color[type]);
//            实例化一个Acbook对象，用于传递到修改页面进行值的获取
            final Acbook acbook = new AcbookDao(getContext()).find(String.valueOf(map.get("id")));
//            点击任何一个位置都可以跳转到修改页面
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toRecordActivity(acbook);
                }
            });
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toRecordActivity(acbook);
                }
            });
            holder.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toRecordActivity(acbook);
                }
            });
            holder.money.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toRecordActivity(acbook);
                }
            });
            return convertView;
        }
//        跳转页面方法
        public void toRecordActivity(Acbook acbook){
            Intent intent1 = new Intent(getContext(),RecordActivity.class);
            UserDao userDao = new UserDao(getContext());
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("acbook",acbook);
            bundle1.putSerializable("user",userDao.selectUserById(acbook.getNumber()));
            intent1.putExtras(bundle1);
            startActivity(intent1);
        }
    }
}
