package com.example.administrator.accountbook;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView rgIv;
    private EditText rgName,rgAccount,rgPassword,rgRepd;
    private Button btnRegist,btnBack;

    private Bundle bundle;
    private MyHelper myHelper;
    private SQLiteDatabase db;
    public static final int SELECT_PIC = 1;
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        myHelper = new MyHelper(this);
        user = new User();
        bundle = new Bundle();
        initView();
        initViewListener();
    }

    private void initView() {
        rgIv = (CircleImageView) findViewById(R.id.reg_iv_head);
        rgName = (EditText) findViewById(R.id.rg_et_name);
        rgAccount = (EditText) findViewById(R.id.rg_et_account);
        rgPassword = (EditText) findViewById(R.id.rg_et_password);
        rgRepd = (EditText) findViewById(R.id.rg_et_repd);
        btnRegist = (Button) findViewById(R.id.rg_btn1);
        btnBack = (Button) findViewById(R.id.rg_btn2);
    }

    private void initViewListener() {
        rgIv.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String nameStr = rgName.getText().toString().trim();
        String accountStr = rgAccount.getText().toString().trim();
        String passwordStr = rgPassword.getText().toString().trim();
        String rePdStr = rgRepd.getText().toString().trim();

        switch(v.getId()){
            case R.id.reg_iv_head:
                Intent intent = new Intent(RegisteActivity.this,DialogActivity.class);
                startActivityForResult(intent, SELECT_PIC);
                break;
            case R.id.rg_btn1:
                if(check(nameStr,accountStr,passwordStr,rePdStr)){
                    UserDao userDao = new UserDao(RegisteActivity.this);
                    if(user.getPhoto() == null){
                        user.setPhoto("2130837614");
                    }
                    user.set_id(userDao.getMaxId()+1);
                    user.setAccount(accountStr);
                    user.setPassword(passwordStr);
                    user.setNickname(nameStr);
                    userDao.adduser(user);
                    Toast.makeText(RegisteActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(RegisteActivity.this,MainActivity.class);
                    bundle.putSerializable("user",user);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
                break;
            case R.id.rg_btn2:
                Intent intent2 = new Intent(RegisteActivity.this,LoginActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private boolean check(String nickName,String account,String password,String rePassword) {
        if(nickName != null && account != null && password != null && rePassword.equals(password)){
            return true;
        }else{
            Toast.makeText(RegisteActivity.this,"输入有误，无法注册",Toast.LENGTH_SHORT).show();
            return false;
        }
   }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == SELECT_PIC && resultCode == RESULT_OK){
            int imgid = data.getIntExtra("image",-1);
            if(imgid != -1){
                rgIv.setImageResource(imgid);
                user.setPhoto(String.valueOf(imgid));       //将图片信息放入user对象中
            }
        }
    }
}
