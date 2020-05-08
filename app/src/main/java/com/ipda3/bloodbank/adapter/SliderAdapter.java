package com.ipda3.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ipda3.bloodbank.R;

public class SliderAdapter extends PagerAdapter {
    Activity context;
    private int[] galImages = new int[]{R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
    private int[] slideDesc = new int[]{R.string.slide1_desc, R.string.slide2_desc, R.string.slide3_desc};
    private int currentPos = 0;

    LayoutInflater mLayoutInflater;

    public SliderAdapter(Activity context) {
        this.context = context;

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return galImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            currentPos = position;
            View itemView = mLayoutInflater.inflate(R.layout.slide, container, false);

            ImageView imageView = itemView.findViewById(R.id.slide_iv_image);
            TextView textView = itemView.findViewById(R.id.slide_tv_text);
            imageView.setImageResource(galImages[position]);
            textView.setText(slideDesc[position]);
            container.addView(itemView);

            return itemView;

        } catch (Exception e) {
            return null;
        }
    }

    public int currentPosition(){
        return currentPos;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
