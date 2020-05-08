package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.donations.DonationsData;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ipda3.bloodbank.helper.HelperMethod.onPermission;

public class DonationDetailsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.donation_details_tv_name)
    TextView donationDetailsTvName;
    @BindView(R.id.donation_details_tv_age)
    TextView donationDetailsTvAge;
    @BindView(R.id.donation_details_tv_blood_type)
    TextView donationDetailsTvBloodType;
    @BindView(R.id.donation_details_tv_bags_num)
    TextView donationDetailsTvBagsNum;
    @BindView(R.id.donation_details_tv_hospital)
    TextView donationDetailsTvHospital;
    @BindView(R.id.donation_details_tv_address)
    TextView donationDetailsTvAddress;
    @BindView(R.id.donation_details_tv_phone)
    TextView donationDetailsTvPhone;
    @BindView(R.id.donation_details_fragment_tv_notes)
    TextView donationDetailsFragmentTvNotes;
    @BindView(R.id.donation_details_fragment_mv_map)
    MapView donationDetailsFragmentMvMap;
    @BindView(R.id.toolbar_back)
    ImageButton toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.donation_details_fragment_btn_call)
    Button donationDetailsFragmentBtnCall;

    public int donationId;
    public DonationsData donationsData;

    public DonationDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_donation_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        donationDetailsFragmentMvMap.onCreate(savedInstanceState);

        setDonationDetails();

        return view;
    }


    public void setDonationDetails() {
        donationDetailsTvName.setText(getString(R.string.name) + ": " + donationsData.getPatientName());
        donationDetailsTvAge.setText(getString(R.string.age) + ": " + donationsData.getPatientAge());
        donationDetailsTvBloodType.setText(getString(R.string.blood_type) + ": " + donationsData.getBloodType().getName());
        donationDetailsTvBagsNum.setText(getString(R.string.bags_num) + ": " + donationsData.getBagsNum());
        donationDetailsTvHospital.setText(getString(R.string.hospital) + ": " + donationsData.getHospitalName());
        donationDetailsTvAddress.setText(getString(R.string.address) + ": " + donationsData.getHospitalAddress());
        donationDetailsTvPhone.setText(getString(R.string.phone) + ": " + donationsData.getPhone());
        donationDetailsFragmentTvNotes.setText(getString(R.string.notes) + ": " + donationsData.getNotes());

        toolbarTitle.setText(donationsData.getPatientName());

        donationDetailsFragmentMvMap.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        donationDetailsFragmentMvMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                try {

                    MarkerOptions currentUserLocation = new MarkerOptions();
                    currentUserLocation.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));

                    LatLng currentUserLatLang = new LatLng(Double.parseDouble(donationsData.getLatitude()), Double.parseDouble(donationsData.getLongitude()));
                    currentUserLocation.position(currentUserLatLang);
                    googleMap.addMarker(currentUserLocation);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, 16));

                    float zoom = 10;
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLatLang, zoom));

                } catch (Exception e) {

                }
            }
        });

    }

    @OnClick({R.id.donation_details_fragment_btn_call, R.id.toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.donation_details_fragment_btn_call:
                onPermission(getActivity());
                getActivity().startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", donationsData.getPhone(), null)));
                break;

            case R.id.toolbar_back:
                onBack();
                break;
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

}
