package com.ipda3.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.data.model.client.ClientData;
import com.ipda3.bloodbank.data.model.post.PostData;
import com.ipda3.bloodbank.data.model.postToggleFavorite.PostToggleFavorite;
import com.ipda3.bloodbank.view.activity.BaseActivity;
import com.ipda3.bloodbank.view.fragment.homeCycle.PostDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.bloodbank.data.api.RetrofitClient.getClient;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.bloodbank.helper.HelperMethod.replaceFragment;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<PostData> postDataList = new ArrayList<>();
    private ClientData clientData;
    private String lang;
    private boolean isFav = false;

    public PostAdapter(Activity context, List<PostData> postDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.postDataList = postDataList;

        clientData = loadUserData(activity);
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        Glide.with(context).load(postDataList.get(position).getThumbnailFullPath()).into(holder.postItemIvImage);
        holder.postItemTvTitle.setText(postDataList.get(position).getTitle());
        isFav = postDataList.get(position).getIsFavourite();
        if (isFav) {
            holder.postItemIbFavorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_filled_heart));
        } else {
            holder.postItemIbFavorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_empt_heart));
        }

        holder.postItemIbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFav = !isFav;
                toggleFavorite(postDataList.get(position).getId());
                if (isFav) {
                    holder.postItemIbFavorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_filled_heart));
                    postDataList.get(position).setIsFavourite(true);
                } else {
                    holder.postItemIbFavorite.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_empt_heart));
                    postDataList.get(position).setIsFavourite(false);
                }
            }
        });

        holder.postItemRlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
                postDetailsFragment.postData = postDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, postDetailsFragment);
            }
        });
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return postDataList.size();
    }

    public void toggleFavorite(int id){
        Call<PostToggleFavorite> postToggleFavoriteCall = getClient().postToggleFavourite(id, LoadData(activity, API_TOKEN));
        postToggleFavoriteCall.enqueue(new Callback<PostToggleFavorite>() {
            @Override
            public void onResponse(Call<PostToggleFavorite> call, Response<PostToggleFavorite> response) {

            }

            @Override
            public void onFailure(Call<PostToggleFavorite> call, Throwable t) {

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.post_item_iv_image)
        ImageView postItemIvImage;
        @BindView(R.id.post_item_tv_title)
        TextView postItemTvTitle;
        @BindView(R.id.post_item_ib_favorite)
        ImageButton postItemIbFavorite;
        @BindView(R.id.post_item_rl_layout)
        RelativeLayout postItemRlLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
