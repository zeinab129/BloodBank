package com.ipda3.bloodbank.view.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.view.activity.HomeCycleActivity;
import com.ipda3.bloodbank.view.fragment.BaseFragment;
import com.jaeger.library.StatusBarUtil;

import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.REMEMBER;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class SplashFragment extends BaseFragment {


    public SplashFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_splash, container, false);
        StatusBarUtil.setTranslucent(getActivity());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoadBoolean(getActivity(), REMEMBER) && loadUserData(getActivity()) != null) {
                    Intent intent = new Intent(getActivity(), HomeCycleActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                } else {
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.splash_cycle_activity, new SliderFragment());
                }
            }
        }, 3000);

        return view;
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}
