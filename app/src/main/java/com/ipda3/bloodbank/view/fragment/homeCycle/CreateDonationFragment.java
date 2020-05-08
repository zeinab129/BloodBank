package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.SpinnerAdapter;
import com.ipda3.bloodbank.data.model.createDonationRequest.CreateDonationRequest;
import com.ipda3.bloodbank.view.activity.MapsActivity;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

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
import static com.ipda3.bloodbank.helper.GeneralRequest.getData;
import static com.ipda3.bloodbank.view.activity.MapsActivity.hospital_address;
import static com.ipda3.bloodbank.view.activity.MapsActivity.latitude;
import static com.ipda3.bloodbank.view.activity.MapsActivity.longitude;

public class CreateDonationFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.toolbar_back)
    ImageButton toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.create_donation_fragment_et_name)
    EditText createDonationFragmentEtName;
    @BindView(R.id.create_donation_fragment_et_age)
    EditText createDonationFragmentEtAge;
    @BindView(R.id.create_donation_fragment_sp_blood_type_spinner)
    Spinner createDonationFragmentSpBloodTypeSpinner;
    @BindView(R.id.create_donation_fragment_et_bags_num)
    EditText createDonationFragmentEtBagsNum;
    @BindView(R.id.create_donation_fragment_et_hospital_address)
    EditText createDonationFragmentEtHospitalAddress;
    @BindView(R.id.create_donation_fragment_sp_government_spinner)
    Spinner createDonationFragmentSpGovernmentSpinner;
    @BindView(R.id.create_donation_fragment_sp_city_spinner)
    Spinner createDonationFragmentSpCitySpinner;
    @BindView(R.id.create_donation_fragment_et_phone_number)
    EditText createDonationFragmentEtPhoneNumber;
    @BindView(R.id.create_donation_fragment_et_notes)
    EditText createDonationFragmentEtNotes;
    @BindView(R.id.create_donation_fragment_rl_city_layout)
    RelativeLayout createDonationFragmentRlCityLayout;
    @BindView(R.id.create_donation_fragment_et_hospital_name)
    EditText createDonationFragmentEtHospitalName;

    private SpinnerAdapter bloodTypeSpinner, governmentSpinner, citySpinner;
    private AdapterView.OnItemSelectedListener listener;

    public CreateDonationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_donation, container, false);
        unbinder = ButterKnife.bind(this, view);

        toolbarBack.setVisibility(View.GONE);
        toolbarTitle.setText(R.string.donation_request);

        setSpinners();

        return view;
    }

    private void setSpinners() {
        bloodTypeSpinner = new SpinnerAdapter(getActivity());
        governmentSpinner = new SpinnerAdapter(getActivity());
        citySpinner = new SpinnerAdapter(getActivity());


        getData(getClient().bloodTypesList(), bloodTypeSpinner, getString(R.string.blood_type), createDonationFragmentSpBloodTypeSpinner);

        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    createDonationFragmentRlCityLayout.setVisibility(View.VISIBLE);
                    getData(getClient().citiesLst(governmentSpinner.selectedId), citySpinner, getString(R.string.city), createDonationFragmentSpCitySpinner);
                } else {
                    createDonationFragmentRlCityLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getData(getClient().governmentsList(), governmentSpinner, getString(R.string.government), createDonationFragmentSpGovernmentSpinner, listener);
    }


    @OnClick({R.id.create_donation_fragment_et_hospital_address, R.id.create_donation_fragment_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_donation_fragment_et_hospital_address:
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.create_donation_fragment_btn_send:
                onCall();
                break;
        }
    }

    public void onCall() {
        String name = createDonationFragmentEtName.getText().toString().trim();
        int age = Integer.parseInt(createDonationFragmentEtAge.getText().toString().trim());
        int bloodTypeId = bloodTypeSpinner.selectedId;
        int bagsNum = Integer.parseInt(createDonationFragmentEtBagsNum.getText().toString().trim());
        String hospitalName = createDonationFragmentEtHospitalName.getText().toString().trim();
        final String hospitalAdd = createDonationFragmentEtHospitalAddress.getText().toString().trim();
        int cityId = citySpinner.selectedId;
        String phone = createDonationFragmentEtPhoneNumber.getText().toString().trim();
        String notes = createDonationFragmentEtNotes.getText().toString().trim();

        Call<CreateDonationRequest> createDonationRequestCall = getClient().createDonationRequest(LoadData(getActivity(), API_TOKEN), name, age, bloodTypeId, bagsNum, hospitalName, hospitalAdd, cityId, phone, notes, latitude, longitude);

        createDonationRequestCall.enqueue(new Callback<CreateDonationRequest>() {
            @Override
            public void onResponse(Call<CreateDonationRequest> call, Response<CreateDonationRequest> response) {
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CreateDonationRequest> call, Throwable t) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (hospital_address != null) {
            createDonationFragmentEtHospitalAddress.setText(hospital_address);
        }
    }
}
