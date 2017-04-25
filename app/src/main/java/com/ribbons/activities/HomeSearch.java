package com.ribbons.activities;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ribbons.R;
import com.ribbons.adapters.HomeSearchAdapter;
import com.ribbons.helper.DividerItemDecoration;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeSearchModel;
import com.ribbons.modeldatas.Result;
import com.ribbons.modeldatas.SearchDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeSearch extends AppCompatActivity {

    private RecyclerView recyclerViewHomeSearch;
    private ArrayList<HomeSearchModel> homeSearchList = new ArrayList<HomeSearchModel>();
    private SearchView searchView;
    private HomeSearchAdapter homeSearchAdapter;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private static final String TAG = "HomeSearch";
    private String id = "";
    private ImageView ivBack;
    private Spinner spinnerLocation,spinnerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        searchView = (SearchView) findViewById(R.id.searchView);
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                HomeSearch.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        recyclerViewHomeSearch = (RecyclerView) findViewById(R.id.recyclerViewHomeSearch);
        recyclerViewHomeSearch.setHasFixedSize(true);
        recyclerViewHomeSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHomeSearch.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        spinnerOperation();
        final String token = sharedHelper.getKey(this,"access_token");
        final String accept = getString(R.string.accept);
        final String type = getString(R.string.token_type);
        searchView.setQueryHint("Type your keyword here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                id = newText;
                operation(accept,type,token,id);
               // homeSearchAdapter.filter(newText);
                return false;
            }
        });



    }

    private void spinnerOperation() {
        String[] location = new String[]{"Royapettah","Triplicane","Mylapore"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeSearch.this,android.R.layout.simple_list_item_1,location);
        spinnerLocation.setAdapter(adapter);
        String[] categoty = new String[]{"Hotels","Coffee Shop","Restaurants"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(HomeSearch.this,android.R.layout.simple_list_item_1,categoty);
        spinnerCategory.setAdapter(adapter1);
    }

    private void operation(String accept, String type, String token, String id) {

        apiService.getSerachData(accept,type +" " + token,id).enqueue(new Callback<SearchDataModel>() {
            @Override
            public void onResponse(Call<SearchDataModel> call, Response<SearchDataModel> response) {
                Log.e(TAG ,"onResponce Success --> " + response.isSuccessful());
                try {
                    if (response.isSuccessful()){
                        if (response.body().getError().equalsIgnoreCase("false")){
                            Toast.makeText(HomeSearch.this, response.body().getResults().get(0).getBrandname()+"", Toast.LENGTH_SHORT).show();
                            List<Result> results = response.body().getResults();
                            homeSearchAdapter = new HomeSearchAdapter(HomeSearch.this,results);
                            recyclerViewHomeSearch.setAdapter(homeSearchAdapter);
                        }else {

                        }
                        homeSearchAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(HomeSearch.this, "Auth error", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SearchDataModel> call, Throwable t) {
                Log.e(TAG, "onRespoce Fail --> " + t.getLocalizedMessage());

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }
}
