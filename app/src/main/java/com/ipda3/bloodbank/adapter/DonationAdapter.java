package com.ipda3.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.donations.DonationsData;
import com.ipda3.bloodbank.view.activity.BaseActivity;
import com.ipda3.bloodbank.view.fragment.homeCycle.DonationDetailsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.onPermission;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<DonationsData> donationsDataList = new ArrayList<>();
    private ClientData clientData;
    private String lang;
    private int currentPosition;

    public DonationAdapter(Activity context, List<DonationsData> donationsDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.donationsDataList = donationsDataList;

        clientData = loadUserData(activity);
        lang = Locale.getDefault().getLanguage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donation,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
        setSwipe(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.donationItemTvPatientName.setText(activity.getString(R.string.patient_name) + ": " + donationsDataList.get(position).getPatientName());
        holder.donationItemTvHospital.setText(activity.getString(R.string.hospital) + ": " + donationsDataList.get(position).getHospitalName());
        holder.donationItemTvCity.setText(activity.getString(R.string.city) + ": " + donationsDataList.get(position).getCity().getName());
        holder.donationItemTvBloodTypeText.setText(donationsDataList.get(position).getBloodType().getName());

        holder.donationItemIbInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonationDetailsFragment donationDetailsFragment = new DonationDetailsFragment();
                donationDetailsFragment.donationId = donationsDataList.get(position).getId();
                donationDetailsFragment.donationsData = donationsDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, donationDetailsFragment);
            }
        });

        holder.donationItemIbCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPermission(activity);
                activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", donationsDataList.get(position).getPhone(), null)));
            }
        });
    }

    private void setAction(ViewHolder holder, int position) {

    }

    private void setSwipe(final ViewHolder holder, final int position) {
        holder.donationItemSrlItem.computeScroll();
        if (lang.contentEquals("ar")) {
            holder.donationItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        } else {
            holder.donationItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
        }

    }


    @Override
    public int getItemCount() {
        return donationsDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        int position;

        @BindView(R.id.donation_item_ib_call)
        ImageButton donationItemIbCall;
        @BindView(R.id.donation_item_ib_info)
        ImageButton donationItemIbInfo;
        @BindView(R.id.donation_item_tv_patient_name)
        TextView donationItemTvPatientName;
        @BindView(R.id.donation_item_tv_hospital)
        TextView donationItemTvHospital;
        @BindView(R.id.donation_item_tv_city)
        TextView donationItemTvCity;
        @BindView(R.id.donation_item_tv_blood_type_text)
        TextView donationItemTvBloodTypeText;
        @BindView(R.id.donation_item_srl_item)
        SwipeRevealLayout donationItemSrlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
