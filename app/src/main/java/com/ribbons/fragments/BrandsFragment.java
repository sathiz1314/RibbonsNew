package com.ribbons.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ribbons.R;
import com.ribbons.activities.FeedBackActivity;
import com.ribbons.activities.MainActivity;
import com.ribbons.activities.SendVoucherDetails;
import com.ribbons.activities.SettingsActivity;
import com.ribbons.activities.SignIn;
import com.ribbons.adapters.MyBrandAdapter;
import com.ribbons.adapters.MyRibbonsAdapter;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.Brand;
import com.ribbons.modeldatas.BrandDataModel;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandsFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private Context context;
    private RecyclerView recyclerViewBrands;
    private Toolbar header;
    private LinearLayout toolbar_LinearLayout, sub_LinearLayout;
    private RelativeLayout toolbar_RelativeLayout;
    private View imageViewMenus;
    private View guillotineMenu;
    private CoordinatorLayout root;
    private AHBottomNavigation bottomNavigation;
    private AppBarLayout appbar;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private static final long RIPPLE_DURATION = 350;
    private static final String TAG = "BrandsFragment";
    private MyBrandAdapter myBrandAdapter;
    private ProgressDialog progressDialog;
    private TextView tvRibboncount, tvPoints, tvNamesub,tvRibbonPins;
    boolean isConnected;


    public BrandsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sharedHelper = new SharedHelper(context);
        apiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        isConnected = ConnectivityReceiver.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_brands, container, false);
        appbar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        header = (Toolbar) getActivity().findViewById(R.id.header);
        toolbar_LinearLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_LinearLayout);
        imageViewMenus = getActivity().findViewById(R.id.imageViewMenus);
        root = (CoordinatorLayout) getActivity().findViewById(R.id.root);
        sub_LinearLayout = (LinearLayout) getActivity().findViewById(R.id.sub_LinearLayout);
     //   bottomNavigation = (AHBottomNavigation) getActivity().findViewById(R.id.bottom_navigation);
        tvRibboncount = (TextView) getActivity().findViewById(R.id.tvRibboncount);
        tvPoints = (TextView) getActivity().findViewById(R.id.tvPoints);
        tvNamesub = (TextView) getActivity().findViewById(R.id.tvNamesub);
        tvRibbonPins = (TextView) getActivity().findViewById(R.id.tvRibbonPins);
        toolbar_LinearLayout.setVisibility(View.GONE);
        sub_LinearLayout.setVisibility(View.VISIBLE);
        //
        tvRibboncount.setText(sharedHelper.getKey(context, "ribboncount") + " " + "Ribbons");
        tvPoints.setText(sharedHelper.getKey(context, "userPoints") + " " + "Points");
        tvNamesub.setText(String.valueOf(sharedHelper.getKey(context, "CName")));
        tvRibbonPins.setText(String.valueOf(sharedHelper.getKey(context,"getRibbonpin")));

        //
        String token = sharedHelper.getKey(context, "access_token");
        String accept = getString(R.string.accept);
        String type = getString(R.string.token_type);
        //
        recyclerViewBrands = (RecyclerView) view.findViewById(R.id.recyclerViewBrands);
        recyclerViewBrands.setHasFixedSize(true);
        recyclerViewBrands.setLayoutManager(new LinearLayoutManager(context));
        menuOperation();
        operation(accept, type, token);
        return view;
    }

    private void operation(String accept, String type, String token) {

        if (isConnected) {
            progressDialog.show();
            apiService.getBrand(accept, type + " " + token).enqueue(new Callback<BrandDataModel>() {
                @Override
                public void onResponse(Call<BrandDataModel> call, Response<BrandDataModel> response) {
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponce Success --> " + response.isSuccessful());

                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getError().equalsIgnoreCase("false")) {
                                List<Brand> brands = response.body().getBrands();
                                myBrandAdapter = new MyBrandAdapter(context, brands);
                                recyclerViewBrands.setAdapter(myBrandAdapter);
                            } else {
                                Toast.makeText(context, "Something went wrong in server side", Toast.LENGTH_SHORT).show();
                            }
                            myBrandAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Auth fail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context,SignIn.class));
                            getActivity().finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<BrandDataModel> call, Throwable t) {
                    Log.e(TAG, "onResponce Fail" + t.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            });

        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    //Menu
    private void menuOperation() {
        guillotineMenu = LayoutInflater.from(context).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), imageViewMenus)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(header)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                    //    bottomNavigation.setVisibility(View.GONE);
                        appbar.setVisibility(View.GONE);
                    }


                    @Override
                    public void onGuillotineClosed() {
                    //    bottomNavigation.setVisibility(View.VISIBLE);
                        appbar.setVisibility(View.VISIBLE);
                    }
                })
                .build();
        //
        NavigationView navigationView = (NavigationView) guillotineMenu.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.nav_home) {

        }*/  if (id == R.id.nav_send_feedback) {
            startActivity(new Intent(context, FeedBackActivity.class));
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else if (id == R.id.nav_last_list) {

        } else if (id == R.id.nav_send_voucher) {
            startActivity(new Intent(context, SendVoucherDetails.class));
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(context, SettingsActivity.class));
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }else if (id==R.id.nav_refer_app){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Refer code Rx1423.</p>"));
            startActivity(Intent.createChooser(sharingIntent,"Share using"));
        } else if (id == R.id.nav_terms_conditions) {

        } else if (id == R.id.nav_logout) {
            Auth.GoogleSignInApi.signOut(MainActivity.mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            String login = sharedHelper.getKey(context, "login_type");
                            if (login.equalsIgnoreCase("Logged")) {
                                Intent logIntent = new Intent(context, SignIn.class);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                sharedHelper.putKey(context, "login_type", "");
                                startActivity(logIntent);
                                Activity activity = (Activity) context;
                                activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }
                        }
                    });
        }
        return true;
    }
}


