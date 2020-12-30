package com.example.administrator.accountbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogActivity extends AppCompatActivity {
    CircleImageView[] iv = new CircleImageView[9];    //九个头像提供给用户选择
    int[] ids = {R.id.image_01,R.id.image_02,R.id.image_03,R.id.image_04,R.id.image_05,
    R.id.image_06,R.id.image_07,R.id.image_08,R.id.image_09};
    int[] imgId = {R.drawable.drg,R.drawable.baiyang,R.drawable.chunv,R.drawable.jinniu,
    R.drawable.jvxie,R.drawable.mojie,R.drawable.sheshou,R.drawable.shuangyv,R.drawable.tianping};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        for(int i = 0;i < iv.length;i++){
            final int finalI = i;
            iv[i] = (CircleImageView) findViewById(ids[i]);
            //设置点击事件监听
            iv[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //返回数据
                    Intent intent = getIntent();
                    //内部类使用外部局部变量，需要final
                    intent.putExtra("image",imgId[finalI]);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }
    }
}
