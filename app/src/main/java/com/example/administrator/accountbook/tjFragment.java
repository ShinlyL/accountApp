package com.example.administrator.accountbook;

import android.app.DatePickerDialog;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class tjFragment extends Fragment implements View.OnClickListener{
    private TextView tvDate1,tvDate2,tvExpend,tvIncome;

    private Acbook acbook;
    private AcbookDao acbookDao;
    private User user;
    private UserDao userDao;
    private Bundle bundle;

    private Calendar cal;
    private int year,month,day;
    private PieChart mPieChart;
    private List<PieData> pieData;
    public static final int COUNT = 1;//连续绘制

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tj, container, false);

        acbookDao = new AcbookDao(getContext());
        userDao = new UserDao(getContext());
        bundle = getArguments();
        user = (User) bundle.getSerializable("user");
        initView(view);
        initViewListener();
        initTopView();
        initPieView(view);

        return view;
    }

    private void initViewListener() {
        tvDate1.setOnClickListener(this);
        tvDate2.setOnClickListener(this);
    }

    private void initPieView(View v){
        String fromDate = year+"-"+month+"-"+1;
        String endDate = year+"-"+month+"-"+day;
        List<Acbook> acbookList = acbookDao.getAcbookNumByDate2(String.valueOf(user.get_id()), fromDate, endDate);
        Map<String, Double> acbookMap = acbookDao.typeTotal(acbookList);
        showPieView(v,acbookMap);
        Double expend = acbookMap.get("支出总和");
        Double income = acbookMap.get("收入总和");
        System.out.println(expend+"===="+income);
    }

    private void showPieView(View v, Map<String,Double> acbookMap) {
        mPieChart = v.findViewById(R.id.mPieChart);
        pieData = new ArrayList<>();
        Iterator<Map.Entry<String, Double>> it = acbookMap.entrySet().iterator();
        DecimalFormat df = new DecimalFormat("######0.00");
        while (it.hasNext()) {
            Map.Entry<String, Double> entry = it.next();
            if(entry.getKey().equals("支出总和")){
                tvIncome.setText("总支出："+df.format(entry.getValue()));
                continue;
            }else if(entry.getKey().equals("收入总和")){
                tvIncome.setText("总收入："+df.format(entry.getValue()));
                continue;
            }
            String flag = entry.getKey().substring(0,entry.getKey().indexOf("/"));
            float percent = (float) (entry.getValue()/(acbookMap.get("支出总和")+acbookMap.get("收入总和")));
            pieData.add(new PieData(flag, percent));
        }
        mPieChart.setData(pieData, COUNT);
    }


    private void initView(View v) {
        tvDate1 = v.findViewById(R.id.rgtj_tv_date1);
        tvDate2 = v.findViewById(R.id.rgtj_tv_date2);
        tvExpend = v.findViewById(R.id.fgtj_tv_expend);
        tvIncome = v.findViewById(R.id.fgtj_tv_income);
    }
//    初始化顶层显示 该账号本月内的所有记录
    private void initTopView() {
        getDate();
        tvDate1.setText(year+"年"+month+"月"+1+"日");
        tvDate2.setText(year+"年"+month+"月"+day+"日");
        String fromDate = year+"-"+month+"-"+1;
        String endDate = year+"-"+month+"-"+day;
        List<Acbook> acbookList = acbookDao.getAcbookNumByDate2(String.valueOf(user.get_id()),
                fromDate,endDate);
//        System.out.println(">>>>>>>>>>>>  "+acbookList.size());
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
        tvIncome.setText("总收入："+df.format(income)+"元");
        tvExpend.setText("总支出："+df.format(expend)+"元");
    }
//   获取当前日期的值
    public void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;  //获取到的月份是从0开始计数的
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rgtj_tv_date1:
                changeDate(tvDate1);
                break;
            case R.id.rgtj_tv_date2:
                changeDate(tvDate2);
                break;
        }
    }
//  比较时间的查询条件
    private void compareDate(TextView tvDate1, TextView tvDate2) {
        String date1Str = tvDate1.getText().toString().trim();
        String date2Str = tvDate2.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日", Locale.CHINA);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(date1Str);
            date2 = format.parse(date2Str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1.compareTo(date2) <= 0){
//            进行查询
//            System.out.println("okokokokokookkookko");
            format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            String dateP = format.format(date1);
            String dateE = format.format(date2);
            List<Acbook> acbookList = acbookDao.getAcbookNumByDate2(String.valueOf(user.get_id()), dateP, dateE);
            Map<String, Double> map = acbookDao.typeTotal(acbookList);
            showPieView(getView(),map);
        }else{
//            date1 比 date2 时间要更现在，不符合查询要求
            Toast.makeText(getContext(),"时间设置有误！",Toast.LENGTH_SHORT).show();
            initTopView();  //重置时间
        }
    }

    private void changeDate(final TextView tvDate){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                year = year1;
                month = month1;
                day = dayOfMonth;
                tvDate.setText(year1+"年"+(++month1)+"月"+dayOfMonth+"日");     //将选中的日期显示到TextView
                compareDate(tvDate1,tvDate2);
            }
        };
        //可无视，属于API版本过低导致的错误，不影响运行
        DatePickerDialog dialog = new DatePickerDialog(getContext(),DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
            dialog.show();
    }
}
