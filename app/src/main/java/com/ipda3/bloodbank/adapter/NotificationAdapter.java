package com.ipda3.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.notificationsList.NotificationsListData;
import com.ipda3.bloodbank.view.activity.BaseActivity;
import com.ipda3.bloodbank.view.fragment.homeCycle.DonationDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<NotificationsListData> notificationsListDataList = new ArrayList<>();
    private ClientData clientData;
    private String lang;
    private DonationDetailsFragment donationDetailsFragment = new DonationDetailsFragment();

    public NotificationAdapter(Activity context, List<NotificationsListData> notificationsListDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.notificationsListDataList = notificationsListDataList;

        clientData = loadUserData(activity);
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.notificationItemTvNotificationTitle.setText(notificationsListDataList.get(position).getTitle());
        holder.notificationItemTvNotificationDate.setText(notificationsListDataList.get(position).getCreatedAt());
        if(notificationsListDataList.get(position).getPivot().getIsRead().equals("0")){
            holder.notificationItemTvNotificationTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bell_filled, 0, 0, 0);
        }else{
            holder.notificationItemTvNotificationTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bell_empty, 0, 0, 0);
        }

        holder.notificationItemLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationsListDataList.get(position).getPivot().getIsRead().equals("0")) {
                    notificationsListDataList.get(position).getPivot().setIsRead("1");
                    holder.notificationItemTvNotificationTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bell_empty, 0, 0, 0);
                }
            }
        });
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notificationsListDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.notification_item_tv_notification_title)
        TextView notificationItemTvNotificationTitle;
        @BindView(R.id.notification_item_tv_notification_date)
        TextView notificationItemTvNotificationDate;
        @BindView(R.id.notification_item_ll_item)
        LinearLayout notificationItemLlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}