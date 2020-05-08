package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.NotificationAdapter;
import com.ipda3.bloodbank.data.model.notificationsList.NotificationsList;
import com.ipda3.bloodbank.data.model.notificationsList.NotificationsListData;
import com.ipda3.bloodbank.helper.OnEndLess;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

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

public class NotificationsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.notifications_fragment_rv_notifications_list)
    RecyclerView notificationsFragmentRvNotificationsList;
    @BindView(R.id.notifications_fragment_iv_no_notification)
    ImageView notificationsFragmentIvNoNotification;
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage;
    private NotificationAdapter notificationAdapter;
    private List<NotificationsListData> notificationsListDataList = new ArrayList<>();

    public NotificationsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    public void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        notificationsFragmentRvNotificationsList.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getNotificationsList(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        notificationsFragmentRvNotificationsList.addOnScrollListener(onEndLess);
        notificationAdapter = new NotificationAdapter(getActivity(), notificationsListDataList);
        notificationsFragmentRvNotificationsList.setAdapter(notificationAdapter);

//        Toast.makeText(getActivity(), notificationAdapter.getItemCount() + "", Toast.LENGTH_LONG).show();
//        if (notificationAdapter.getItemCount() == 0) {
//            notificationsFragmentIvNoNotification.setVisibility(View.GONE);
//        }else {
//            notificationsFragmentIvNoNotification.setVisibility(View.VISIBLE);
//        }

        getNotificationsList(1);
    }

    public void getNotificationsList(int page) {
        Call<NotificationsList> notificationsListCall = getClient().notificationsList(LoadData(getActivity(), API_TOKEN), page);
        startCall(notificationsListCall);
    }

    private void startCall(Call<NotificationsList> notificationsListCall) {
        notificationsListCall.enqueue(new Callback<NotificationsList>() {
            @Override
            public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        maxPage = response.body().getData().getLastPage();
                        notificationsListDataList.addAll(response.body().getData().getData());
                        notificationAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationsList> call, Throwable t) {

            }
        });
    }
}
