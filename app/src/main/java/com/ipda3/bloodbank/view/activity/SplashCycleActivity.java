package com.ipda3.bloodbank.view.activity;

import android.os.Bundle;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.view.fragment.splashCycle.SplashFragment;

import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class SplashCycleActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cycle);

        replaceFragment(getSupportFragmentManager(), R.id.splash_cycle_activity, new SplashFragment());
    }
}
