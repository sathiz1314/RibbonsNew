package com.ribbons.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ribbons.R;
import com.ribbons.helper.DialogCaller;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private TextView tv_create_Acc;
    private AppCompatEditText ed_UserName, ed_Password;
    private String mUsername, mPassword, mFullName, mEmail;
    private SignInButton sign_in_button;
    private static final String TAG = SignIn.class.getSimpleName();
    private int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton signInButton;
    private Button signOutButton,fb_CustomButton;
    private LoginButton fb_Login;
    LoginManager mLoginManager;
    CallbackManager callbackManager;
    AccessTokenTracker mAccessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        ed_UserName = (AppCompatEditText) findViewById(R.id.ed_UserName);
        ed_Password = (AppCompatEditText) findViewById(R.id.ed_Password);
        tv_create_Acc = (TextView) findViewById(R.id.tv_create_Acc);
        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        fb_CustomButton = (Button) findViewById(R.id.fb_CustomButton);
        fb_Login = (LoginButton) findViewById(R.id.fb_Login);
        fb_Login.setReadPermissions("public_profile email");
        fb_CustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFacebookLogin();
            }
        });
      //  fbLogin();
        setupFacebookStuff();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        tv_create_Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,SignUp.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });


    }



    private void handleFacebookLogin() {

        if (AccessToken.getCurrentAccessToken() != null){
            mLoginManager.logOut();
        }else{
            mAccessTokenTracker.startTracking();
            mLoginManager.logInWithReadPermissions(SignIn.this, Arrays.asList("public_profile"));
        }
    }

    private void setupFacebookStuff() {
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,AccessToken currentAccessToken) {
                updateFacebookButtonUI();
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                updateFacebookButtonUI();
                Log.e(TAG, "SetupFacebookStuff : " +loginResult);
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject json = response.getJSONObject();

                        Log.e(TAG,"Fb Graph Request :" +json.toString());
                        Log.e(TAG,"FB Object Request:" +object);
                        try {
                            Log.e(TAG," FB PROFILE CHECK : "+json.getString("name")+object.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void updateFacebookButtonUI() {
        if (AccessToken.getCurrentAccessToken() != null){
            //Login
            fb_CustomButton.setBackground(getResources().getDrawable(R.drawable.fb_logout));
        }else{
            //Logout
            fb_CustomButton.setBackground(getResources().getDrawable(R.drawable.fb_login));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e(TAG, "handleSignInResult:" + result.isSuccess());

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                startActivity(new Intent(SignIn.this, MainActivity.class));
                mFullName = acct.getDisplayName();
                mEmail = acct.getEmail();
                Toast.makeText(this, mFullName + " -- " + mEmail + "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signIn(View view) {

        mUsername = ed_UserName.getText().toString().trim();
        mPassword = ed_Password.getText().toString().trim();
        if (mUsername.length() == 0 && mPassword.length() == 0) {
            DialogCaller.showDialog(SignIn.this, "Ribbons", "All fields are mandatory", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (mUsername.length() == 0) {
            DialogCaller.showDialog(SignIn.this, "Ribbons", "Required Field", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        } else if (mPassword.length() == 0) {
            DialogCaller.showDialog(SignIn.this, "Ribbons", "Required Field", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        } else {

            startActivity(new Intent(SignIn.this, MainActivity.class));
            SignIn.this.finish();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }
}


 /* private void fbLogin() {
            fb_Login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d(TAG,"Fb Login Result : "+loginResult);
                    if (AccessToken.getCurrentAccessToken()!=null){
                        Log.d(TAG,"Fb ID : " + loginResult.getAccessToken().getUserId());
                        Profile profile = Profile.getCurrentProfile();
                        if (profile!=null){
                            Log.e("facebook_id", profile.getId());
                            Log.e("f_name", profile.getFirstName());
                            Log.e("m_name", profile.getMiddleName());
                            Log.e("l_name", profile.getLastName());
                            Log.e("full_name", profile.getName());
                            Log.e("profile_pic", profile.getProfilePictureUri(150,150).toString());
                        }
                        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                JSONObject json = response.getJSONObject();

                                Log.e(TAG,"Fb Graph Request :" +json.toString());
                                Log.e(TAG,"FB Object Request:" +object);
                                try {
                                    Log.e(TAG," FB PROFILE CHECK : "+json.getString("name")+object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields","id,name,email");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                        graphRequest.executeAsync();
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(TAG,"Fb Login Error : "+error);

                }
            });
 }*/
