package com.example.administrator.accountbook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class meFragment extends Fragment implements View.OnClickListener{
    private CircleImageView mefrgiv;
    private TextView mefrgName,mefrgFun,mefrgAbout,mefrgUpdate,mefrgFB;
    private Bundle bundle;
    private User user;
    private UserDao userDao;

    public static final int SELECT_PIC = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        initView(view);
        bundle = getArguments();
        User bundleUser = (User) bundle.getSerializable("user");
        userDao = new UserDao(getContext());
        user = userDao.selectUser(bundleUser.getAccount(), bundleUser.getPassword());

        mefrgiv.setImageResource(Integer.parseInt(user.getPhoto()));
        mefrgName.setText(user.getNickname());

        mefrgiv.setOnClickListener(this);
        mefrgName.setOnClickListener(this);
        mefrgFun.setOnClickListener(this);
        mefrgAbout.setOnClickListener(this);
        mefrgUpdate.setOnClickListener(this);
        mefrgFB.setOnClickListener(this);
        return view;
    }

    private void initView(View v) {
        mefrgiv = v.findViewById(R.id.mefrg_iv);
        mefrgName = v.findViewById(R.id.mefrg_tv_name);
        mefrgAbout = v.findViewById(R.id.mefrg_tv_about);
        mefrgFun = v.findViewById(R.id.mefrg_tv_funciton);
        mefrgUpdate = v.findViewById(R.id.mefrg_tv_update);
        mefrgFB = v.findViewById(R.id.mefrg_tv_feedback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == SELECT_PIC && resultCode == RESULT_OK){
            int imgid = data.getIntExtra("image",-1);
            if(imgid != -1){
                mefrgiv.setImageResource(imgid);
                user.setPhoto(String.valueOf(imgid));
                userDao.updateUser(user);
                Toast.makeText(getContext(),"更换头像成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mefrg_iv:
                Intent intent = new Intent(getActivity(),DialogActivity.class);
                startActivityForResult(intent, SELECT_PIC);
                break;
            case R.id.mefrg_tv_name:
                showInputDialog();
                break;
            case R.id.mefrg_tv_funciton:
                Toast.makeText(getContext(),"功能1.分类记账功能\n功能2.饼图统计功能\n" +
                        "功能3.用户登录注册功能\n功能3.头像更换",Toast.LENGTH_LONG).show();
                break;
            case R.id.mefrg_tv_update:
                Toast.makeText(getContext(),"1.用户登录页面待优化\n2.记账数据待开发存储到网络",Toast.LENGTH_LONG).show();
                break;
            case R.id.mefrg_tv_feedback:
                Toast.makeText(getContext(),"有问题请反馈至邮箱：\n2226761545@qq.com",Toast.LENGTH_LONG).show();
                break;
            case R.id.mefrg_tv_about:
                Toast.makeText(getContext(),"基于Android开发的记账小程序",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showInputDialog(){
        final EditText editText = new EditText(getContext());
        editText.setText(mefrgName.getText());
        AlertDialog.Builder inputDialog=new AlertDialog.Builder(getContext());
        inputDialog.setTitle("昵称");
        inputDialog.setMessage("请输入修改的昵称").setView(editText);
        inputDialog.setPositiveButton("确定输入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mefrgName.setText(editText.getText());
                user.setNickname(editText.getText().toString().trim());
                userDao.updateUser(user);
                Toast.makeText(getContext(),"修改昵称成功",Toast.LENGTH_SHORT).show();
            }
        });
        inputDialog.setNegativeButton("取消输入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        inputDialog.show();
    }
}
