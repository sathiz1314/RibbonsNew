package com.ribbons.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ribbons.R;
import com.ribbons.gps.TrackGPS;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.LoginDetails;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.LoginDataModel;
import com.ribbons.modeldatas.SocialDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleApiClient.ConnectionCallbacks {


    private TextView tv_create_Acc,tv_forgotpassword;
    private AppCompatEditText ed_UserName, ed_Password;
    private String mUsername, mPassword, mFullName, mEmail;
    private SignInButton sign_in_button;
    private static final String TAG = SignIn.class.getSimpleName();
    private int RC_SIGN_IN = 0;
    public static GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton, fb_CustomButton, button_Google;
    private LoginButton fb_Login;
    LoginManager mLoginManager;
    CallbackManager callbackManager;
    AccessTokenTracker mAccessTokenTracker;
    Profile profile;
    private LoginDetails loginDetails;
    private APIService apiService;
    private SharedHelper sharedHelper;
    private ProgressDialog progressDialog;
    private boolean isConnected;
    // GPSTracker gpsTracker;
    TrackGPS gpsTracker;
    private String latitude = "", longitude = "";
    static final Integer LOCATION = 0x1;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    GoogleApiClient client;
    static final Integer GPS_SETTINGS = 0x7;
    Location mLocation;
    private LocationManager mLocationManager;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    private LocationManager locationManager;
    public static double lats,lngs;
    private AVLoadingIndicatorView avi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginDetails = new LoginDetails();
        sharedHelper = new SharedHelper(this);
        gpsTracker = new TrackGPS(SignIn.this);
        isConnected = ConnectivityReceiver.isConnected();
       // locationTracker();
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        apiService = ApiUtils.getAPIService();
        ed_UserName = (AppCompatEditText) findViewById(R.id.ed_UserName);
        ed_Password = (AppCompatEditText) findViewById(R.id.ed_Password);
        tv_create_Acc = (TextView) findViewById(R.id.tv_create_Acc);
        tv_forgotpassword = (TextView) findViewById(R.id.tv_forgotpassword);
        //  sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        button_Google = (Button) findViewById(R.id.button_Google);
        fb_CustomButton = (Button) findViewById(R.id.fb_CustomButton);
        //  fb_Login = (LoginButton) findViewById(R.id.fb_Login);
        //   fb_Login.setReadPermissions("public_profile email");
        fb_CustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFacebookLogin();
            }
        });
        button_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        tv_create_Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected){
                    startActivity(new Intent(SignIn.this, SignUp.class));
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    SignIn.this.finish();
                }else {
                    Toast.makeText(SignIn.this, getString(R.string.no_internet) + "", Toast.LENGTH_SHORT).show();
                }


            }
        });
        tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected){
                    startActivity(new Intent(SignIn.this, ForgotPasswordActivity.class));
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }else {
                    Toast.makeText(SignIn.this, getString(R.string.no_internet) + "", Toast.LENGTH_SHORT).show();
                }

            }
        });

        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();

        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);

        setupFacebookStuff();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

       /* sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        for (int i = 0; i < sign_in_button.getChildCount(); i++) {
            View v = sign_in_button.getChildAt(i);

            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setText(null);
                tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.gplus_log));
                tv.setPadding(15, 15, 15, 15);
                return;
            }
        }*/
    }

    //-----------------------------------------------------------FaceBook-------------------------------------------------------------------------------------------//
    private void handleFacebookLogin() {

        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                mLoginManager.logOut();

            } else {
                mAccessTokenTracker.startTracking();
                mLoginManager.logInWithReadPermissions(SignIn.this, Arrays.asList("public_profile"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //------------------------------------------------------FaceBook------------------------------------------------------------------------------------------------//
    private void setupFacebookStuff() {

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                updateFacebookButtonUI();
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                try {
                    updateFacebookButtonUI();
                    Log.e(TAG, "SetupFacebookStuff : " + loginResult);
                    String id = loginResult.getAccessToken().getUserId();
                    sharedHelper.putKey(SignIn.this, "Social_Id", loginResult.getAccessToken().getUserId());
                    sharedHelper.putKey(SignIn.this, "firstName", profile.getFirstName());
                    sharedHelper.putKey(SignIn.this, "lastName", profile.getLastName());
                    sharedHelper.putKey(SignIn.this, "typeFacebook", "facebook");
                    String fname = profile.getFirstName();
                    String lname = profile.getLastName();

                    GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            JSONObject json = response.getJSONObject();
                            Log.e(TAG, "FB Object Request:" + object);
                            //       Log.e(TAG, " FB PROFILE CHECK : " + object.optString("email"));
                            try {
                                mEmail = object.optString("email");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }});
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","id,name,email");
                    graphRequest.setParameters(parameters);
                    graphRequest.executeAsync();
                    graphRequest.executeAsync();
                    socialLogin(id,fname,lname,mEmail,"facebook");

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SignIn.this, " Can't fetch user data \n try again ! ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    //Social Login
    private void socialLogin(final String id, final String fname, final String lname, final String mEmail, final String type) {

        try {
            apiService.getSocial(id).enqueue(new Callback<SocialDataModel>() {
                @Override
                public void onResponse(Call<SocialDataModel> call, Response<SocialDataModel> response) {
                    Log.e(TAG," onResponce success SocialLogin " +response.isSuccessful());

                    try {
                        if (response.body().getErrorMsg().equalsIgnoreCase("socialid already exists")){
                            Log.e(TAG," onResponce SocialLogin " +response.body().getErrorMsg());
                            String token = response.body().getAccessToken();
                            sharedHelper.putKey(SignIn.this,"access_token",token);
                            startActivity(new Intent(SignIn.this,MainActivity.class));
                            SignIn.this.finish();
                        }
                        else if (response.body().getErrorMsg().equalsIgnoreCase("New User")){
                            Log.e(TAG," onResponce SocialLogin " +response.body().getErrorMsg());
                            Intent intent = new Intent(SignIn.this,SignUpSecondPage.class);
                            intent.putExtra("firstNames",fname);
                            intent.putExtra("lastNames",lname);
                            intent.putExtra("mEmail",mEmail);
                            intent.putExtra("Social_Id",id);
                            intent.putExtra("Social_Type",type);
                            startActivity(intent);
                            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            SignIn.this.finish();
                        }
                    }catch (Exception e){
                        Toast.makeText(SignIn.this, "Something went wrong in server side !", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }
                @Override
                public void onFailure(Call<SocialDataModel> call, Throwable t) {
                    Log.e(TAG," onResponce Fail --> " + t.getLocalizedMessage());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SignIn.this, "Error occurred ! please try again", Toast.LENGTH_SHORT).show();
        }

    }

    //------------------------------------------------------------FaceBook------------------------------------------------------------------------------------------//
    private void updateFacebookButtonUI() {
        if (AccessToken.getCurrentAccessToken() != null) {
            //Login
            profile = Profile.getCurrentProfile();
            fb_CustomButton.setBackground(getResources().getDrawable(R.drawable.fb_logout));
        } else {
            //Logout
            fb_CustomButton.setBackground(getResources().getDrawable(R.drawable.fb_login));
        }
    }

    //------------------------------------------------------------FaceBook------------------------------------------------------------------------------------------//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e(TAG, "handleSignInResult:" + result.isSuccess());

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String id = acct.getId();
                String fName = acct.getGivenName();
                String lName = acct.getFamilyName();
                mEmail = acct.getEmail();
                sharedHelper.putKey(SignIn.this, "typeGoogleShared", "google");
                socialLogin(id,fName,lName,mEmail,"google");

            }
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signIn(View view) {
        Log.e(TAG, "signIn: ");
        mUsername = ed_UserName.getText().toString().trim();
        mPassword = ed_Password.getText().toString().trim();
       // progressDialog.show();
       // avi.show();
        avi.smoothToShow();

        if (mUsername.length() == 0 && mPassword.length() == 0) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        } else if (mUsername.length() == 0) {
            Toast.makeText(this, "Required Field Name", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();

        } else if (mPassword.length() == 0) {
            Toast.makeText(this, "Required Field Password", Toast.LENGTH_SHORT).show();
            progressDialog.cancel();
        } else {

            if (isConnected){
                try {
                    apiService.loginPost(getString(R.string.client_id), getString(R.string.credentials), getString(R.string.grant_type), mUsername, "*", mPassword).enqueue(new Callback<LoginDataModel>() {
                        @Override
                        public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {
                            Log.e(TAG, "onResponse: " + response.isSuccessful());
                          //  progressDialog.dismiss();
                            avi.hide();
                            if (response.isSuccessful()) {
                                Log.e(TAG, "Login_Responce" + response.body().getAccessToken());
                                sharedHelper.putKey(SignIn.this, "access_token", response.body().getAccessToken());
                                sharedHelper.putKey(SignIn.this, "token_type", response.body().getTokenType());
                                sharedHelper.putKey(SignIn.this, "login_type", "Logged");
                                startActivity(new Intent(SignIn.this, MainActivity.class));
                                SignIn.this.finish();
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            } else {
                                Toast.makeText(SignIn.this, "The user credentials were incorrect", Toast.LENGTH_SHORT).show();
                              //  progressDialog.dismiss();
                                avi.hide();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginDataModel> call, Throwable t) {
                            Log.e(TAG, "Error_Login_Responce " + t.getLocalizedMessage());
                          //  progressDialog.dismiss();
                            avi.hide();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignIn.this, "Something went wrong ! Please try Again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }else {
              //  progressDialog.dismiss();
                avi.hide();
                Toast.makeText(SignIn.this, getString(R.string.no_internet)+ "", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SignIn.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignIn.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SignIn.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SignIn.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    askForGPS();
                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void askForGPS() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(SignIn.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onLocationChanged(Location location) {

        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());
        lats = location.getLatitude();
        lngs = location.getLongitude();
        sharedHelper.putKey(SignIn.this,"Lat",lat);
        sharedHelper.putKey(SignIn.this,"Lng",lng);
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();

    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            lats = mLocation.getLatitude();
            lngs = mLocation.getLongitude();
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.connect();
    }

    protected void startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }
}



