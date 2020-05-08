package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.NotificationSettingAdapter;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ipda3.bloodbank.data.model.generalResponse.GeneralResponseData;
import com.ipda3.bloodbank.data.model.notificationSettings.NotificationSettings;
import com.ipda3.bloodbank.helper.InternetState;
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
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.dismissProgressDialog;
import static com.ipda3.bloodbank.helper.HelperMethod.setInitRecyclerViewAsGridLayoutManager;
import static com.ipda3.bloodbank.helper.HelperMethod.showProgressDialog;

public class NotificationSettingsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.notification_settings_fragment_tv_title)
    TextView notificationSettingsFragmentTvTitle;
    @BindView(R.id.notification_settings_fragment_rel_bloods_gone)
    RelativeLayout notificationSettingsFragmentRelBloodsGone;
    @BindView(R.id.notification_settings_fragment_iv_blood_types_plus_minus)
    ImageView notificationSettingsFragmentIvBloodTypesPlusMinus;
    @BindView(R.id.notification_settings_fragment_rel_blood_types)
    RelativeLayout notificationSettingsFragmentRelBloodTypes;
    @BindView(R.id.notification_settings_fragment_rel_bloods)
    RelativeLayout notificationSettingsFragmentRelBloods;
    @BindView(R.id.notification_settings_fragment_rel_governorates_gone)
    RelativeLayout notificationSettingsFragmentRelGovernoratesGone;
    @BindView(R.id.notification_settings_fragment_iv_governments_plus_minus)
    ImageView notificationSettingsFragmentIvGovernmentsPlusMinus;
    @BindView(R.id.notification_settings_fragment_rel_governorates)
    RelativeLayout notificationSettingsFragmentRelGovernorates;
    @BindView(R.id.notification_settings_fragment_rel)
    RelativeLayout notificationSettingsFragmentRel;
    @BindView(R.id.notification_settings_fragment_rv_blood_types)
    RecyclerView notificationSettingsFragmentRvBloodTypes;
    @BindView(R.id.notification_settings_fragment_rv_governorates)
    RecyclerView notificationSettingsFragmentRvGovernorates;


    private GridLayoutManager gridLayoutManager;
    private NotificationSettingAdapter bloodsAdapter, GovernAdapter;

    private List<GeneralResponseData> governoratesList = new ArrayList<>();
    private List<GeneralResponseData> bloodsList = new ArrayList<>();
    private List<String> oldBloodTypes = new ArrayList<>();
    private List<String> oldGovernorates = new ArrayList<>();
    private ClientData clientData;


    public NotificationSettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        onCall(true);

        return view;
    }

    private void onCall(final boolean state) {

        if (InternetState.isConnected(getActivity())) {
            showProgressDialog(getActivity(), getString(R.string.wait));

            Call<NotificationSettings> call;

            if (state) {
                call = getClient().getNotificationSettings(clientData.getApiToken());
            } else {
                call = getClient().changeNotificationSettings(clientData.getApiToken(), GovernAdapter.ids, bloodsAdapter.ids);
            }

            call.enqueue(new Callback<NotificationSettings>() {
                @Override
                public void onResponse(Call<NotificationSettings> call, Response<NotificationSettings> response) {
                    try {

                        dismissProgressDialog();
                        if (state) {

                            if (response.body().getStatus() == 1) {

                                oldBloodTypes = response.body().getData().getBloodTypes();
                                oldGovernorates = response.body().getData().getGovernorates();
                                getBloodTypes();
                                getGovernorates();

                            } else {
                            }

                        } else {
                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<NotificationSettings> call, Throwable t) {
                    dismissProgressDialog();
                }
            });

        } else {
            dismissProgressDialog();
        }

    }

    private void getBloodTypes() {

        setInitRecyclerViewAsGridLayoutManager(getActivity(), notificationSettingsFragmentRvBloodTypes, gridLayoutManager, 3);

        if (InternetState.isConnected(getActivity())) {
            Call<GeneralResponse> call;

            call = getClient().bloodTypesList();
            call.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    try {
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            if (bloodsList.size() == 0) {
                                visible(notificationSettingsFragmentRelBloodsGone, notificationSettingsFragmentIvBloodTypesPlusMinus);
                                bloodsList = new ArrayList<>();
                                bloodsList.addAll(response.body().getData());
                                bloodsAdapter = new NotificationSettingAdapter(getActivity(), getActivity(), bloodsList, oldBloodTypes);
                                notificationSettingsFragmentRvBloodTypes.setAdapter(bloodsAdapter);
                            }
                        } else {
                            dismissProgressDialog();

                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    dismissProgressDialog();
                }
            });
        } else {
            dismissProgressDialog();
        }

    }

    private void getGovernorates() {

        setInitRecyclerViewAsGridLayoutManager(getActivity(), notificationSettingsFragmentRvGovernorates, gridLayoutManager, 3);

        if (InternetState.isConnected(getActivity())) {
            Call<GeneralResponse> call;

            call = getClient().governmentsList();
            call.enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    try {
                        dismissProgressDialog();
                        if (response.body().getStatus() == 1) {
                            if (governoratesList.size() == 0) {
                                visible(notificationSettingsFragmentRelGovernoratesGone, notificationSettingsFragmentIvGovernmentsPlusMinus);
                                bloodsList = new ArrayList<>();
                                governoratesList.addAll(response.body().getData());
                                GovernAdapter = new NotificationSettingAdapter(getActivity(), getActivity(), governoratesList, oldGovernorates);
                                notificationSettingsFragmentRvGovernorates.setAdapter(GovernAdapter);
                            }
                        } else {
                            dismissProgressDialog();
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    dismissProgressDialog();
                }
            });
        } else {
            dismissProgressDialog();
        }

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

    @OnClick({R.id.notification_settings_fragment_iv_blood_types_plus_minus, R.id.notification_settings_fragment_iv_governments_plus_minus, R.id.notification_settings_fragment_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notification_settings_fragment_iv_blood_types_plus_minus:
                visible(notificationSettingsFragmentRelBloodsGone, notificationSettingsFragmentIvBloodTypesPlusMinus);
                break;
            case R.id.notification_settings_fragment_iv_governments_plus_minus:
                visible(notificationSettingsFragmentRelGovernoratesGone, notificationSettingsFragmentIvGovernmentsPlusMinus);
                break;
            case R.id.notification_settings_fragment_btn_save:
                onCall(false);
                break;
        }
    }

    private void visible(View view, ImageView imageView) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_minus);

        } else {
            imageView.setImageResource(R.drawable.ic_plus_white);
            view.setVisibility(View.GONE);
        }
    }
}
