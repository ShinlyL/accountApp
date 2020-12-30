package com.example.administrator.accountbook;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2020-11-7 , 0007.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private int drawableIDS[];
    private int StringIDs[];

    public MyAdapter(Context context,int DrawableIDs[],int StringIDs[]){
        this.context = context;
        this.drawableIDS = DrawableIDs;
        this.StringIDs = StringIDs;
    }

    @Override
    public int getCount() {
        return drawableIDS.length;
    }

    @Override
    public Object getItem(int position) {
        return drawableIDS[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.CENTER_VERTICAL);
        ImageView iv = new ImageView(context);
        iv.setImageResource(drawableIDS[position]);
        iv.setLayoutParams(new ViewGroup.LayoutParams(100,80));
        ll.addView(iv);
        TextView tv = new TextView(context);
        tv.setText(StringIDs[position]);
        tv.setTextSize(24);
        tv.setTextColor(Color.BLUE);
        tv.setTag("tag TextView");
        ll.addView(tv);
        return ll;
    }
}
