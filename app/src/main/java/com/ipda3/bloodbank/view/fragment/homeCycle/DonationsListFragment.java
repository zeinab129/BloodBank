package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.DonationAdapter;
import com.ipda3.bloodbank.adapter.SpinnerAdapter;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.donations.Donations;
import com.ipda3.bloodbank.data.model.donations.DonationsData;
import com.ipda3.bloodbank.helper.OnEndLess;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.GeneralRequest.getData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class DonationsListFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.donation_list_fragment_sp_government)
    Spinner donationListFragmentSpGovernment;
    @BindView(R.id.donation_list_fragment_sp_blood_type)
    Spinner donationListFragmentSpBloodType;
    @BindView(R.id.donation_list_fragment_rv_list)
    RecyclerView donationListFragmentRvList;


    public HomeContainerFragment homeContainerFragment;


    private SpinnerAdapter bloodTypeAdapter, governmentAdapter;
    private LinearLayoutManager linearLayout;
    private DonationAdapter donationAdapter;
    private List<DonationsData> donationsDataList = new ArrayList<>();
    private OnEndLess onEndLess;
    private ClientData clientData;
    private Integer maxPage = 0;
    private boolean filter = false;


    public DonationsListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_donations_list, container, false);
        unbinder = ButterKnife.bind(this, view);


        clientData = loadUserData(getActivity());

        setSpinners();
        init();

        return view;
    }

    public void init() {

        linearLayout = new LinearLayoutManager(getActivity());
        donationListFragmentRvList.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        if (filter) {
                            onFilter(current_page);
                        } else {
                            getDonations(current_page);
                        }
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        donationListFragmentRvList.addOnScrollListener(onEndLess);
        donationAdapter = new DonationAdapter(getActivity(), donationsDataList);
        donationListFragmentRvList.setAdapter(donationAdapter);

        getDonations(1);

    }


    public void setSpinners() {
        bloodTypeAdapter = new SpinnerAdapter(getActivity());
        governmentAdapter = new SpinnerAdapter(getActivity());

        getData(getClient().bloodTypesList(), bloodTypeAdapter, getString(R.string.blood_type), donationListFragmentSpBloodType);
        getData(getClient().governmentsList(), governmentAdapter, getString(R.string.government), donationListFragmentSpGovernment);
    }

    public void getDonations(int page) {
        Call<Donations> donationsCall = getClient().donationRequestsList(LoadData(getActivity(), API_TOKEN), page);
        startCall(donationsCall, page);
    }

    private void onFilter(int current_page) {
        filter = true;
        Call<Donations> donationsCall = getClient().donationsFilteredList(
                LoadData(getActivity(), API_TOKEN), current_page, bloodTypeAdapter.selectedId, governmentAdapter.selectedId);
        startCall(donationsCall, current_page);
    }

    private void startCall(Call<Donations> donationsCall, int page) {
        if (page == 1) {
            reInit();
        }
        donationsCall.enqueue(new Callback<Donations>() {
            @Override
            public void onResponse(Call<Donations> call, Response<Donations> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        //Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        maxPage = response.body().getData().getLastPage();
                        donationsDataList.addAll(response.body().getData().getData());
                        donationAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Donations> call, Throwable t) {

            }
        });
    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        donationsDataList = new ArrayList<>();
        donationAdapter = new DonationAdapter(getActivity(), donationsDataList);
        donationListFragmentRvList.setAdapter(donationAdapter);
    }

    @OnClick({R.id.donations_list_fragment_f_a_btn_create_donation, R.id.donation_list_fragment_ib_filter})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.donations_list_fragment_f_a_btn_create_donation:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, new CreateDonationFragment());
                break;
            case R.id.donation_list_fragment_ib_filter:
                if (bloodTypeAdapter.selectedId == 0 && governmentAdapter.selectedId == 0 && filter) {
                    filter = false;

                    getDonations(1);
                } else {
                    onFilter(1);
                }

                break;
        }
    }


}
