package com.ribbons.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.easing.linear.Linear;
import com.ribbons.R;
import com.ribbons.helper.AppController;
import com.ribbons.helper.AppHelper;
import com.ribbons.helper.ConstantsUrl;
import com.ribbons.helper.SharedHelper;
import com.ribbons.helper.VolleyMultipartRequest;
import com.ribbons.modeldatas.LocationModel;
import com.ribbons.modeldatas.Success;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.ribbons.retromodels.UserProfileModelData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

import static android.support.constraint.R.id.parent;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Toolbar toolbar;
    private TextView tvFirstName, tvLastName, tvDob, tvPhone, tvEmail, tvLocation, tvEditProfile;
    private RadioButton rdFirst, rdSecond, rdMale, rdFemale;
    private SharedHelper sharedHelper;
    private TextView tv_Pin1, tv_Pin2, tv_Pin3, tv_Pin4, tv_Pin5, tv_Pin6, tv_Pin7, tv_Pin8, tvDobEdit;
    private LinearLayout linearShow, linearEdit;
    private ScrollView scrollShow, scrollEdit;
    private Button footer_Save;
    private EditText EditPhone, EditLastName, EditFirstName;
    private Calendar calendar, cal1;
    private ImageView ivEdit;
    private static final int SELECT_PHOTO = 100;
    private AppCompatSpinner spinner_Location;
    private APIService apiService;
    private static final String TAG = "ProfileActivity";
    private List<String> stringArrayList = new ArrayList<String>();
    private String mLocation, mGender, mFirstname = "", mLastName = "", mDob = "", mPhone = "";
    private RadioGroup rgGroup;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //
        sharedHelper = new SharedHelper(ProfileActivity.this);
        ButterKnife.bind(this);
        calendar = Calendar.getInstance();
        apiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle(R.string.my_profile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.home_menuColor));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                ProfileActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        //
        ivEdit = (ImageView) findViewById(R.id.ivEdit);
        EditFirstName = (EditText) findViewById(R.id.EditFirstName);
        EditLastName = (EditText) findViewById(R.id.EditLastName);
        EditPhone = (EditText) findViewById(R.id.EditPhone);
        EditFirstName = (EditText) findViewById(R.id.EditFirstName);
        EditFirstName = (EditText) findViewById(R.id.EditFirstName);
        //
        footer_Save = (Button) findViewById(R.id.footer_Save);
        scrollShow = (ScrollView) findViewById(R.id.scrollShow);
        scrollEdit = (ScrollView) findViewById(R.id.scrollEdit);
        //
        tvEditProfile = (TextView) findViewById(R.id.tvEditProfile);
        tv_Pin1 = (TextView) findViewById(R.id.tv_Pin1);
        tv_Pin2 = (TextView) findViewById(R.id.tv_Pin2);
        tv_Pin3 = (TextView) findViewById(R.id.tv_Pin3);
        tv_Pin4 = (TextView) findViewById(R.id.tv_Pin4);
        tv_Pin5 = (TextView) findViewById(R.id.tv_Pin5);
        tv_Pin6 = (TextView) findViewById(R.id.tv_Pin6);
        tv_Pin7 = (TextView) findViewById(R.id.tv_Pin7);
        tv_Pin8 = (TextView) findViewById(R.id.tv_Pin8);
        //
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvDobEdit = (TextView) findViewById(R.id.tvDobEdit);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        //
        rgGroup = (RadioGroup) findViewById(R.id.rgGroup);
        rdFirst = (RadioButton) findViewById(R.id.rdFirst);
        rdSecond = (RadioButton) findViewById(R.id.rdSecond);
        rdMale = (RadioButton) findViewById(R.id.rdMale);
        rdFemale = (RadioButton) findViewById(R.id.rdFemale);
        spinner_Location = (AppCompatSpinner) findViewById(R.id.spinner_Location);
        //
        final String accept = getString(R.string.accept);
        final String type = getString(R.string.token_type);
        final String token = sharedHelper.getKey(ProfileActivity.this, "access_token");
        //
        tvFirstName.setText(sharedHelper.getKey(ProfileActivity.this, "getFirstname"));
        tvLastName.setText(sharedHelper.getKey(ProfileActivity.this, "getLastname"));
        tvDob.setText(sharedHelper.getKey(ProfileActivity.this, "getDob"));
        tvPhone.setText(sharedHelper.getKey(ProfileActivity.this, "getPhonenumber"));
        tvEmail.setText(sharedHelper.getKey(ProfileActivity.this, "getEmail"));
        tvLocation.setText(sharedHelper.getKey(ProfileActivity.this, "getLocation"));
        String gender = sharedHelper.getKey(ProfileActivity.this, "getGender");
        String ribbonPin = sharedHelper.getKey(ProfileActivity.this, "getRibbonpin");
        //
        EditFirstName.setText(sharedHelper.getKey(ProfileActivity.this, "getFirstname"));
        EditLastName.setText(sharedHelper.getKey(ProfileActivity.this, "getLastname"));
        EditPhone.setText(sharedHelper.getKey(ProfileActivity.this, "getPhonenumber"));
        tvDobEdit.setText(String.valueOf(sharedHelper.getKey(ProfileActivity.this, "getDob")));

        //
        if (gender.equalsIgnoreCase("m")) {
            rdFirst.setChecked(true);
            rdMale.setChecked(true);
        } else {
            rdSecond.setChecked(true);
            rdFemale.setChecked(true);
        }
        //
        try {
            tv_Pin1.setText(String.valueOf(ribbonPin.substring(0, 1)));
            tv_Pin2.setText(String.valueOf(ribbonPin.substring(1, 2)));
            tv_Pin3.setText(String.valueOf(ribbonPin.substring(2, 3)));
            tv_Pin4.setText(String.valueOf(ribbonPin.substring(3, 4)));
            tv_Pin5.setText(String.valueOf(ribbonPin.substring(4, 5)));
            tv_Pin6.setText(String.valueOf(ribbonPin.substring(5, 6)));
            tv_Pin7.setText(String.valueOf(ribbonPin.substring(6, 7)));
            tv_Pin8.setText(String.valueOf(ribbonPin.substring(7, 8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        locationOperation();
        //
        rGOperation();
        //
        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollShow.setVisibility(View.GONE);
                scrollEdit.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight).duration(800).playOn(scrollEdit);
            }
        });
        //
        footer_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mFirstname = EditFirstName.getText().toString().trim();
                String mLastName = EditLastName.getText().toString().trim();
                String mPhone = EditPhone.getText().toString().trim();
                String dob = tvDobEdit.getText().toString().trim();
                updateOperation(accept, type, token,mFirstname,mLastName,mPhone,dob);
            }
        });
        //
        tvDobEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
        //
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
            }
        });
    }

    //
    private void rGOperation() {
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rdMale.isChecked()) {
                    mGender = "m";
                    Toast.makeText(ProfileActivity.this, mGender + "", Toast.LENGTH_SHORT).show();
                } else if (rdFemale.isChecked()) {
                    mGender = "f";
                    Toast.makeText(ProfileActivity.this, mGender + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //
    private void locationOperation() {
        apiService.locationGet().enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, retrofit2.Response<LocationModel> response) {

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
        });
    }

    //
    private void spinnerOperation(retrofit2.Response<LocationModel> response) {
        stringArrayList.clear();
        stringArrayList.add("Select");
        for (Success success : response.body().getSuccess()) {
            stringArrayList.add(success.getLocationname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, stringArrayList);
        // adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ivEdit.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void selectDate() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(ProfileActivity.this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setYearRange(1940, 1999);
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.showYearPickerFirst(true);
        dpd.setAccentColor(getResources().getColor(R.color.button_globalColor));
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        tvDobEdit.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        mDob = tvDobEdit.getText().toString().trim();
    }

    //
    private void updateOperation(final String accept, final String type, final String token, final String mFirstname, final String mLastName, final String mPhone, final String dob) {
        progressDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ConstantsUrl.Base_Url + ConstantsUrl.UpDateMyProfile_Url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.dismiss();
                //
                String res = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    Log.e(TAG, "onResponce Update " + jsonObject.toString());
                    userProfileOperation(accept, type, token);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //
                Toast.makeText(ProfileActivity.this, error + "", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", accept);
                headers.put("Authorization", type + " " + token);
                return headers;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                params.put("image", new VolleyMultipartRequest.DataPart("image1.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), ivEdit.getDrawable()), "image/jpeg"));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname",mFirstname);
                params.put("lastname", mLastName);
                params.put("dob", dob);
                params.put("location", mLocation);
                params.put("gender", mGender);
                params.put("phonenumber", mPhone);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void userProfileOperation(String accept, String type, String token) {

        apiService.getUserProfile(accept, type + " " + token, "12345").enqueue(new Callback<UserProfileModelData>() {
            @Override
            public void onResponse(Call<UserProfileModelData> call, retrofit2.Response<UserProfileModelData> response) {
                Log.e(TAG, "onResponce userProfile Success " + response.isSuccessful());
                //
                if (response.isSuccessful()) {

                    String fName = response.body().getUserdata().get(0).getFirstname();
                    String lName = response.body().getUserdata().get(0).getLastname();
                    String dob = response.body().getUserdata().get(0).getDob();
                    String gender = response.body().getUserdata().get(0).getGender();
                    String phone = response.body().getUserdata().get(0).getPhonenumber();
                    String email = response.body().getUserdata().get(0).getEmail();
                    String location = response.body().getUserdata().get(0).getLocation();
                    String ribbonPin = response.body().getUserdata().get(0).getRibbonpin();
                    //
                    sharedHelper.putKey(ProfileActivity.this, "getRibbonpin", ribbonPin);
                    sharedHelper.putKey(ProfileActivity.this, "getFirstname", fName);
                    sharedHelper.putKey(ProfileActivity.this, "getLastname", lName);
                    sharedHelper.putKey(ProfileActivity.this, "getDob", dob);
                    sharedHelper.putKey(ProfileActivity.this, "getGender", gender);
                    sharedHelper.putKey(ProfileActivity.this, "getPhonenumber", phone);
                    sharedHelper.putKey(ProfileActivity.this, "getEmail", email);
                    sharedHelper.putKey(ProfileActivity.this, "getLocation", location);
                    scrollEdit.setVisibility(View.GONE);
                    YoYo.with(Techniques.SlideInLeft).duration(800).playOn(scrollShow);
                    scrollShow.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to updated ! Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileModelData> call, Throwable t) {
                Log.e(TAG, "onResponce userProfile Fail " + t.getLocalizedMessage());
            }
        });

    }
}
