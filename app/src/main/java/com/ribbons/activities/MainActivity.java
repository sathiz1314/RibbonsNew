package com.ribbons.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private BottomNavigationView bottomNavigationView;
    //   private ImageView imageViewMenu;
    private Fragment fragment = null;
    private Class fragmentClass = null;
    private FragmentManager fragmentManager;
    private static final long RIPPLE_DURATION = 250;
    private RelativeLayout root;
    private View imageViewMenu;
   // private LinearLayout header;
    private Toolbar header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_main);
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        header = (Toolbar) findViewById(R.id.header);
        root = (RelativeLayout) findViewById(R.id.root);
    //    bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        imageViewMenu = findViewById(R.id.imageViewMenu);


      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        try {
            fragment = new HomeFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(R.id.content_main, fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }

        bottomNavigationViewOperation();
        menuOperation();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //Menu
    private void menuOperation() {

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), imageViewMenu)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(header)
                .setClosedOnStart(true)
                .build();
    }

    // BottomNavigationView
    private void bottomNavigationViewOperation() {
       /* bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_home:
                        try {
                            fragment = new HomeFragment();
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.replace(R.id.content_main, fragment).commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case R.id.action_myribbons:
                        try {
                            fragment = new MyRibbonFragment();
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.content_main, fragment).commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        break;
                    case R.id.action_ribbons:
                        try {
                            fragment = new RibbonFragment();
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.content_main, fragment).commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case R.id.action_notification:
                        try {
                            fragment = new NotificationFrgment();
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.content_main, fragment).commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case R.id.action_brands:
                        try {
                            fragment = new BrandsFragment();
                            fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.content_main, fragment).commit();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }
        });*/
    }

    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        super.onBackPressed();
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

        if (id == R.id.nav_home) {
        } else if (id == R.id.nav_send_feedback) {

        } else if (id == R.id.nav_last_list) {

        } else if (id == R.id.nav_send_voucher) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_terms_conditions) {


            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            onBackPressed();
                            MainActivity.this.finish();
                            Toast.makeText(MainActivity.this, status+"", Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    });


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
