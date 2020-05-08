package com.ipda3.bloodbank.view.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.SliderAdapter;
import com.ipda3.bloodbank.view.activity.HomeCycleActivity;
import com.ipda3.bloodbank.view.activity.UserCycleActivity;
import com.ipda3.bloodbank.view.fragment.BaseFragment;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SliderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.slider_fragment_vp_slides)
    ViewPager sliderFragmentVpSlides;
    @BindView(R.id.slider_fragment_ll_dots)
    LinearLayout sliderFragmentLlDots;

    SliderAdapter slideAdapter;
    @BindView(R.id.slider_fragment_ib_next)
    ImageButton sliderFragmentIbNext;
    private int dotsCount;
    private ImageView[] dots;
    private int curPos;


    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        unbinder = ButterKnife.bind(this, view);
        StatusBarUtil.setTranslucent(getActivity());

        slideAdapter = new SliderAdapter(getActivity());
        sliderFragmentVpSlides.setAdapter(slideAdapter);

        dotsCount = slideAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shape_nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            sliderFragmentLlDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shape_active_dot));

        sliderFragmentVpSlides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                curPos = position;
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shape_nonactive_dot));
                }

                dots[position].setImageDrawable(getActivity().getResources().getDrawable(R.drawable.shape_active_dot));

                if (position == dotsCount - 1) {
                    sliderFragmentIbNext.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_correct));
                } else {
                    sliderFragmentIbNext.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_next));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }

    @OnClick(R.id.slider_fragment_ib_next)
    public void onViewClicked() {
        if(curPos == dotsCount-1){
            Intent intent = new Intent(getContext(), UserCycleActivity.class);
            startActivity(intent);
        }
        else if (curPos < dotsCount - 1){
            sliderFragmentVpSlides.setCurrentItem(getItem(+1), true);
        }
    }

    private int getItem(int i) {
        return sliderFragmentVpSlides.getCurrentItem() + i;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }


}
