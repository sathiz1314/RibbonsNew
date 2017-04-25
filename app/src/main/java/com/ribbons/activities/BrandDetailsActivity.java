package com.ribbons.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.adapters.MyBrandDetailsAdapter;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.BrandDetailsDataModel;
import com.ribbons.modeldatas.Ribbonslist;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBrandDetails;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private static final String TAG = "BrandDetailsActivity";
    private MyBrandDetailsAdapter myBrandDetailsAdapter;
    private ImageView iv_header,ivBack;
    private TextView tvTitle,tvTitleDesc;
    private RelativeLayout relativeBrandDetails;
    boolean isConnected;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_details);
       //
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        isConnected = ConnectivityReceiver.isConnected();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
       //
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitleDesc = (TextView) findViewById(R.id.tvTitleDesc);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        relativeBrandDetails = (RelativeLayout) findViewById(R.id.relativeBrandDetails);
        recyclerViewBrandDetails = (RecyclerView) findViewById(R.id.recyclerViewBrandDetails);
        recyclerViewBrandDetails.setHasFixedSize(true);
        recyclerViewBrandDetails.setLayoutManager(new LinearLayoutManager(this));
       //
        String token = sharedHelper.getKey(this,"access_token");
        String id = getIntent().getStringExtra("getId");
        String accept = getString(R.string.accept);
        String type = getString(R.string.token_type);
        operation(accept,type,token,id);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                BrandDetailsActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
    }

    private void operation(String accept, String type, String token, String id) {
        if (isConnected){
            apiService.getBrandDetails(accept,type+" "+token,id).enqueue(new Callback<BrandDetailsDataModel>() {
                @Override
                public void onResponse(Call<BrandDetailsDataModel> call, Response<BrandDetailsDataModel> response) {
                    Log.e(TAG,"onResponce success -->" + response.isSuccessful());
                    try {
                        if (response.isSuccessful()){
                            if (response.body().getError().equalsIgnoreCase("false")){
                                Glide.with(BrandDetailsActivity.this).load(response.body().getBranddetails().get(0).getBrandbanner()).into(iv_header);
                                tvTitle.setText(response.body().getBranddetails().get(0).getBrandname());
                                tvTitleDesc.setText(response.body().getBranddetails().get(0).getBrandname());

                                List<Ribbonslist> ribbonslists = response.body().getRibbonslist();
                                if (ribbonslists.size()<0){
                                    relativeBrandDetails.setVisibility(View.VISIBLE);
                                }else {
                                    myBrandDetailsAdapter = new MyBrandDetailsAdapter(BrandDetailsActivity.this,ribbonslists);
                                    recyclerViewBrandDetails.setAdapter(myBrandDetailsAdapter);
                                }
                            }else {
                                Toast.makeText(BrandDetailsActivity.this, "Auth fail", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(BrandDetailsActivity.this,SignIn.class));
                                BrandDetailsActivity.this.finish();
                            }
                        }
                        myBrandDetailsAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(Call<BrandDetailsDataModel> call, Throwable t) {
                    Log.e(TAG," onResponce Fail -->" + t.getLocalizedMessage());
                }
            });

        }else {
            Toast.makeText(this, getResources().getString(R.string.no_internet)+"", Toast.LENGTH_SHORT).show();
        }


    }
}
