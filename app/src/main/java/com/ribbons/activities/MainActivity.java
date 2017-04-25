package com.ribbons.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ribbons.R;
import com.ribbons.fragments.BrandsFragment;
import com.ribbons.fragments.HomeFragment;
import com.ribbons.fragments.MyRibbonFragment;
import com.ribbons.fragments.NotificationFrgment;
import com.ribbons.fragments.RibbonFragment;
import com.ribbons.helper.SharedHelper;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.ribbons.retromodels.UserProfileModelData;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static GoogleApiClient mGoogleApiClient;
    //   private ImageView imageViewMenu;
    private Fragment fragment = null;
    private Class fragmentClass = null;
    private FragmentManager fragmentManager;
    private static final long RIPPLE_DURATION = 350;
    private CoordinatorLayout root;
    private View imageViewMenu;
    private View guillotineMenu;
    // private LinearLayout header;
    private Toolbar header;
    private AHBottomNavigation bottomNavigation;
    private LinearLayout toolbar_LinearLayout;
    private RelativeLayout toolbar_RelativeLayout;
    private SharedHelper sharedHelper;
    private AppBarLayout appbar;
    private TextView tv_RibbonCounts, tv_UserPoints, tvRibbonPin;
    private APIService apiService;
    private static final String TAG = "MainActivity";
    Boolean doubleBackToExitPressedOnce = false;
    private static long back_pressed_time;
    private static long PERIOD = 2000;
    private boolean isVisible = false;
    private LinearLayout bottomHome, bottomMyRibbons, bottomRibbons, bottomNotifications, bottomBrands, bottomLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);
        sharedHelper = new SharedHelper(this);
        if (sharedHelper.isFirstTimeLaunch()) {
            dialogueOperation();
        }
        //
        apiService = ApiUtils.getAPIService();
        //
        header = (Toolbar) findViewById(R.id.header);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        root = (CoordinatorLayout) findViewById(R.id.root);
        toolbar_LinearLayout = (LinearLayout) findViewById(R.id.toolbar_LinearLayout);
        toolbar_RelativeLayout = (RelativeLayout) findViewById(R.id.toolbar_RelativeLayout);
        imageViewMenu = findViewById(R.id.imageViewMenu);
      //  bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        tv_RibbonCounts = (TextView) findViewById(R.id.tv_RibbonCounts);
        tv_UserPoints = (TextView) findViewById(R.id.tv_UserPoints);
        tvRibbonPin = (TextView) findViewById(R.id.tvRibbonPin);
        bottomHome = (LinearLayout) findViewById(R.id.bottomHome);
        bottomMyRibbons = (LinearLayout) findViewById(R.id.bottomMyRibbons);
        bottomRibbons = (LinearLayout) findViewById(R.id.bottomRibbons);
        bottomNotifications = (LinearLayout) findViewById(R.id.bottomNotifications);
        bottomBrands = (LinearLayout) findViewById(R.id.bottomBrands);
        bottomLinearLayout = (LinearLayout) findViewById(R.id.bottomLinearLayout);
        //
        toolbar_LinearLayout.setVisibility(View.VISIBLE);
        toolbar_RelativeLayout.setVisibility(View.GONE);
        //
        String accept = getString(R.string.accept);
        String type = getString(R.string.token_type);
        String token = sharedHelper.getKey(MainActivity.this, "access_token");
        userProfileOperation(accept, type, token);
        //
      //  bottomNavigationViewOperation();
        menuOperation();
        //
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //
        try {
            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorGlobal));

            fragment = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_main, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomHome.setOnClickListener(this);
        bottomMyRibbons.setOnClickListener(this);
        bottomRibbons.setOnClickListener(this);
        bottomNotifications.setOnClickListener(this);
        bottomBrands.setOnClickListener(this);
    } // on create

    private void userProfileOperation(String accept, String type, String token) {

        apiService.getUserProfile(accept, type + " " + token, "12345").enqueue(new Callback<UserProfileModelData>() {
            @Override
            public void onResponse(Call<UserProfileModelData> call, Response<UserProfileModelData> response) {
                Log.e(TAG, "onResponce userProfile Success " + response.isSuccessful());
                //
                try {
                    String fName = response.body().getUserdata().get(0).getFirstname();
                    String lName = response.body().getUserdata().get(0).getLastname();
                    String dob = response.body().getUserdata().get(0).getDob();
                    String gender = response.body().getUserdata().get(0).getGender();
                    String phone = response.body().getUserdata().get(0).getPhonenumber();
                    String email = response.body().getUserdata().get(0).getEmail();
                    String location = response.body().getUserdata().get(0).getLocation();
                    String ribbonPin = response.body().getUserdata().get(0).getRibbonpin();
                    tvRibbonPin.setText(String.valueOf(ribbonPin));
                    sharedHelper.putKey(MainActivity.this, "getRibbonpin", ribbonPin);
                    //
                    sharedHelper.putKey(MainActivity.this, "getFirstname", fName);
                    sharedHelper.putKey(MainActivity.this, "getLastname", lName);
                    sharedHelper.putKey(MainActivity.this, "getDob", dob);
                    sharedHelper.putKey(MainActivity.this, "getGender", gender);
                    sharedHelper.putKey(MainActivity.this, "getPhonenumber", phone);
                    sharedHelper.putKey(MainActivity.this, "getEmail", email);
                    sharedHelper.putKey(MainActivity.this, "getLocation", location);
                    sharedHelper.putKey(MainActivity.this, "CName", fName + " " + lName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserProfileModelData> call, Throwable t) {
                Log.e(TAG, "onResponce userProfile Fail " + t.getLocalizedMessage());
            }
        });

    }

    private void dialogueOperation() {
        sharedHelper.setFirstTimeLaunch(false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.setCancelable(false);
        final AlertDialog a = alertDialog.create();
        a.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        View dialogView = inflater.inflate(R.layout.ribbon_home_dialogue, null);
        a.setView(dialogView);
        a.show();
        final TextView tvRbPin = (TextView) findViewById(R.id.tvRbPin);
        final ImageView iv_ribbonDialogueClose = (ImageView) dialogView.findViewById(R.id.iv_ribbonDialogueClose);
        iv_ribbonDialogueClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.cancel();
            }
        });
    }

    //Menu
    private void menuOperation() {
        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), imageViewMenu)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(appbar)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                   //     bottomNavigation.setVisibility(View.GONE);
                        //if (isVisible==true){
                        appbar.setVisibility(View.VISIBLE);
                        // }else {
                        appbar.setVisibility(View.GONE);
                        //}

                        Log.e(TAG, "open");

                    }

                    @Override
                    public void onGuillotineClosed() {
                 //       bottomNavigation.setVisibility(View.VISIBLE);
                        appbar.setVisibility(View.VISIBLE);
                        Log.e(TAG, "close");
                    }
                })
                .build();

    }



    @Override
    public void onBackPressed() {
        // root.removeView(guillotineMenu);
        // appbar.setVisibility(View.VISIBLE);
//        isVisible = true;
        if (back_pressed_time + PERIOD > System.currentTimeMillis()) {
            super.onBackPressed();
        } else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.nav_home) {

        } */
        if (id == R.id.nav_send_feedback) {
            startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_last_list) {
            startActivity(new Intent(MainActivity.this, LastVisitActivity.class));
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_send_voucher) {
            startActivity(new Intent(MainActivity.this, SendVoucherDetails.class));
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_refer_app) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Refer code Rx1423.</p>"));
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } else if (id == R.id.nav_terms_conditions) {

        } else if (id == R.id.nav_logout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            String login = sharedHelper.getKey(MainActivity.this, "login_type");
                            if (login.equalsIgnoreCase("Logged")) {
                                Intent logIntent = new Intent(MainActivity.this, SignIn.class);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                sharedHelper.putKey(MainActivity.this, "login_type", "");
                                startActivity(logIntent);
                                MainActivity.this.finish();
                                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                                Log.e(TAG, "Status -->" + status.toString());
                                Toast.makeText(MainActivity.this, status + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.bottomHome) {
            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            try {
                fragment = new HomeFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.bottomMyRibbons) {

            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorGlobal));

            try {
                fragment = new MyRibbonFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.bottomRibbons) {

            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorGlobal));

            try {
                fragment = new RibbonFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.bottomNotifications) {
            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            try {
                fragment = new NotificationFrgment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.bottomBrands) {
            bottomMyRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomHome.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomRibbons.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomNotifications.setBackgroundColor(getResources().getColor(R.color.colorGlobal));
            bottomBrands.setBackgroundColor(getResources().getColor(R.color.colorBottomDull));
            try {
                fragment = new BrandsFragment();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_main, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}

//-------------------------------------------------------------------------------------------------------------------------------------------------//

 /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

   DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


         final Dialog dialogView = new Dialog(this);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogView.setContentView(R.layout.ribbon_home_dialogue);
        dialogView.setCancelable(false);
        dialogView.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogView.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogView.getWindow().setAttributes(lp);


    // BottomNavigationView ---------------------------------------------------------------------------------------------------------------------//
    @SuppressLint("NewApi")
    private void bottomNavigationViewOperation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.home, R.color.home_menuColor);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_myribbon, R.drawable.ribbon_ic, R.color.home_menuColor);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_ribbon, R.drawable.ribbons, R.color.home_menuColor);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_notifications, R.drawable.notification, R.color.home_menuColor);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_brands, R.drawable.brands, R.color.home_menuColor);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
        bottomNavigation.setColored(true);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        //  bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#000000"));
        bottomNavigation.setAccentColor(Color.parseColor("#FFC107"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    try {
                        fragment = new HomeFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 1) {
                    try {
                        fragment = new MyRibbonFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 2) {
                    try {
                        fragment = new RibbonFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 3) {
                    try {
                        fragment = new NotificationFrgment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 4) {
                    try {
                        fragment = new BrandsFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content_main, fragment).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }*/