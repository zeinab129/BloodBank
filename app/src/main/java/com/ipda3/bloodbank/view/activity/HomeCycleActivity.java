package com.ipda3.bloodbank.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.notificationCount.NotificationCount;
import com.ipda3.bloodbank.view.fragment.homeCycle.HomeContainerFragment;
import com.ipda3.bloodbank.view.fragment.homeCycle.MoreFragment;
import com.ipda3.bloodbank.view.fragment.homeCycle.NotificationsFragment;
import com.ipda3.bloodbank.view.fragment.userCycle.RegisterAndEditProfileFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class HomeCycleActivity extends BaseActivity {

    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_back)
    public ImageButton toolbarBack;
    @BindView(R.id.toolbar_title)
    public TextView toolbarTitle;
    @BindView(R.id.toolbar_sub_view)
    RelativeLayout toolbarSubView;
    @BindView(R.id.home_cycle_activity_fl_home_frame)
    FrameLayout homeCycleActivityFlHomeFrame;
    @BindView(R.id.home_cycle_activity_iv_home)
    ImageView homeCycleActivityIvHome;
    @BindView(R.id.home_cycle_activity_rb_home)
    RadioButton homeCycleActivityRbHome;
    @BindView(R.id.home_cycle_activity_iv_profile)
    ImageView homeCycleActivityIvProfile;
    @BindView(R.id.home_cycle_activity_rb_profile)
    RadioButton homeCycleActivityRbProfile;
    @BindView(R.id.home_cycle_activity_iv_notification)
    ImageView homeCycleActivityIvNotification;
    @BindView(R.id.home_cycle_activity_rb_notification)
    RadioButton homeCycleActivityRbNotification;
    @BindView(R.id.home_cycle_activity_tv_notification_count)
    TextView homeCycleActivityTvNotificationCount;
    @BindView(R.id.home_cycle_activity_iv_more)
    ImageView homeCycleActivityIvMore;
    @BindView(R.id.home_cycle_activity_rb_more)
    RadioButton homeCycleActivityRbMore;
    @BindView(R.id.home_cycle_activity_rg_navigation)
    RadioGroup homeCycleActivityRgNavigation;
    @BindView(R.id.home_cycle_activity)
    LinearLayout homeCycleActivity;

    public HomeContainerFragment homeContainerFragment;
    public RegisterAndEditProfileFragment registerAndEditProfileFragment;
    public NotificationsFragment notificationsFragment;
    public MoreFragment moreFragment;

    public int notificationsCount;

    private List<ImageView> imageViews = new ArrayList<>();
    public ClientData clientData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);
        unbinder = ButterKnife.bind(this);

        imageViews.add(homeCycleActivityIvHome);
        imageViews.add(homeCycleActivityIvProfile);
        imageViews.add(homeCycleActivityIvNotification);
        imageViews.add(homeCycleActivityIvMore);

        homeContainerFragment = new HomeContainerFragment();
        registerAndEditProfileFragment = new RegisterAndEditProfileFragment();
        registerAndEditProfileFragment.PROFILE = true;
        notificationsFragment = new NotificationsFragment();
        moreFragment = new MoreFragment();


        replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, homeContainerFragment);

        OnCheckedChangeListener(homeCycleActivityRbHome, homeContainerFragment, 0);
        OnCheckedChangeListener(homeCycleActivityRbProfile, registerAndEditProfileFragment, 1);
        OnCheckedChangeListener(homeCycleActivityRbNotification, notificationsFragment, 2);
        OnCheckedChangeListener(homeCycleActivityRbMore, moreFragment, 3);

        moreFragment.homeCycleActivity = this;

        clientData = loadUserData(this);
        getNotificationCount();

        setNavigation(View.VISIBLE, R.id.home_cycle_activity_rb_home);

    }

    private void getNotificationCount() {
        Call<NotificationCount> notificationCountCall = getClient().getNotificationsCounter(LoadData(this, API_TOKEN));
        notificationCountCall.enqueue(new Callback<NotificationCount>() {
            @Override
            public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        notificationsCount = Integer.parseInt(response.body().getData().getNotificationsCount().toString());
                        setNotificationsCounter(notificationsCount);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<NotificationCount> call, Throwable t) {

            }
        });
    }

    private void setNotificationsCounter(Integer notificationsCount) {
        if (notificationsCount == 0) {
            homeCycleActivityTvNotificationCount.setVisibility(View.INVISIBLE);
        } else if (notificationsCount > 0 && notificationsCount <= 9) {
            homeCycleActivityTvNotificationCount.setVisibility(View.VISIBLE);
            homeCycleActivityTvNotificationCount.setText(notificationsCount);
        } else {
            homeCycleActivityTvNotificationCount.setVisibility(View.VISIBLE);
            homeCycleActivityTvNotificationCount.setText(R.string.max_notification);
        }
    }

    public void setToolBar(int visibility, String title, View.OnClickListener backActionBtn) {
        toolbarSubView.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            toolbarTitle.setText(title);
            toolbarBack.setOnClickListener(backActionBtn);
        }

    }

    public void setNavigation(int visibility, int id) {

        homeCycleActivityRgNavigation.setVisibility(visibility);

        if (id!= 0) {
            homeCycleActivityRgNavigation.check(id);

            switch (id) {
                case R.id.home_cycle_activity_rb_home:
                    setSelection(0);
                    break;
                case R.id.home_cycle_activity_rb_profile:
                    setSelection(1);
                    break;
                case R.id.home_cycle_activity_rb_notification:
                    setSelection(2);
                    break;
                case R.id.home_cycle_activity_rb_more:
                    setSelection(3);
                    break;
            }
        }


    }

    private void OnCheckedChangeListener(final RadioButton radioButton, final Fragment fragment, final int i) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButton.isChecked()) {
                    setSelection(i);
                    replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, fragment);
                }
            }
        });
    }

    private void setSelection(int i) {
        for (int j = 0; j < imageViews.size(); j++) {
            if (i == j) {
                imageViews.get(j).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(j).setVisibility(View.INVISIBLE);
            }
        }

        if(i == 0 || i == 1){
            setToolBar(View.GONE, null, null);
        }
        else if(i == 2){
            setToolBar(View.VISIBLE, getString(R.string.notifications), null);
            toolbarBack.setVisibility(View.INVISIBLE);
        }
        else if(i == 3){
            setToolBar(View.VISIBLE, getString(R.string.more), null);
            toolbarBack.setVisibility(View.INVISIBLE);
        }


    }
}
