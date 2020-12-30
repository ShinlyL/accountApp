package com.example.administrator.accountbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView loginIv;
    private EditText account,password;
    private Button login,regist;
    private CheckBox ckRember,ckAuto;

    private Bundle bundle;
    private UserDao userDao;
    private User user;
    private SharedPreferences loginPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        SQLiteStudioService.instance().start(this);
        //实例化SharedPreferences对象
        loginPreference = getSharedPreferences("login",MODE_PRIVATE);
        userDao = new UserDao(this);
        initView();
        initViewListenter();
        boolean checkedRem = loginPreference.getBoolean("ckRem",ckRember.isChecked());
        boolean checkedAuto = loginPreference.getBoolean("autologin",ckAuto.isChecked());
        if(checkedRem){
            Map<String,Object> m = readLogin();
            if(m != null){
                loginIv.setImageResource(Integer.parseInt(String.valueOf(m.get("photo"))));
                account.setText((CharSequence) m.get("account"));
                password.setText((CharSequence) m.get("password"));
                ckAuto.setChecked(checkedAuto);
                ckRember.setChecked(checkedRem);
            }
        }
        if(checkedRem && checkedAuto){
            Map<String,Object> m = readLogin();
            if(m != null){
                bundle = new Bundle();
                user = new User();
                user.setAccount(String.valueOf(m.get("account")));
                user.setPassword(String.valueOf(m.get("password")));
                user.setPhoto(String.valueOf(m.get("photo")));
                bundle.putSerializable("user",user);
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        //实现页面延时跳转
                        startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtras(bundle));
                        return false;
                    }
                }).sendEmptyMessageDelayed(0,2000); //2秒延时
                Toast.makeText(LoginActivity.this,"正在登录....",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        loginIv = (CircleImageView) findViewById(R.id.login_iv_head);
        account = (EditText) findViewById(R.id.login_account);
        password = (EditText) findViewById(R.id.login_pd);
        ckRember = (CheckBox) findViewById(R.id.login_rem);
        ckAuto = (CheckBox) findViewById(R.id.login_auto);
        login = (Button) findViewById(R.id.login_btn_lg);
        regist = (Button) findViewById(R.id.login_btn_regist);
    }

    private void initViewListenter() {
        login.setOnClickListener(this);
        regist.setOnClickListener(this);
    }

    //保存账号和密码
    public void configLoginInfo(boolean checked,boolean autologin){
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putBoolean("ckRem",ckRember.isChecked());
        editor.putBoolean("autologin",autologin);
        if(checked){
            editor.putString("photo",user.getPhoto());
            editor.putString("account",user.getAccount());
            editor.putString("password",user.getPassword());
        }else {
            editor.remove("photo").remove("account").remove("password");
        }
        editor.commit();
    }

    private Map<String, Object> readLogin() {
        Map<String,Object> m = new HashMap<>();
        String account = loginPreference.getString("account","");
        String password = loginPreference.getString("password","");
        String photo = loginPreference.getString("photo","");
        m.put("account",account);
        m.put("password",password);
        m.put("photo",photo);
        return m;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn_lg:
                String accountStr = account.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                bundle = new Bundle();
                user = userDao.selectUser(accountStr,passwordStr);
                if(user != null) {
                    final Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                    loginIv.setImageResource(Integer.parseInt(user.getPhoto()));
                    bundle.putSerializable("user",user);
                    intent1.putExtras(bundle);
                    configLoginInfo(ckRember.isChecked(),ckAuto.isChecked());   //存储登录信息到内存中
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            //实现页面延时跳转
                            startActivity(intent1);
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0,3000); //3秒延时
                    Toast.makeText(LoginActivity.this,"正在登录....",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this,"账号和密码输入有误！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_btn_regist:
                Intent intent = new Intent(LoginActivity.this,RegisteActivity.class);
                startActivity(intent);
                break;
        }
    }

}
