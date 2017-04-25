package com.ribbons.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeDataModel;
import com.ribbons.modeldatas.HomeDetailDataModel;
import com.ribbons.modeldatas.UnTieRibbonDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RibbonDetailsActivity extends AppCompatActivity {

    private TextView tv_ViewAllOffers, tvTitle, tvPrice, tvAddress1, tvAddress2, tvAddress3, tvDiscountPoint;
    private ImageView iv_header;
    private APIService apiService;
    private SharedHelper sharedHelper;
    private static final String TAG = "RibbonDetailsActivity";
    private View view;
    private String adTitle, adPoints, adImg;
    private String id, token, accept, type;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeRibbonsDetails;
    boolean isConnected;
    private ScrollView scrollRibbonDetails;
    private View viewsImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ribbon_details);
        apiService = ApiUtils.getAPIService();
        sharedHelper = new SharedHelper(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ... ");
        progressDialog.setCancelable(false);
        isConnected = ConnectivityReceiver.isConnected();
        tv_ViewAllOffers = (TextView) findViewById(R.id.tv_ViewAllOffers);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAddress1 = (TextView) findViewById(R.id.tvAddress1);
        tvAddress2 = (TextView) findViewById(R.id.tvAddress2);
        tvAddress3 = (TextView) findViewById(R.id.tvAddress3);
        tvDiscountPoint = (TextView) findViewById(R.id.tvDiscountPoint);
        iv_header = (ImageView) findViewById(R.id.iv_header);
        viewsImage = (View) findViewById(R.id.viewsImage);


        relativeRibbonsDetails = (RelativeLayout) findViewById(R.id.relativeRibbonsDetails);
        scrollRibbonDetails = (ScrollView) findViewById(R.id.scrollRibbonDetails);
        tv_ViewAllOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RibbonDetailsActivity.this, ViewAllActivities.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                RibbonDetailsActivity.this.finish();
            }
        });
        iv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                RibbonDetailsActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

            }
        });
        //
        id = getIntent().getStringExtra("getId");
        token = sharedHelper.getKey(this, "access_token");
        accept = getString(R.string.accept);
        type = getString(R.string.token_type);
        operation(id);
    }

    private void operation(String id) {
        if (isConnected){
            progressDialog.show();
            final String token = sharedHelper.getKey(RibbonDetailsActivity.this, "access_token");
            Log.e(TAG, "Tokens --> " + token);
            apiService.getHomeDetails("application/json", "Bearer" + " " + token, id).enqueue(new Callback<HomeDetailDataModel>() {
                @Override
                public void onResponse(Call<HomeDetailDataModel> call, Response<HomeDetailDataModel> response) {
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponce Success --> " + response.isSuccessful());
                    try {
                        if (response.isSuccessful()) {
                            response.body().getSuccess().get(0).getAddress();
                            tvTitle.setText(response.body().getSuccess().get(0).getBrandname());
                            Glide.with(RibbonDetailsActivity.this).load(response.body().getSuccess().get(0).getBrandbanner()).into(iv_header);
                            viewsImage.setBackgroundResource(R.drawable.transparent);
                            iv_header.setAlpha(0.50f);
                            tvPrice.setText("₹" + " " + String.valueOf(response.body().getSuccess().get(0).getPointsrequired()));
                            tvAddress1.setText(String.valueOf(response.body().getSuccess().get(0).getAddress()));
                            tvDiscountPoint.setText(String.valueOf(response.body().getSuccess().get(0).getRibbondescription()));
                            adTitle = response.body().getSuccess().get(0).getOutletname();
                            adPoints = response.body().getSuccess().get(0).getPointsrequired();
                            adImg = response.body().getSuccess().get(0).getBrandbanner();
                        }else {
                            Toast.makeText(RibbonDetailsActivity.this, "Auth fail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RibbonDetailsActivity.this,SignIn.class));
                            RibbonDetailsActivity.this.finish();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<HomeDetailDataModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponce Fail" + t.getLocalizedMessage());
                }
            });
        }else {
            Toast.makeText(this, getResources().getString(R.string.no_internet)+"", Toast.LENGTH_SHORT).show();
        }


    }

    public void untieRibbon(View view) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.setCancelable(true);
        final View dialogView = inflater.inflate(R.layout.ribbondialogue_sufficient, null);
        alertDialog.setView(dialogView);
        //
        final TextView tv_title, tv_Points, tv_PointsDescription;
        final ImageView iv_header;
        tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        tv_Points = (TextView) dialogView.findViewById(R.id.tv_Points);
        tv_PointsDescription = (TextView) dialogView.findViewById(R.id.tv_PointsDescription);
        iv_header = (ImageView) dialogView.findViewById(R.id.iv_header);
        tv_title.setText(adTitle);
        tv_Points.setText(adPoints);
        tv_PointsDescription.setText("Trade" + " " + adPoints + " points ?");
        Glide.with(RibbonDetailsActivity.this).load(adImg).into(iv_header);
        //
        Button bt_Yes = (Button) dialogView.findViewById(R.id.bt_Yes);
        Button bt_No = (Button) dialogView.findViewById(R.id.bt_No);
        //
        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
        //
        bt_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                untieOperation(dialog);
            }
        });
        bt_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

    }

    private void untieOperation(final AlertDialog dialog) {

        apiService.getUntieRibbon(accept, type + " " + token, id).enqueue(new Callback<UnTieRibbonDataModel>() {
            @Override
            public void onResponse(Call<UnTieRibbonDataModel> call, Response<UnTieRibbonDataModel> response) {
                progressDialog.dismiss();
                Log.e(TAG, " onResponce Untie Success --> " + response.isSuccessful());
                if (response.isSuccessful()) {
                    dialog.cancel();
                    //
                    if (response.body().getError().equalsIgnoreCase("false")){
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RibbonDetailsActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        alertDialog.setCancelable(true);
                        final View dialogView = inflater.inflate(R.layout.untiedialogue, null);
                        alertDialog.setView(dialogView);
                        //
                        final AlertDialog dialogs = alertDialog.create();
                        dialogs.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                        dialogs.show();
                        //
                        final ImageView ivCancel;
                        ivCancel = (ImageView) dialogView.findViewById(R.id.ivCancel);
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogs.dismiss();
                            }
                        });
                    }
                    else if (response.body().getError().equalsIgnoreCase("true")){
                        Toast.makeText(RibbonDetailsActivity.this, response.body().getErrorMsg()+ " ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RibbonDetailsActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnTieRibbonDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, " onResponce Untie Fail --> " + t.getLocalizedMessage());
            }
        });

    }
}

//------------------------------------------------------------------------------------------------------------------------------------------------------//

//
//dialog.cancel();
                /*final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RibbonDetailsActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                alertDialog.setCancelable(true);
                final View dialogView = inflater.inflate(R.layout.untiedialogue, null);
                alertDialog.setView(dialogView);
                final AlertDialog dialog = alertDialog.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                dialog.show();*/