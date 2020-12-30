package com.example.administrator.accountbook;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etDetail,etMoney;
    private TextView tvRecord,tvIncome,tvDate;
    private Spinner spKind;
    private Button btnCir,btnDel;
    private View back;

    private Acbook acbook;
    private User user;
    private AcbookDao acbookDao;

    private Calendar cal;
    private int year,month,day;

    private String detailStr,moneyStr,dateStr,flagStr,numberStr;
    private Bundle bundle;
    private boolean doType = false;     //判断是添加新的记账信息 还是 修改记账信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        user = (User) bundle.getSerializable("user");
        acbook = (Acbook) bundle.getSerializable("acbook");
        //获取当前日期
        getDate();
        initView();
        initViewListener();


        initMySpinnerRecord();
        SpinnerListener spinnerListener = new SpinnerListener();
        spKind.setOnItemSelectedListener(spinnerListener);
//        System.out.println("=============="+acbook);
        if(acbook != null){
            doType = true;        //如果有记账记录传递过来，说明是需要进行修改操作
            initUpdateView(acbook);
        }

    }

    public void initView(){
        etMoney = (EditText) findViewById(R.id.et_recordMoney);
        etDetail = (EditText) findViewById(R.id.et_recordDetail);
        tvRecord = (TextView) findViewById(R.id.tv_recordType1);
        tvIncome = (TextView) findViewById(R.id.tv_recordType2);
        tvDate = (TextView) findViewById(R.id.tv_recordDate);
        tvDate.setText(year+"年"+month+"月"+day+"日");     //将当前的日期显示到TextView

        spKind = (Spinner) findViewById(R.id.sp_record);
        btnCir = (Button) findViewById(R.id.btn_cirRecord);
        btnDel = (Button) findViewById(R.id.btn_delRecord);
        back = findViewById(R.id.back_iv);
    }

    public void initViewListener(){
        tvRecord.setOnClickListener(this);
        tvIncome.setOnClickListener(this);
        tvDate.setOnClickListener(this);

        btnCir.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        detailStr = etDetail.getText().toString().trim();
        moneyStr = etMoney.getText().toString().trim();
        dateStr = year+"-"+month+"-"+day;
        numberStr = String.valueOf(user.get_id());
        switch(v.getId()){
            case R.id.tv_recordType1:
                tvRecord.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tvIncome.setBackgroundColor(Color.parseColor("#eee9bf"));
                initMySpinnerRecord();
                break;
            case R.id.tv_recordType2:
                tvRecord.setBackgroundColor(Color.parseColor("#eee9bf"));
                tvIncome.setBackgroundColor(Color.parseColor("#FFFFFF"));
                initMySpinnerIncome();
                break;
            case R.id.btn_cirRecord:
                //验证输入的值是否合理
                if(!checkInput(moneyStr)){
                    Toast.makeText(RecordActivity.this,"金额设置不合理",Toast.LENGTH_SHORT).show();
                    break;
                }else if(detailStr.equals("") || detailStr == null){
                    etDetail.setText("无输入内容");
                    detailStr = etDetail.getText().toString().trim();
                }
                acbookDao = new AcbookDao(this);
                if(doType){
                    // 因为是修改操作，所以acbook对象传递过来，并一早就存在，不需创建
                    acbook.setMoney(moneyStr);
                    acbook.setFlag(flagStr);
                    acbook.setDetail(detailStr);
                    acbook.setDate(dateStr);
                    acbookDao.update(acbook);
                    Toast.makeText(RecordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                }else{
                    acbook = new Acbook(acbookDao.getMaxId()+1,detailStr,flagStr,dateStr,moneyStr,numberStr);
                    acbookDao.add(acbook);
                    Toast.makeText(RecordActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                }
                toMainActivity(user);
                break;
            case R.id.btn_delRecord:
                //删除记账信息，使用消息框进行误删提示
                if(doType){
                    showNormalDialog(acbook.get_id());
                }else{
                    toMainActivity(user);
                }
                break;
            case R.id.tv_recordDate:
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
                        year = year1;
                        month = month1;
                        day = dayOfMonth;
                        tvDate.setText(year1+"年"+month1+"月"+dayOfMonth+"日");     //将选中的日期显示到TextView
                    }
                };
                //可无视，属于API版本过低导致的错误，不影响运行
                DatePickerDialog dialog = new DatePickerDialog(RecordActivity.this,DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
                dialog.show();
                break;
            case R.id.back_iv:
                toMainActivity(user);
                break;
        }
    }

    /**
     * 将修改的值填写入相应的位置
     */
    public void initUpdateView(Acbook acbook){
        btnDel.setText("删除");
        AcbookDao acbookDao = new AcbookDao(this);
        int type = (int) acbookDao.getIvSourceId(acbook.getFlag()).get("type");
        String flag = acbook.getFlag();
        int flagIndex = Integer.parseInt(flag.substring(flag.indexOf("/")+1));
        etMoney.setText(acbook.getMoney());
        if(type > 0){
            tvRecord.setBackgroundColor(Color.parseColor("#eee9bf"));
            tvIncome.setBackgroundColor(Color.parseColor("#FFFFFF"));
            initMySpinnerIncome();
        }else{
            tvRecord.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tvIncome.setBackgroundColor(Color.parseColor("#eee9bf"));
            initMySpinnerRecord();
        }
        spKind.setSelection(flagIndex,true);
        flagStr = acbook.getFlag();
        etDetail.setText(acbook.getDetail());
        String dateStr = acbook.getDate();
        String[] dateSplit = dateStr.split("-");
        year = Integer.parseInt(dateSplit[0]);
        month = Integer.parseInt(dateSplit[1]);
        day = Integer.parseInt(dateSplit[2]);
        tvDate.setText(year+"年"+month+"月"+day+"日");
    }
    public void getDate() {
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;  //获取到的月份是从0开始计数的
        day = cal.get(Calendar.DAY_OF_MONTH);
    }
    //初始化支出的分类 适配器
    private void initMySpinnerRecord(){
        int[] recordPic = {R.drawable.restaurant,R.drawable.pet,R.drawable.parents,R.drawable.shopping,
        R.drawable.traffic,R.drawable.occupyhome,R.drawable.human,R.drawable.psychiatry};
        int[] recordStr = {R.string.Restaurant,R.string.Pet,R.string.Parents,R.string.Shopping,R.string.Traffic,R.string.OccupyHome,R.string.Human,R.string.Psychiatry};

        MyAdapter adapter = new MyAdapter(this,recordPic,recordStr);
        spKind.setAdapter(adapter);
        spKind.setPrompt("支出类型");
        spKind.setSelection(0,true);
        flagStr = "餐饮/0";
    }
    //初始化收入的分类 适配器
    private void initMySpinnerIncome(){
        int[] incomePic = {R.drawable.reimburse,R.drawable.wage,R.drawable.redpacket,R.drawable.parttime,
        R.drawable.bonus,R.drawable.investment};
        int[] incomeStr = {R.string.Reimburse,R.string.Wage,R.string.RedPacket,R.string.PartTime,R.string.Bonus,R.string.Investment};

        MyAdapter adapter = new MyAdapter(this,incomePic,incomeStr);
        spKind.setAdapter(adapter);
        spKind.setPrompt("收入类型");
        spKind.setSelection(0,true);
        flagStr = "报销/0";
    }

    // 创建一个内容类，Spinner使用，获取其中的值
    class SpinnerListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getId() == R.id.sp_record){
                LinearLayout ll = (LinearLayout) view;
                TextView tv = (TextView) ll.findViewWithTag("tag TextView");
                flagStr = tv.getText().toString().trim()+"/"+position;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
    //    显示普通的对话框
    private void showNormalDialog(final int number){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(RecordActivity.this);
        normalDialog.setIcon(R.drawable.drg);
        normalDialog.setTitle("删除记录");
        normalDialog.setMessage("您确认删除该笔记录吗？");
        normalDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AcbookDao acbookDao = new AcbookDao(RecordActivity.this);
                acbookDao.delete(number);
                toMainActivity(user);
            }
        });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        normalDialog.show();    // 显示对话框
    }
    // 跳转回首页面，将User对象传递过去
    private void toMainActivity(User user){
        Intent intent = new Intent(RecordActivity.this,MainActivity.class);
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //验证用户的设置的值是否合理
    private boolean checkInput(String moneyStr){
        Pattern pattern= Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match=pattern.matcher(moneyStr);
        return match.matches();
    }
}
