package com.ribbons.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ribbons.R;
import com.ribbons.gps.TrackGPS;
import com.ribbons.helper.AppController;
import com.ribbons.helper.ConstantsUrl;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.LocationModel;
import com.ribbons.modeldatas.Success;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.ribbons.retrohelper.SignupClass;
import com.ribbons.retromodels.OtpDataModel;
import com.ribbons.retromodels.SignupDataModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpSecondPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, GoogleApiClient.OnConnectionFailedListener {

    public static GoogleApiClient mGoogleApiClient;
    private AppCompatEditText ed_FirstName, ed_LastName, ed_phone, ed_location, ed_Dob;
    private TextView tv_Pin1, tv_Pin2, tv_Pin3, tv_Pin4, tv_Pin5, tv_Pin6, tv_Pin7, tv_Pin8, txt, tvLocation;
    private AppCompatTextView tv_Dob;
    private RadioGroup rg_Button;
    private RadioButton rb_Male, rb_Female;
    private Calendar calendar, cal1;
    private DateFormat dateFormat;
    private AppCompatSpinner spinner_Location;
    private String mFirstName = "", mLastName = "", mPhone = "", mDob = "", mLocation = "", mGender = "", mRandomValue = "", mPinValuesHalf = "", mPinValuesFull = "", checkPin = "";
    private String mPin1, mPin2, mPin3, mPin4, mPin5, mPin6, mPin7, mPin8, mEmail, mPassword, mRePassword, gLocation = "", Social_Id = "",Social_Type="";
    private TextWatcher textWatcherPin1;
    private Random random;
    private int i1;
    private APIService apiService;
    public static final String TAG = SignUpSecondPage.class.getSimpleName();
    private ArrayList<Success> successArrayList = new ArrayList<Success>();
    private List<String> stringArrayList = new ArrayList<String>();
    private SignupClass signupClass;
    private SharedHelper sharedHelper;
    private String typeNormal = "normal", typeFacebook = "facebook", typeGoogle = "google";
    private String gId = "", fbId = "", gEmail, fbMail;
    private ProgressDialog progressDialog;
    TrackGPS gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_page);
        //
        sharedHelper = new SharedHelper(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        apiService = ApiUtils.getAPIService();
        gpsTracker = new TrackGPS(SignUpSecondPage.this);
        calendar = Calendar.getInstance();
        signupClass = new SignupClass();
        random = new Random();
        Random rand = new Random();
        i1 = rand.nextInt(99999);
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        //
        ed_FirstName = (AppCompatEditText) findViewById(R.id.ed_FirstName);
        ed_LastName = (AppCompatEditText) findViewById(R.id.ed_LastName);
        ed_phone = (AppCompatEditText) findViewById(R.id.ed_phone);
        tv_Dob = (AppCompatTextView) findViewById(R.id.tv_Dob);
        tv_Pin1 = (TextView) findViewById(R.id.tv_Pin1);
        tv_Pin2 = (TextView) findViewById(R.id.tv_Pin2);
        tv_Pin3 = (TextView) findViewById(R.id.tv_Pin3);
        tv_Pin4 = (TextView) findViewById(R.id.tv_Pin4);
        tv_Pin5 = (TextView) findViewById(R.id.tv_Pin5);
        tv_Pin6 = (TextView) findViewById(R.id.tv_Pin6);
        tv_Pin7 = (TextView) findViewById(R.id.tv_Pin7);
        tv_Pin8 = (TextView) findViewById(R.id.tv_Pin8);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        txt = (TextView) findViewById(R.id.txt);
        spinner_Location = (AppCompatSpinner) findViewById(R.id.spinner_Location);
        rg_Button = (RadioGroup) findViewById(R.id.rg_Button);
        rb_Male = (RadioButton) findViewById(R.id.rb_Male);
        rb_Female = (RadioButton) findViewById(R.id.rb_Female);

        //
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //
        ed_FirstName.addTextChangedListener(textWatcherFirstName);
        ed_LastName.addTextChangedListener(textWatcherLastName);
        ed_phone.addTextChangedListener(textWatcherPhoen);
        String fname = getIntent().getStringExtra("firstNames");
        String lname = getIntent().getStringExtra("lastNames");
        ed_FirstName.setText(fname);
        ed_LastName.setText(lname);
        //
        mEmail = getIntent().getStringExtra("mEmail");
        mPassword = getIntent().getStringExtra("mPassword");
        mRePassword = getIntent().getStringExtra("ed_RePassword");
        Social_Id = getIntent().getStringExtra("Social_Id");
        Social_Type = getIntent().getStringExtra("Social_Type");
        if (mEmail == null){
            alertEmail();
        }

        //
        Toast.makeText(SignUpSecondPage.this, Social_Type+ " \n" + mEmail + " \n" + Social_Id + "\n", Toast.LENGTH_SHORT).show();
        //
        try {
            mRandomValue = String.valueOf(i1).substring(0, 1) + String.valueOf(i1).substring(1, 2) + String.valueOf(i1).substring(2, 3) + String.valueOf(i1).substring(3, 4);
           /* tv_Pin5.setText(String.valueOf(i1).substring(0, 1));
            tv_Pin6.setText(String.valueOf(i1).substring(1, 2));
            tv_Pin7.setText(String.valueOf(i1).substring(2, 3));
            tv_Pin8.setText(String.valueOf(i1).substring(3, 4));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        getGeoAddress();
        locationOperation();
        dateOperation();
        rGOperation();
    }

    // EmailAlert----
    private void alertEmail() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpSecondPage.this);
        LayoutInflater inflater = getLayoutInflater();
        alertDialog.setCancelable(false);
        final AlertDialog a = alertDialog.create();
        a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        View dialogView= inflater.inflate(R.layout.alert_email_dialogue, null);
        a.setView(dialogView);
        a.show();
        final Button btSubmit = (Button) dialogView.findViewById(R.id.btSubmit);
        final AppCompatEditText ed_Email = (AppCompatEditText) dialogView.findViewById(R.id.ed_Email);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_Email.getText().toString().trim();
                a.cancel();
            }
        });
    }

    private void getGeoAddress() {

        String lat = sharedHelper.getKey(SignUpSecondPage.this, "Lat");
        String lng = sharedHelper.getKey(SignUpSecondPage.this, "Lng");
        double latitude = SignIn.lats, longitude = SignIn.lngs;
        //  Toast.makeText(SignUpSecondPage.this, latitude+"", Toast.LENGTH_SHORT).show();

        /*if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }*/
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append("\n");
                }

                sb.append(address.getLocality()).append("\n");
                //  Toast.makeText(SignUpSecondPage.this, address.getLocality()+"", Toast.LENGTH_SHORT).show();
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
                result = sb.toString();
                String str = address.getAddressLine(1);
                String[] animalsArray = str.split(",");
                String data = animalsArray[1];
                tvLocation.setText(data);
                //   Toast.makeText(SignUpSecondPage.this, data+"", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        }
    }

    //
    private void locationOperation() {
       /* apiService.locationGet().enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {

                try {
                    if (response.isSuccessful()) {
                        for (Success success : response.body().getSuccess()) {
                            Log.e(TAG, "SuccessList : " + success.getLocationname());
                            spinnerOperation(response);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                Log.e(TAG, "Error :" + t.getLocalizedMessage());
            }
        });*/
    }

    //
    private void rGOperation() {
        rg_Button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_Male.isChecked()) {
                    mGender = "m";
                } else if (rb_Female.isChecked()) {
                    mGender = "f";
                }
            }
        });

    }

    //
    private void spinnerOperation(Response<LocationModel> response) {
        stringArrayList.clear();
        stringArrayList.add("Select");
        for (Success success : response.body().getSuccess()) {
            stringArrayList.add(success.getLocationname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpSecondPage.this, R.layout.spinner_item, stringArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_Location.setAdapter(adapter);
        spinner_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mLocation = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //
    private void dateOperation() {
        tv_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(SignUpSecondPage.this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setYearRange(1940, 1999);
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.showYearPickerFirst(true);
                dpd.setAccentColor(getResources().getColor(R.color.button_globalColor));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    //
    public void next(View view) {
        progressDialog.show();
        mFirstName = ed_FirstName.getText().toString().trim();
        mLastName = ed_LastName.getText().toString().trim();
        mPhone = ed_phone.getText().toString().trim();
        mDob = tv_Dob.getText().toString().trim();
        gLocation = tvLocation.getText().toString().trim();
        mPin1 = tv_Pin1.getText().toString().trim();
        mPin2 = tv_Pin2.getText().toString().trim();
        mPin3 = tv_Pin3.getText().toString().trim();
        mPin4 = tv_Pin4.getText().toString().trim();
        mPin5 = tv_Pin5.getText().toString().trim();
        mPin6 = tv_Pin6.getText().toString().trim();
        mPin7 = tv_Pin7.getText().toString().trim();
        mPin8 = tv_Pin8.getText().toString().trim();
        checkPin = mPin5 + mPin6 + mPin7 + mPin8;
        mPinValuesHalf = mPin1 + mPin2 + mPin3 + mPin4;
        mPinValuesFull = mPin1 + mPin2 + mPin3 + mPin4 + mPin5 + mPin6 + mPin7 + mPin8;

        if (mFirstName.length() == 0 && mLastName.length() == 0 && mPhone.length() == 0 && mDob.length() == 0 && mGender.length() == 0 && gLocation.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "All are mandatory field !", Toast.LENGTH_SHORT).show();
        } else if (mFirstName.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "First Name required field !", Toast.LENGTH_SHORT).show();
        } else if (mLastName.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "Last Name required field !", Toast.LENGTH_SHORT).show();
        } else if (mDob.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "Date of Birth required field !", Toast.LENGTH_SHORT).show();
        } else if (mGender.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "Gender required field !", Toast.LENGTH_SHORT).show();
        } else if (gLocation.length() == 0) {
            progressDialog.dismiss();
            Toast.makeText(this, "Location required field !", Toast.LENGTH_SHORT).show();
        } else {
            String mFacebook = "", mNormal = "", mGoogle = "";
            try {
                mFacebook = Social_Type;
                mNormal = sharedHelper.getKey(SignUpSecondPage.this, "typeNormal");
                mGoogle = Social_Type;
            } catch (Exception e) {
                e.printStackTrace();
            }

//---------------------------------------------------noraml sign in -----------------------------------------------------------------------------------//
            if (mPhone.length() > 0 && Social_Type.equalsIgnoreCase("normal")) {

                if (mPhone.length() < 10) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
                } else if (mPhone.length() > 10) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "TYPE --> " + typeNormal);
                    otpOperation(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesFull, mPhone, mEmail, mPassword, "", typeNormal);
                }

            } else if (mPhone.length() == 0 && Social_Type.equalsIgnoreCase("normal")) {

                if (checkPin.length() == 0) {
                    pinAlert();
                } else if (checkPin.length() > 0) {
                    Log.e(TAG, "TYPE --> " + typeNormal);
                    signupOperationWithoutMobile(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesHalf + mRandomValue, mEmail, mPassword, "", typeNormal);
                }
            }
