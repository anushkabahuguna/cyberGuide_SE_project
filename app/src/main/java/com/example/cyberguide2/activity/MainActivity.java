package com.example.cyberguide2.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cyberguide2.R;
import com.example.cyberguide2.adapter.MainArticleAdapter;
import com.example.cyberguide2.model.Article;
import com.example.cyberguide2.model.ResponseModel;
import com.example.cyberguide2.rests.APIInterface;
import com.example.cyberguide2.rests.ApiClient;
import com.example.cyberguide2.utils.ClickListener;
import com.example.cyberguide2.utils.OnRecyclerViewItemClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//Retrofit to call the API
//RecyclerView to show the Response list.
//Card View to show each row in card.

public class MainActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private String API_KEY;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Integer count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getString(R.string.NEWS_API_KEY);
       RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
       //  LinearLayoutManager , vertical scrolling
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // pages 1 -- 5
         count =1;
        mainRecycler.setLayoutManager(linearLayoutManager);
//        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);
//        Call<ResponseModel> call = apiService.getLatestNews(API_KEY, "cyberbullying", "en");
//
//        call.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                if (response.body().getStatus().equals("ok")) {
//                    List<Article> articleList = response.body().getArticles();
//
//                    if (articleList.size() > 0) {
//                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(getApplicationContext(), articleList);
//                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);
//                        mainRecycler.setAdapter(mainArticleAdapter);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Log.e("out", t.toString());
//            }
//        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple_200,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
//        Java runnable is an interface used to execute code on a concurrent thread
        mSwipeRefreshLayout.post(() -> {

            mSwipeRefreshLayout.setRefreshing(true);

            // Fetching data from server
            loadRecyclerViewData();
        });


    }
    @Override
    public void onRefresh() {

        // Fetching data from server
        loadRecyclerViewData();
    }
    private void loadRecyclerViewData()
    {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        final APIInterface apiService = ApiClient.getClient().create(APIInterface.class);
        //send the request
        Call<ResponseModel> call = apiService.getLatestNews(API_KEY, "CYBERBULLYING NOT JAPANESE", "en", String.valueOf(count),"publishedAt");
        count++;
        if(count==6)
        {
            count=1;
        }
        RecyclerView mainRecycler = findViewById(R.id.activity_main_rv);
        //asynchronously send the request and notify callback of its response
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            //successful request
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatus().equals("ok")) {

                    List<Article> articleList = response.body().getArticles();

                    if (articleList.size() > 0) {
//                        making a View for each item in the data set.
                        final MainArticleAdapter mainArticleAdapter = new MainArticleAdapter(getApplicationContext(), articleList,new ClickListener() {
                            @Override public void onPositionClicked(int position) {
                                // callback performed on click
                            }

                            @Override
                            public void onLongClicked(int position) {

                            }


                        });
                        mainArticleAdapter.setOnRecyclerViewItemClickListener(MainActivity.this);
                        mainRecycler.setAdapter(mainArticleAdapter);

                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
            //failed request
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("out", t.toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(int position, View view) {
        if (view.getId() == R.id.article_adapter_ll_parent) {
            Article article = (Article) view.getTag();
            if (!TextUtils.isEmpty(article.getUrl())) {
                Toast.makeText(getApplicationContext(), "Opening link!",
                        Toast.LENGTH_LONG).show();
                Log.e("clicked url", article.getUrl());
                Intent webActivity = new Intent(this, WebActivity.class);
                webActivity.putExtra("url", article.getUrl());
                startActivity(webActivity);
            }


        }
    }

}