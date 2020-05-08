package com.ipda3.bloodbank.view.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.bloodbank.R;
import com.ipda3.bloodbank.adapter.DonationAdapter;
import com.ipda3.bloodbank.adapter.PostAdapter;
import com.ipda3.bloodbank.adapter.SpinnerAdapter;
import com.ipda3.bloodbank.data.model.generalResponse.GeneralResponse;
import com.ipda3.bloodbank.data.model.post.Post;
import com.ipda3.bloodbank.data.model.post.PostData;
import com.ipda3.bloodbank.helper.OnEndLess;
import com.ipda3.bloodbank.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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

public class PostsListFragment extends BaseFragment {

    public HomeContainerFragment homeContainerFragment;
    Unbinder unbinder;
    @BindView(R.id.posts_list_fragment_filter_et_keyword)
    EditText postsListFragmentFilterEtKeyword;
    @BindView(R.id.posts_list_fragment_cat_sp_spinner)
    Spinner postsListFragmentCatSpSpinner;
    @BindView(R.id.posts_list_fragment_rl_filter)
    RelativeLayout postsListFragmentRlFilter;
    @BindView(R.id.posts_list_fragment_rv_posts)
    RecyclerView postsListFragmentRvPosts;


    private PostAdapter postAdapter;
    private SpinnerAdapter categorySpinner;
    private List<PostData> postDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private Integer maxPage = 0;

    public boolean favList = false;
    private boolean filter = false;
    private String keyWord = "";

    public PostsListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_posts_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    public void init() {
        setSpinner();

        linearLayout = new LinearLayoutManager(getActivity());
        postsListFragmentRvPosts.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        if (filter) {
                            onFilter(current_page);
                        } else {
                            getPosts(current_page);
                        }
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        postsListFragmentRvPosts.addOnScrollListener(onEndLess);
        postAdapter = new PostAdapter(getActivity(), postDataList);
        postsListFragmentRvPosts.setAdapter(postAdapter);

        getPosts(1);
    }

    public void setSpinner() {
        categorySpinner = new SpinnerAdapter(getActivity());

        Call<GeneralResponse> categoriesCall = getClient().categories();
        getData(categoriesCall, categorySpinner, getString(R.string.categories), postsListFragmentCatSpSpinner);
    }

    public void getPosts(int page) {
        if (favList) {
            Call<Post> postCall = getClient().favoritePostsList(LoadData(getActivity(), API_TOKEN), page);
            postsListFragmentRlFilter.setVisibility(View.GONE);
            startCall(postCall, page);
        } else {
            Call<Post> postCall = getClient().postsList(LoadData(getActivity(), API_TOKEN), page);
            postsListFragmentRlFilter.setVisibility(View.VISIBLE);
            startCall(postCall, page);
        }

    }

    private void onFilter(int current_page) {
        filter = true;
        keyWord = postsListFragmentFilterEtKeyword.getText().toString().trim();

        Call<Post> postCall = getClient().postFilteredList(
                LoadData(getActivity(), API_TOKEN), current_page, keyWord, categorySpinner.selectedId);
        startCall(postCall, current_page);
    }

    private void startCall(Call<Post> postCall, int page) {
        if(page == 1){
            reInit();
        }
        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
//                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                if (response.body().getStatus() == 1) {
                    try {
                        maxPage = response.body().getData().getLastPage();
                        postDataList.addAll(response.body().getData().getData());
                        postAdapter.notifyDataSetChanged();
                        if(postAdapter.getItemCount() == 0){

                        }

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
//                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        postDataList = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), postDataList);
        postsListFragmentRvPosts.setAdapter(postAdapter);
    }

    @OnClick(R.id.posts_list_fragment_ib_filter)
    public void onViewClicked() {
        if (categorySpinner.selectedId == 0 &&  keyWord == "" && filter) {
            filter = false;
            getPosts(1);
        } else {
            onFilter(1);
        }
    }
}
