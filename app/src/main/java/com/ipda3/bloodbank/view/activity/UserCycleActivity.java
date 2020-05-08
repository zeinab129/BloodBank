package com.ipda3.bloodbank.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.view.fragment.userCycle.LoginFragment;

import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class UserCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cycle);
        replaceFragment(getSupportFragmentManager(), R.id.user_cycle_activity, new LoginFragment());
    }
}
