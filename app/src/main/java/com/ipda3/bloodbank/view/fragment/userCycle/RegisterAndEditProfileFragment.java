package com.ipda3.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.SpinnerAdapter;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.register.Register;
import com.ipda3.bloodbank.helper.DateTxt;
import com.ipda3.bloodbank.view.activity.HomeCycleActivity;
import com.ipda3.bloodbank.view.fragment.BaseFragment;
import com.jaeger.library.StatusBarUtil;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.USER_PASSWORD;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.GeneralRequest.getData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;
import static com.ipda3.bloodbank.helper.HelperMethod.showCalender;

public class RegisterAndEditProfileFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.register_and_edit_profile_fragment_et_name)
    EditText registerAndEditProfileFragmentEtName;
    @BindView(R.id.register_and_edit_profile_fragment_et_email)
    EditText registerAndEditProfileFragmentEtEmail;
    @BindView(R.id.register_and_edit_profile_fragment_et_birth_date)
    EditText registerAndEditProfileFragmentEtBirthDate;
    @BindView(R.id.register_and_edit_profile_fragment_sp_blood_type)
    Spinner registerAndEditProfileFragmentSpBloodType;
    @BindView(R.id.register_and_edit_profile_fragment_et_last_donation_date)
    EditText registerAndEditProfileFragmentEtLastDonationDate;
    @BindView(R.id.register_and_edit_profile_fragment_sp_government)
    Spinner registerAndEditProfileFragmentSpGovernment;
    @BindView(R.id.register_and_edit_profile_fragment_sp_city)
    Spinner registerAndEditProfileFragmentSpCity;
    @BindView(R.id.register_and_edit_profile_fragment_rl_city)
    RelativeLayout registerAndEditProfileFragmentRlCity;
    @BindView(R.id.register_and_edit_profile_fragment_et_phone_number)
    EditText registerAndEditProfileFragmentEtPhoneNumber;
    @BindView(R.id.register_and_edit_profile_fragment_et_password)
    EditText registerAndEditProfileFragmentEtPassword;
    @BindView(R.id.register_and_edit_profile_fragment_et_confirm_password)
    EditText registerAndEditProfileFragmentEtConfirmPassword;
    @BindView(R.id.registers_and_edit_profile_fragment_tv_title)
    TextView registersAndEditProfileFragmentTvTitle;
    @BindView(R.id.register_and_edit_profile_fragment_rl_main_layout)
    RelativeLayout registerAndEditProfileFragmentRlMainLayout;
    @BindView(R.id.register_and_edit_profile_fragment_btn_register)
    Button registerAndEditProfileFragmentBtnRegister;


    private SpinnerAdapter bloodTypeSpinner, governmentSpinner, citySpinner;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private AdapterView.OnItemSelectedListener listener;
    private DateTxt birthdayDate, lastDonationDate;
    public boolean PROFILE = false;
    private HomeCycleActivity homeCycleActivity;
    private ClientData clientData;

    public RegisterAndEditProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register_and_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        StatusBarUtil.setTranslucent(getActivity());

        setSpinners();
        setDates();

        if (PROFILE) {
            setUpActivity();
            clientData = loadUserData(getActivity());
            setUserData();


            registersAndEditProfileFragmentTvTitle.setBackgroundColor(getResources().getColor(R.color.red));
            registerAndEditProfileFragmentRlMainLayout.setBackgroundColor(getResources().getColor(R.color.white));
            registerAndEditProfileFragmentBtnRegister.setBackgroundResource(R.drawable.shape_button_red);
            registerAndEditProfileFragmentBtnRegister.setTextColor(getResources().getColor(R.color.white));
        }

        else {
            registersAndEditProfileFragmentTvTitle.setBackgroundColor(getResources().getColor(R.color.transparent));
            registerAndEditProfileFragmentRlMainLayout.setBackgroundResource(R.drawable.background);
            registerAndEditProfileFragmentBtnRegister.setBackgroundResource(R.drawable.shape_button);
            registerAndEditProfileFragmentBtnRegister.setTextColor(getResources().getColor(R.color.red));
        }



        return view;
    }


    @OnClick({R.id.register_and_edit_profile_fragment_et_birth_date, R.id.register_and_edit_profile_fragment_et_last_donation_date, R.id.register_and_edit_profile_fragment_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_and_edit_profile_fragment_et_birth_date:
                showCalender(getActivity(), getString(R.string.select_birth_date), registerAndEditProfileFragmentEtBirthDate, birthdayDate);
                break;
            case R.id.register_and_edit_profile_fragment_et_last_donation_date:
                showCalender(getActivity(), getString(R.string.select_last_donation_date), registerAndEditProfileFragmentEtLastDonationDate, lastDonationDate);
                break;
            case R.id.register_and_edit_profile_fragment_btn_register:
                register();
                break;
        }
    }


    private void setDates() {

        DecimalFormat mFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calendar.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calendar.get(Calendar.YEAR));

        birthdayDate = new DateTxt("01", "01", "1990", "01-01-1990");
        lastDonationDate = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
    }

    private void setSpinners() {
        bloodTypeSpinner = new SpinnerAdapter(getActivity());
        governmentSpinner = new SpinnerAdapter(getActivity());
        citySpinner = new SpinnerAdapter(getActivity());


        getData(getClient().bloodTypesList(), bloodTypeSpinner, getString(R.string.blood_type), registerAndEditProfileFragmentSpBloodType);

        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    registerAndEditProfileFragmentRlCity.setVisibility(View.VISIBLE);
                    getData(getClient().citiesLst(governmentSpinner.selectedId), citySpinner, getString(R.string.city), registerAndEditProfileFragmentSpCity);
                } else {
                    registerAndEditProfileFragmentRlCity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getData(getClient().governmentsList(), governmentSpinner, getString(R.string.government), registerAndEditProfileFragmentSpGovernment, listener);
    }

    private void register() {

        String name = registerAndEditProfileFragmentEtName.getText().toString();
        String email = registerAndEditProfileFragmentEtEmail.getText().toString();
        String birthDate = registerAndEditProfileFragmentEtBirthDate.getText().toString();
        String phone = registerAndEditProfileFragmentEtPhoneNumber.getText().toString();
        String donationLastDate = registerAndEditProfileFragmentEtLastDonationDate.getText().toString();
        String password = registerAndEditProfileFragmentEtPassword.getText().toString();
        String confirmPassword = registerAndEditProfileFragmentEtConfirmPassword.getText().toString();
        int cityId = citySpinner.selectedId;
        int bloodTypeId = bloodTypeSpinner.selectedId;

        Call<Register> registerCall = getClient().onRegister(name, email, birthDate, cityId, phone, donationLastDate, password, confirmPassword, bloodTypeId);

        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                Register result = response.body();
                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setUserData() {
        bloodTypesSelectedId = clientData.getClient().getBloodType().getId();
        governmentSelectedId = clientData.getClient().getCity().getGovernorate().getId();
        citiesSelectedId = clientData.getClient().getCity().getId();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                registerAndEditProfileFragmentSpBloodType.setSelection(bloodTypesSelectedId);
                registerAndEditProfileFragmentSpGovernment.setSelection(governmentSelectedId);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        registerAndEditProfileFragmentSpCity.setSelection(citiesSelectedId);
//                    }
//                }, 2000);
            }
        }, 2000);


        registerAndEditProfileFragmentEtName.setText(clientData.getClient().getName());
        registerAndEditProfileFragmentEtEmail.setText(clientData.getClient().getEmail());
        registerAndEditProfileFragmentEtPhoneNumber.setText(clientData.getClient().getPhone());
        registerAndEditProfileFragmentEtPassword.setText(LoadData(getActivity(), USER_PASSWORD));
        registerAndEditProfileFragmentEtConfirmPassword.setText(LoadData(getActivity(), USER_PASSWORD));

        registerAndEditProfileFragmentEtBirthDate.setText(clientData.getClient().getBirthDate());
        registerAndEditProfileFragmentEtLastDonationDate.setText(clientData.getClient().getDonationLastDate());

        registersAndEditProfileFragmentTvTitle.setText(getString(R.string.edit_profile));
        registerAndEditProfileFragmentBtnRegister.setText(getString(R.string.edit));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onBack() {
        if (PROFILE) {
            replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame
                    , homeCycleActivity.homeContainerFragment);
        } else {
            super.onBack();
        }
    }

}