//---------------------------------------------------noraml sign in -----------------------------------------------------------------------------------//

//---------------------------------------------------google sign in -----------------------------------------------------------------------------------//
            else if (mPhone.length() > 0 && Social_Type.equalsIgnoreCase("google")) {

                otpOperation(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesFull, mPhone, mEmail, mPassword, Social_Id, Social_Type);
                Log.e(TAG, "TYPE --> " + Social_Type);

            } else if (mPhone.length() == 0 && Social_Type.equalsIgnoreCase("google")) {
                if (checkPin.length() == 0) {
                    pinAlert();
                } else if (checkPin.length() > 0) {
                    signupOperationWithoutMobile(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesHalf + mRandomValue, mEmail, mPassword, Social_Id, Social_Type);
                    Log.e(TAG, "TYPE --> " + Social_Type);
                }
            }
//---------------------------------------------------------------------google sign in -----------------------------------------------------------------------------------//

//---------------------------------------------------Fb sign in -----------------------------------------------------------------------------------//
            else if (mPhone.length() == 0 && Social_Type.equalsIgnoreCase("facebook")) {
                if (checkPin.length() == 0) {
                    pinAlert();
                } else if (checkPin.length() > 0) {
                    signupOperationWithoutMobile(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesHalf + mRandomValue, mEmail, mPassword, Social_Id, Social_Type);
                    Log.e(TAG, "TYPE --> " + Social_Type);
                }
            } else if (mPhone.length() > 0 && Social_Type.equalsIgnoreCase("facebook")) {
                otpOperation(mFirstName, mLastName, mDob, mGender, gLocation, mPinValuesFull, mPhone, mEmail, mPassword, Social_Id, Social_Type);
                Log.e(TAG, "TYPE --> " + Social_Type);
            }
