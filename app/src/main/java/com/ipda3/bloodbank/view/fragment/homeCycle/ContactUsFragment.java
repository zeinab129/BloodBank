package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.appSettings.AppSettings;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.contactUs.ContactUs;
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
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class ContactUsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.contact_us_fragment_tv_phone)
    TextView contactUsFragmentTvPhone;
    @BindView(R.id.contact_us_fragment_tv_email)
    TextView contactUsFragmentTvEmail;
    @BindView(R.id.contact_us_fragment_et_msg_title)
    EditText contactUsFragmentEtMsgTitle;
    @BindView(R.id.contact_us_fragment_et_msg_content)
    EditText contactUsFragmentEtMsgContent;

    public AppSettings appSettings;

    private ClientData clientData;

    public ContactUsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        unbinder = ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        contactUsFragmentTvPhone.setText(clientData.getClient().getPhone());
        contactUsFragmentTvEmail.setText(clientData.getClient().getEmail());

        return view;
    }

    @OnClick({R.id.contact_us_fragment_iv_facebook, R.id.contact_us_fragment_iv_intsta, R.id.contact_us_fragment_iv_twitter, R.id.contact_us_fragment_iv_youtube, R.id.contact_us_fragment_btn_msg_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_us_fragment_iv_facebook:
                Intent facebookIntent = new Intent();
                facebookIntent.setAction(Intent.ACTION_VIEW);
                facebookIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                facebookIntent.setData(Uri.parse(appSettings.getData().getFacebookUrl()));
                startActivity(facebookIntent);
                break;
            case R.id.contact_us_fragment_iv_intsta:
                Intent instaIntent = new Intent();
                instaIntent.setAction(Intent.ACTION_VIEW);
                instaIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                instaIntent.setData(Uri.parse(appSettings.getData().getInstagramUrl()));
                startActivity(instaIntent);
                break;
            case R.id.contact_us_fragment_iv_twitter:
                Intent twitterIntent = new Intent();
                twitterIntent.setAction(Intent.ACTION_VIEW);
                twitterIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                twitterIntent.setData(Uri.parse(appSettings.getData().getTwitterUrl()));
                startActivity(twitterIntent);
                break;
            case R.id.contact_us_fragment_iv_youtube:
                Intent youtubeIntent = new Intent();
                youtubeIntent.setAction(Intent.ACTION_VIEW);
                youtubeIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                youtubeIntent.setData(Uri.parse(appSettings.getData().getYoutubeUrl()));
                startActivity(youtubeIntent);
                break;
            case R.id.contact_us_fragment_btn_msg_send:
                String msgTitle, msgContent;
                msgTitle = contactUsFragmentEtMsgTitle.getText().toString();
                msgContent = contactUsFragmentEtMsgContent.getText().toString();
                Call<ContactUs> contactUsCall = getClient().contact(LoadData(getActivity(), API_TOKEN), msgTitle, msgContent);
                send(contactUsCall);
                break;
        }
    }

    public void send(Call<ContactUs> call){
        call.enqueue(new Callback<ContactUs>() {
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ContactUs> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
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
