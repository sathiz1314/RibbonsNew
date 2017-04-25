package com.ribbons.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ribbons.R;
import com.ribbons.adapters.MyRibbonsAdapter;
import com.ribbons.adapters.RibbonAdapter;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Ribbon;
import com.ribbons.modeldatas.RibbonsDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllActivities extends AppCompatActivity {

    private RecyclerView recyclerViewAllOffers;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private CollapsingToolbarLayout collapsing_toolbar;
    private ImageView iv_backIcon;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private RibbonAdapter ribbonAdapter;
    private static final String TAG = "ViewAllActivities";
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_activities);
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle("All Offers");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorGlobal));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  onBackPressed();
                Intent logIntent = new Intent(ViewAllActivities.this, MainActivity.class);
                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                logIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logIntent);
                ViewAllActivities.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        /*iv_backIcon  = (ImageView) findViewById(R.id.iv_backIcon);
        iv_backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                ViewAllActivities.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });*/

        String token = sharedHelper.getKey(this, "access_token");
        String accept = getString(R.string.accept);
        String type = getString(R.string.token_type);
        operation(accept, type, token);

        recyclerViewAllOffers = (RecyclerView) findViewById(R.id.recyclerViewAllOffers);
        recyclerViewAllOffers.setHasFixedSize(true);
        recyclerViewAllOffers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void operation(String accept, String type, String token) {
        progressDialog.show();
        apiService.getRibbonData(accept, type + " " + token, String.valueOf("13.063390"), String.valueOf("80.237391")).enqueue(new Callback<RibbonsDataModel>() {
            @Override
            public void onResponse(Call<RibbonsDataModel> call, Response<RibbonsDataModel> response) {
                progressDialog.dismiss();
                Log.e(TAG, " onResponce success --> " + response.isSuccessful());

                try {
                    if (response.isSuccessful()) {
                        if (response.body().getError().equalsIgnoreCase("false")) {
                            List<Ribbon> ribbonList = response.body().getRibbons();
                            ribbonAdapter = new RibbonAdapter(ViewAllActivities.this, ribbonList);
                            recyclerViewAllOffers.setAdapter(ribbonAdapter);
                        }
                    }
                    ribbonAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RibbonsDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onResponce Fail -->" + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent logIntent = new Intent(ViewAllActivities.this, MainActivity.class);
        logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        logIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logIntent);
        ViewAllActivities.this.finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
