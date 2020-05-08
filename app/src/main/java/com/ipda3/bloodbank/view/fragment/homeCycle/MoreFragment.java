package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.appSettings.AppSettings;
import com.ipda3.bloodbank.view.activity.HomeCycleActivity;
import com.ipda3.bloodbank.view.activity.UserCycleActivity;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.clean;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class MoreFragment extends BaseFragment {

    Unbinder unbinder;
    public HomeCycleActivity homeCycleActivity;

    private ContactUsFragment contactUsFragment = new ContactUsFragment();
    private AboutAppFragment aboutAppFragment = new AboutAppFragment();

    public MoreFragment() {
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
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        unbinder = ButterKnife.bind(this, view);

        onCall();

        return view;
    }

    public void onCall(){
        Call<AppSettings> appSettingsCall = getClient().appSettings(LoadData(getActivity(), API_TOKEN));
        appSettingsCall.enqueue(new Callback<AppSettings>() {
            @Override
            public void onResponse(Call<AppSettings> call, Response<AppSettings> response) {
                if(response.body().getStatus() == 1){
                    contactUsFragment.appSettings = response.body();
                    aboutAppFragment.appSettings = response.body();
                }
            }

            @Override
            public void onFailure(Call<AppSettings> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.more_fragment_tv_favorites, R.id.more_fragment_tv_contact_us, R.id.more_fragment_tv_about_app, R.id.more_fragment_tv_rate_app_on_store, R.id.more_fragment_tv_notifications_settings, R.id.more_fragment_tv_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_fragment_tv_favorites:
                PostsListFragment postsListFragment = new PostsListFragment();
                postsListFragment.favList = true;
                homeCycleActivity.toolbarBack.setVisibility(View.VISIBLE);
                homeCycleActivity.toolbarTitle.setText(getString(R.string.favorites));
                homeCycleActivity.toolbarBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBack();
                    }
                });
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, postsListFragment);
                break;
            case R.id.more_fragment_tv_contact_us:
                homeCycleActivity.toolbarBack.setVisibility(View.VISIBLE);
                homeCycleActivity.toolbarTitle.setText(getString(R.string.contact_us));
                homeCycleActivity.toolbarBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBack();
                    }
                });
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, contactUsFragment);
                break;
            case R.id.more_fragment_tv_about_app:
                homeCycleActivity.toolbarBack.setVisibility(View.VISIBLE);
                homeCycleActivity.toolbarTitle.setText(getString(R.string.about_app));
                homeCycleActivity.toolbarBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBack();
                    }
                });
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, aboutAppFragment);
                break;
            case R.id.more_fragment_tv_rate_app_on_store:
                break;
            case R.id.more_fragment_tv_notifications_settings:
                homeCycleActivity.toolbarBack.setVisibility(View.VISIBLE);
                homeCycleActivity.toolbarBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBack();
                    }
                });
                homeCycleActivity.toolbarTitle.setText(getString(R.string.notifications_settings));
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new NotificationSettingsFragment());
                break;
            case R.id.more_fragment_tv_sign_out:
                clean(getActivity());
                Intent intent = new Intent(getActivity(), UserCycleActivity.class);
                Toast.makeText(getActivity(), R.string.signed_out, Toast.LENGTH_LONG).show();
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        homeCycleActivity.toolbarBack.setVisibility(View.INVISIBLE);
        homeCycleActivity.toolbarTitle.setText(getString(R.string.more));
    }
}
