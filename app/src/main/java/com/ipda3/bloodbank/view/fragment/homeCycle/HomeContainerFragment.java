package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.ViewPagerWithFragmentAdapter;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeContainerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.home_container_fragment_tl_tap)
    TabLayout homeContainerFragmentTlTap;
    @BindView(R.id.home_container_fragment_vp_view_pager)
    ViewPager homeContainerFragmentVpViewPager;

    PostsListFragment postsListFragment;
    DonationsListFragment donationsListFragment;

    public HomeContainerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);
        unbinder = ButterKnife.bind(this, view);

        ViewPagerWithFragmentAdapter adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        postsListFragment = new PostsListFragment();
        donationsListFragment = new DonationsListFragment();

        donationsListFragment.homeContainerFragment = this;
        adapter.addPager(postsListFragment, getString(R.string.posts));
        adapter.addPager(donationsListFragment, getString(R.string.donations));





        homeContainerFragmentVpViewPager.setAdapter(adapter);
        homeContainerFragmentTlTap.setupWithViewPager(homeContainerFragmentVpViewPager);

        return view;
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
