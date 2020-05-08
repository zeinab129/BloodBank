package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.appSettings.AppSettings;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutAppFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.about_app_fragment_tv_text)
    TextView aboutAppFragmentTvText;

    public AppSettings appSettings;

    public AboutAppFragment() {
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
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        unbinder = ButterKnife.bind(this, view);

        aboutAppFragmentTvText.setText(appSettings.getData().getAboutApp());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
