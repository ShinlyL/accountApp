package com.example.administrator.accountbook;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    RadioGroup mainRg;
    Fragment bookFrag,tjFrag,meFrag;
    private FragmentManager manager;
    private User user;
    private UserDao userDao;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainRg = (RadioGroup) findViewById(R.id.main_rg);
        // 设置监听点击了那个单选按钮
        mainRg.setOnCheckedChangeListener(this);
        //传递来自注册页面的用户相关信息
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        userDao = new UserDao(MainActivity.this);
        user = userDao.selectUser(user.getAccount(),user.getPassword());
        bundle = new Bundle();
        bundle.putSerializable("user",user);
        //创建碎片对象
        bookFrag = new bookFragment();
        bookFrag.setArguments(bundle);
        tjFrag = new tjFragment();
        tjFrag.setArguments(bundle);
        meFrag = new meFragment();
        meFrag.setArguments(bundle);
        //将四个Fragment进行动态加载，add/hide/show
        addFragmentPage();
    }
    /**
     * @des 将主页当中的碎片一起加载进入布局中，展示对应的碎片，其他的隐藏
     */
    private void addFragmentPage(){
        //1.创建碎片管理者对象
        manager = getSupportFragmentManager();
        //2.创建碎片处理事务的对象
        FragmentTransaction transaction = manager.beginTransaction();
        //3.将四个Fragment统一添到布局中
        transaction.add(R.id.main_center,bookFrag);
        transaction.add(R.id.main_center,tjFrag);
        transaction.add(R.id.main_center,meFrag);
        //4.隐藏不需要的碎片
        transaction.hide(tjFrag);
        transaction.hide(meFrag);
        //5.提交碎片改变后的事务
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction transaction = manager.beginTransaction();
        switch (checkedId){
            case R.id.main_rb_book:
                transaction.hide(tjFrag);
                transaction.hide(meFrag);
                transaction.show(bookFrag);
                break;
            case R.id.main_rb_tj:
                transaction.hide(bookFrag);
                transaction.hide(meFrag);
                transaction.show(tjFrag);
                break;
            case R.id.main_rb_me:
                transaction.hide(tjFrag);
                transaction.hide(bookFrag);
                transaction.show(meFrag);
                bundle.putSerializable("user",user);
                break;
        }
        transaction.commit();
    }
}
