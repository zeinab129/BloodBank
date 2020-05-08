package com.ipda3.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.resetPassword.ResetPassword;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class ForgetPasswordFirstStepFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.forget_password_first_step_fragment_et_phone)
    EditText forgetPasswordFirstStepFragmentEtPhone;


    public ForgetPasswordFirstStepFragment() {
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
        View view = inflater.inflate(R.layout.fragment_forget_password_first_step, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.forget_password_first_step_fragment_btn_login)
    public void onViewClicked() {
        sendCode();
    }

    private void sendCode() {
        String phone = forgetPasswordFirstStepFragmentEtPhone.getText().toString();
        if(phone.isEmpty()){
            Toast.makeText(getActivity(), R.string.please_enter_phone_number, Toast.LENGTH_LONG).show();
        }
        else{
            Call<ResetPassword> resetPasswordCall = getClient().onForgetPassword(phone);
            resetPasswordCall.enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    ForgetPasswordSecondStepFragment forgetPasswordSecondStepFragment = new ForgetPasswordSecondStepFragment();
                    forgetPasswordSecondStepFragment.phone = phone;
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_cycle_activity, forgetPasswordSecondStepFragment);
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
