package com.ribbons.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.ribbons.R;
import com.ribbons.activities.HomeSearch;
import com.ribbons.activities.SignIn;
import com.ribbons.adapters.HomePageAdapter;
import com.ribbons.adapters.MyPagerAdapter;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeDataModel;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Ribbonobject;
import com.ribbons.modeldatas.Sliderribbon;
import com.ribbons.modeldatas.Success;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;
    private AppBarLayout appbar;

    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private RecyclerView recyclerViewHome;
    private AppCompatTextView tv_search;
    private LinearLayout toolbar_LinearLayout,linear_header,sub_LinearLayout;
    private RelativeLayout toolbar_RelativeLayout;
    private Toolbar header;
    private AHBottomNavigation bottomNavigation;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private static final String TAG = "HomeFragment";
    private ArrayList<Ribbonobject> ribbonobjectArrayList = new ArrayList<Ribbonobject>();
    private  List<Ribbonobject> rib=null;
    private HomePageAdapter homePageAdapter;
    private CircleIndicator indicator;
    private TextView tv_RibbonCounts,tv_UserPoints,tvName;
    private ProgressDialog progressDialog;
    boolean isConnected;
    private AVLoadingIndicatorView avi;



    public HomeFragment() {

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
        View view = inflater.inflate(R.layout.dummyfrag, container, false);
        context=getActivity();
        //
        header = (Toolbar) getActivity().findViewById(R.id.header);
        toolbar_LinearLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_LinearLayout);
        sub_LinearLayout = (LinearLayout) getActivity().findViewById(R.id.sub_LinearLayout);
        linear_header = (LinearLayout)view.findViewById(R.id.linear_header);
        toolbar_RelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.toolbar_RelativeLayout);
      //  bottomNavigation = (AHBottomNavigation) getActivity().findViewById(R.id.bottom_navigation);
        tv_RibbonCounts = (TextView) getActivity().findViewById(R.id.tv_RibbonCounts);
        tv_UserPoints = (TextView) getActivity().findViewById(R.id.tv_UserPoints);
        tvName = (TextView) getActivity().findViewById(R.id.tvName);
        avi= (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        //
        toolbar_LinearLayout.setVisibility(View.VISIBLE);
        toolbar_RelativeLayout.setVisibility(View.GONE);
        sub_LinearLayout.setVisibility(View.GONE);
        header.setNavigationIcon(null);
        header.setTitle(null);

        tv_search = (AppCompatTextView) view.findViewById(R.id.tv_search);
        recyclerViewHome = (RecyclerView) view.findViewById(R.id.recyclerViewHome);
        recyclerViewHome.setHasFixedSize(true);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(context));
        indicator = (CircleIndicator) view.findViewById(R.id.indicator_unselected_background);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setPadding(60,0,60,0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(40);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = viewPager.getMeasuredWidth() -
                        viewPager.getPaddingLeft() - viewPager.getPaddingRight();
                int pageHeight = viewPager.getHeight();
                int paddingLeft = viewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;
                int max = pageHeight / 10;
                if (transformPos< -1) {
                    page.setAlpha(1f);
                    page.setScaleY(0.8f);

                } else if (transformPos <= 1) {
                    page.setScaleY(1f);
                } else {
                    page.setAlpha(1f);
                    page.setScaleY(0.8f);
                }
            }
        });
        operation();

        linear_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, HomeSearch.class));
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        return view;
    }

    private void operation() {
        if (isConnected){
            //progressDialog.show();
            avi.show();
            String  s = sharedHelper.getKey(context,"access_token");
            try {
                apiService.getHomeData("application/json","Bearer"+" "+s,String.valueOf("13.063390"),String.valueOf("80.237391")).enqueue(new Callback<HomeDataModel>() {
                    @Override
                    public void onResponse(Call<HomeDataModel> call, Response<HomeDataModel> response) {
                       // progressDialog.dismiss();
                        avi.hide();
                        Log.e(TAG," onResponce Success " + response.isSuccessful());
                        try {
                            if (response.isSuccessful()){
                                Log.e(TAG, "onResponse: "+response.body().getSuccess());
                                String userPoints = response.body().getSuccess().get(0).getUserpoints().getPoints();
                                String ribboncount = String.valueOf(response.body().getSuccess().get(0).getUserribbons().getRibboncount());
                                sharedHelper.putKey(context,"userPoints",userPoints);
                                sharedHelper.putKey(context,"ribboncount",ribboncount);
                                tv_RibbonCounts.setText(String.valueOf(ribboncount + " " + "Ribbons"));
                                tv_UserPoints.setText(String.valueOf(userPoints + " " + "Points"));
                                tvName.setText(String.valueOf(sharedHelper.getKey(context,"CName")));

                                for (Success obj : response.body().getSuccess()){
                                    rib = obj.getRibbonobjects();
                                    homePageAdapter = new HomePageAdapter(context,rib);
                                    recyclerViewHome.setAdapter(homePageAdapter);
                                }
                                for (Success obj : response.body().getSuccess()){
                                    List<Sliderribbon> sliderribbons = obj.getSliderribbon();
                                    myPagerAdapter = new MyPagerAdapter(context,sliderribbons);
                                    viewPager.setAdapter(myPagerAdapter);
                                    viewPager.setCurrentItem(viewPager.getAdapter().getCount()/2);
                                    indicator.setViewPager(viewPager);
                                }
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Auth fail", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(context,SignIn.class));
                                getActivity().finish();
                            }
                            homePageAdapter.notifyDataSetChanged();
                            myPagerAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<HomeDataModel> call, Throwable t) {
                        Log.e(TAG," onResponce Fail " + t.getLocalizedMessage());
                       // progressDialog.dismiss();
                        avi.hide();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

//---------------------------------------------------------------------------------------------------------------------------------------------------//

 /* modelDatasList.clear();
        modelDatasList.add(new HomeModelData(R.drawable.hairstyle,"200","Naturals Saloon"));
        modelDatasList.add(new HomeModelData(R.drawable.hotels,"450","Hotel Buhari"));
        modelDatasList.add(new HomeModelData(R.drawable.supermarkets,"150","Super Market"));
        modelDatasList.add(new HomeModelData(R.drawable.iceshop,"180","Ice Cream Shop"));
        modelDatasList.add(new HomeModelData(R.drawable.bakery,"100","Bakery"));*/