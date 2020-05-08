package com.ipda3.bloodbank.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ipda3.bloodbank.view.activity.BaseActivity;
import com.ipda3.bloodbank.view.activity.HomeCycleActivity;

public class BaseFragment extends Fragment {

    public BaseActivity baseActivity;
    public HomeCycleActivity homeCycleActivity;

    public void setUpActivity() {
        baseActivity = (BaseActivity) getActivity();
        baseActivity.baseFragment = this;

        try {
            homeCycleActivity = (HomeCycleActivity) getActivity();
        } catch (Exception e) {

        }
    }

    public void onBack() {
        baseActivity.superBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setUpActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
