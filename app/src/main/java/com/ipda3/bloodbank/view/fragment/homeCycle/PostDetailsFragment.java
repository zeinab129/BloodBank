package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.post.PostData;
import com.ipda3.bloodbank.data.model.postToggleFavorite.PostToggleFavorite;
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

public class PostDetailsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.post_details_fragment_im_image)
    ImageView postDetailsFragmentImImage;
    @BindView(R.id.post_details_fragment_ib_favorite)
    ImageButton postDetailsFragmentIbFavorite;
    @BindView(R.id.post_details_fragment_tv_title)
    TextView postDetailsFragmentTvTitle;
    @BindView(R.id.post_details_fragment_tv_content)
    TextView postDetailsFragmentTvContent;

    public PostData postData;
    private boolean isFav = false;

    public PostDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        Glide.with(getActivity()).load(postData.getThumbnailFullPath()).into(postDetailsFragmentImImage);
        postDetailsFragmentTvTitle.setText(postData.getTitle());
        postDetailsFragmentTvContent.setText(postData.getContent());
        isFav = postData.getIsFavourite();
        if (isFav) {
            postDetailsFragmentIbFavorite.setImageResource(R.drawable.ic_filled_heart);
        } else {
            postDetailsFragmentIbFavorite.setImageResource(R.drawable.ic_empt_heart);
        }


        return view;
    }

    @OnClick(R.id.post_details_fragment_ib_favorite)
    public void onViewClicked() {
        isFav = !isFav;
        Call<PostToggleFavorite> postToggleFavoriteCall = getClient().postToggleFavourite(postData.getId(), LoadData(getActivity(), API_TOKEN));
        postToggleFavoriteCall.enqueue(new Callback<PostToggleFavorite>() {
            @Override
            public void onResponse(Call<PostToggleFavorite> call, Response<PostToggleFavorite> response) {
                if(response.body().getStatus() == 1){
                    if(isFav){
                        postDetailsFragmentIbFavorite.setImageResource(R.drawable.ic_filled_heart);
                        postData.setIsFavourite(true);
                    }
                    else{
                        postDetailsFragmentIbFavorite.setImageResource(R.drawable.ic_empt_heart);
                        postData.setIsFavourite(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostToggleFavorite> call, Throwable t) {

            }
        });
    }
}