////---------------------------------------------------Fb sign in -----------------------------------------------------------------------------------//

            else {
            }
        }
    }

    private void pinAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pin Random generation");
        builder.setMessage("Do you want to generate pin randomly");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tv_Pin5.setText(String.valueOf(i1).substring(0, 1));
                tv_Pin6.setText(String.valueOf(i1).substring(1, 2));
                tv_Pin7.setText(String.valueOf(i1).substring(2, 3));
                tv_Pin8.setText(String.valueOf(i1).substring(3, 4));
                progressDialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                progressDialog.dismiss();

            }
        });
        builder.show();
    }

    private void otpOperation(final String mFirstName, final String mLastName, final String mDob, final String mGender, final String mLocation, final String mRibbonPin, final String mPhone, final String mEmail, final String mPassword, final String mSocialId, final String typeNormal) {
        apiService.otp(mPhone).enqueue(new Callback<OtpDataModel>() {
            @Override
            public void onResponse(Call<OtpDataModel> call, Response<OtpDataModel> response) {
                progressDialog.dismiss();
                Log.e(TAG, "Otp Responce Success -->" + response.isSuccessful());

                if (response.isSuccessful()) {
                    Log.e(TAG, "Otp Responce -->" + response.body().getError());
                    signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mRibbonPin, mPhone, mEmail, mPassword, mSocialId, typeNormal);
                    //  signupOperationWithoutMobile(mFirstName,mLastName,mDob,mGender,mLocation,mRibbonPin,mEmail,mPassword,mSocialId,typeNormal);

                } else {
                    Toast.makeText(SignUpSecondPage.this, "Check Mobile Field", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OtpDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, " Otp Fail" + t.getLocalizedMessage());
            }
        });
    }


    private void signupOperationWithoutMobile(String mFirstName, String mLastName, String mDob, String mGender, String mLocation, String mRibbonPin, String mEmail, String mPassword, String mSocialId, String typeNormal) {

        apiService.signupPostWithoutMobile(mFirstName, mLastName, mEmail, mPassword, mDob, mGender, mLocation, typeNormal, mSocialId, mRibbonPin).enqueue(new Callback<SignupDataModel>() {
            @Override
            public void onResponse(Call<SignupDataModel> call, Response<SignupDataModel> response) {
                progressDialog.dismiss();
                Log.e(TAG, "onResponceWithoutMobile Success  --> " + response.isSuccessful());

                if (response.isSuccessful()) {
                    Log.e(TAG, "onResponceWithoutMobile -->" + response.body().getError());
                    if (response.body().getError().equalsIgnoreCase("false")) {
                        Toast.makeText(SignUpSecondPage.this, "Ribbon SignUp Completed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpSecondPage.this, SignIn.class));
                        SignUpSecondPage.this.finish();
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else {
                        Toast.makeText(SignUpSecondPage.this, response.body().getErrorMsg() + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, " onResponceWithoutMobile Fail" + t.getLocalizedMessage());
            }
        });
    }

    private void signupOperation(String mFirstName, String mLastName, String mDob, String mGender, String mLocation, String mRibbonPin, String mPhone, String mEmail, String mPassword, String mSocialId, String mType) {

        apiService.signupPost(mFirstName, mLastName, mEmail, mPassword, mDob, mPhone, mGender, mLocation, mType, mSocialId, mRibbonPin).enqueue(new Callback<SignupDataModel>() {
            @Override
            public void onResponse(Call<SignupDataModel> call, Response<SignupDataModel> response) {
                progressDialog.dismiss();
                Log.e(TAG, "onResponce --> : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Log.e(TAG, " SignupResponce -->" + response.body().getErrorMsg());
                    if (response.body().getError().equalsIgnoreCase("false")) {
                        Toast.makeText(SignUpSecondPage.this, "Ribbon SignUp Completed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpSecondPage.this, SignIn.class));
                        SignUpSecondPage.this.finish();
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    } else if (response.body().getError().equalsIgnoreCase("true")) {
                        Toast.makeText(SignUpSecondPage.this, response.body().getErrorMsg() + "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpSecondPage.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupDataModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, " onResponce Fail" + t.getLocalizedMessage());
            }
        });
    }

    //
    TextWatcher textWatcherFirstName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                tv_Pin1.setText(charSequence.toString().substring(0, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    //
    TextWatcher textWatcherLastName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                tv_Pin4.setText(charSequence.toString().substring(0, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    //
    TextWatcher textWatcherPhoen = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                /*tv_Pin5.setText(charSequence.toString().substring(0, 1));
                tv_Pin6.setText(charSequence.toString().substring(1, 2));
                tv_Pin7.setText(charSequence.toString().substring(2, 3));
                tv_Pin8.setText(charSequence.toString().substring(3, 4));*/
                tv_Pin5.setText(charSequence.toString().substring(6, 7));
                tv_Pin6.setText(charSequence.toString().substring(7, 8));
                tv_Pin7.setText(charSequence.toString().substring(8, 9));
                tv_Pin8.setText(charSequence.toString().substring(9, 10));
                if (charSequence.length() == 10) {
                    Toast.makeText(SignUpSecondPage.this, "Mobile number limit exceeded ! ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    //
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tv_Dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        String date1 = String.valueOf(dayOfMonth);
        String month = String.valueOf(monthOfYear + 1);
        if (date1.length() == 1 && month.length() == 1) {
            tv_Pin2.setText(String.valueOf(0));
            tv_Pin3.setText(String.valueOf(date1.substring(0, 1)));
            //tv_Pin4.setText(String.valueOf(monthOfYear + 1));
        } else if (date1.length() == 2 && month.length() == 1) {
            tv_Pin2.setText(String.valueOf(date1.substring(0, 1)));
            tv_Pin3.setText(String.valueOf(date1.substring(1, 2)));
            // tv_Pin4.setText(String.valueOf(monthOfYear + 1));
        } else if (date1.length() == 1 && month.length() == 2) {
            tv_Pin2.setText(String.valueOf(dayOfMonth));
            tv_Pin3.setText(String.valueOf(date1.substring(1, 2)));
            // tv_Pin4.setText(String.valueOf(month.substring(1, 2)));
        } else if (date1.length() == 2 && month.length() == 2) {
            tv_Pin2.setText(String.valueOf(date1.substring(0, 1)));
            tv_Pin3.setText(String.valueOf(date1.substring(1, 2)));
            // tv_Pin4.setText(String.valueOf(month.substring(1, 2)));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }
}

 /* tv_Pin6.setText(String.valueOf(i1).substring(0, 1));
        tv_Pin7.setText(String.valueOf(i1).substring(1, 2));
        tv_Pin8.setText(String.valueOf(i1).substring(2, 3));
        dateExtractOperation();*/


 /* if (mPhone.length() == 0 && mNormal.equalsIgnoreCase(typeNormal)) {
                    mRandomValue = String.valueOf(i1).substring(0, 1) + String.valueOf(i1).substring(1, 2) + String.valueOf(i1).substring(2, 3);
                    signupOperationWithoutMobile(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesHalf + mRandomValue,mEmail, mPassword,"",typeNormal);
                }*/

//  else if (mNormal.equalsIgnoreCase(typeNormal)) {
//      signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesFull, mPhone, mEmail, mPassword,"",typeNormal);
//   }


               /* else if (mPhone.length() == 0 && mFacebook.equalsIgnoreCase(typeFacebook)) {
                    mRandomValue = String.valueOf(i1).substring(0, 1) + String.valueOf(i1).substring(1, 2) + String.valueOf(i1).substring(2, 3);
                    signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesHalf + mRandomValue, mPhone, mEmail, mPassword, sharedHelper.getKey(SignUpSecondPage.this, "Social_Id"), typeFacebook);
                }
                else if (mFacebook.equalsIgnoreCase(typeFacebook)) {
                    signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesFull, mPhone, mEmail, mPassword, sharedHelper.getKey(SignUpSecondPage.this, "Social_Id"), typeFacebook);
                }
                else if (mPhone.length() == 0 && mGoogle.equalsIgnoreCase(typeGoogle)) {
                    mRandomValue = String.valueOf(i1).substring(0, 1) + String.valueOf(i1).substring(1, 2) + String.valueOf(i1).substring(2, 3);
                    signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesHalf + mRandomValue, mPhone, mEmail, mPassword, sharedHelper.getKey(SignUpSecondPage.this, "Social_Id"), typeGoogle);
                }
                else if (mGoogle.equalsIgnoreCase(typeGoogle)) {
                    signupOperation(mFirstName, mLastName, mDob, mGender, mLocation, mPinValuesFull, mPhone, mEmail, mPassword, sharedHelper.getKey(SignUpSecondPage.this, "Social_Id"), typeGoogle);
                }*/

