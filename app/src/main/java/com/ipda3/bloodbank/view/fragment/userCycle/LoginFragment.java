package com.ipda3.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.client.Client;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.ipda3.bloodbank.helper.GeneralRequest.userData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class LoginFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.login_fragment_et_phone)
    EditText loginFragmentEtPhone;
    @BindView(R.id.login_fragment_et_password)
    EditText loginFragmentEtPassword;
    @BindView(R.id.login_fragment_cb_remember_me)
    CheckBox loginFragmentCbRememberMe;


    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }




    @OnClick({R.id.login_fragment_tv_forget_password, R.id.login_fragment_btn_login, R.id.login_fragment_tv_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_tv_forget_password:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_cycle_activity, new ForgetPasswordFirstStepFragment());
                break;
            case R.id.login_fragment_btn_login:
                login();
                break;
            case R.id.login_fragment_tv_sign_up:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_cycle_activity, new RegisterAndEditProfileFragment());
                break;
        }
    }

    private void login() {
        String phone = loginFragmentEtPhone.getText().toString();
        String password = loginFragmentEtPassword.getText().toString();
        boolean remember = loginFragmentCbRememberMe.isChecked();

        Call<Client> loginCall = getClient().onLogin(phone, password);

        userData(getActivity(), loginCall, password, remember, true);

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
