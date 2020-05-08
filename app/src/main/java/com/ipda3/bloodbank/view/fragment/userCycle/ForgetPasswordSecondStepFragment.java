package com.ipda3.bloodbank.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.newPassword.NewPassword;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;

public class ForgetPasswordSecondStepFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.forget_password_second_step_fragment_et_code)
    EditText forgetPasswordSecondStepFragmentEtCode;
    @BindView(R.id.forget_password_second_step_fragment_et_password)
    EditText forgetPasswordSecondStepFragmentEtPassword;
    @BindView(R.id.forget_password_second_step_fragment_et_confirm_password)
    EditText forgetPasswordSecondStepFragmentEtConfirmPassword;

    public String phone;

    public ForgetPasswordSecondStepFragment() {
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
        View view = inflater.inflate(R.layout.fragment_forget_password_second_step, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.forget_password_second_step_fragment_btn_change)
    public void onViewClicked() {
        changePassword();
    }

    private void changePassword() {
        String confirmationCode = forgetPasswordSecondStepFragmentEtCode.getText().toString();
        String newPassword = forgetPasswordSecondStepFragmentEtPassword.getText().toString();
        String confirmNewPassword = forgetPasswordSecondStepFragmentEtConfirmPassword.getText().toString();

        Call<NewPassword> newPasswordCall = getClient().newPassword(newPassword, confirmNewPassword, confirmationCode, phone);
        newPasswordCall.enqueue(new Callback<NewPassword>() {
            @Override
            public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                NewPassword result = response.body();
                Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<NewPassword> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
