package com.ribbons.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ribbons.R;
import com.ribbons.adapters.MyRibbonsAdapter;
import com.ribbons.adapters.SendVoucherAdapter;
import com.ribbons.helper.AppController;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.ConstantsUrl;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.MyRibbonDataModel;
import com.ribbons.modeldatas.Myribbon;
import com.ribbons.modeldatas.SendVoucherDataModel;
import com.ribbons.modeldatas.UnTieRibbonDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendVoucherDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewSendVoucher;
    // private View ivMenu;
    private ImageView ivMenu;
    private View guillotineMenu;
    private static final long RIPPLE_DURATION = 350;
    private AppBarLayout appBar;
    private CoordinatorLayout root;
    private ArrayList<HomeModelData> modelDataArrayList = new ArrayList<HomeModelData>();
    private AppCompatEditText ed_code1, ed_code2, ed_code3, ed_code4, ed_code5, ed_code6, ed_code7, ed_code8;
    private Button bt_SendVoucher;
    private ProgressDialog progressDialog;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private SendVoucherAdapter sendVoucherAdapter;
    private MyRibbonsAdapter myRibbonsAdapter;
    private RelativeLayout relativeBg;
    private static final String TAG = "SendVoucherDetails";
    private boolean isConnected;
    private NestedScrollView nested;
    private String token = "", accept = "", type = "";
    public static String rId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_voucher_details);
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        isConnected = ConnectivityReceiver.isConnected();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nested = (NestedScrollView) findViewById(R.id.nested);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        relativeBg = (RelativeLayout) findViewById(R.id.relativeBg);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle("SEND VOUCHER");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorGlobal));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                SendVoucherDetails.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        bt_SendVoucher = (Button) findViewById(R.id.bt_SendVoucher);
        ed_code1 = (AppCompatEditText) findViewById(R.id.ed_code1);
        ed_code2 = (AppCompatEditText) findViewById(R.id.ed_code2);
        ed_code3 = (AppCompatEditText) findViewById(R.id.ed_code3);
        ed_code4 = (AppCompatEditText) findViewById(R.id.ed_code4);
        ed_code5 = (AppCompatEditText) findViewById(R.id.ed_code5);
        ed_code6 = (AppCompatEditText) findViewById(R.id.ed_code6);
        ed_code7 = (AppCompatEditText) findViewById(R.id.ed_code7);
        ed_code8 = (AppCompatEditText) findViewById(R.id.ed_code8);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(1);
        ed_code1.setFilters(FilterArray);
        ed_code2.setFilters(FilterArray);
        ed_code3.setFilters(FilterArray);
        ed_code4.setFilters(FilterArray);
        ed_code5.setFilters(FilterArray);
        ed_code6.setFilters(FilterArray);
        ed_code7.setFilters(FilterArray);
        ed_code8.setFilters(FilterArray);
        //
        ed_code1.addTextChangedListener(textWatcher1);
        ed_code2.addTextChangedListener(textWatcher2);
        ed_code3.addTextChangedListener(textWatcher3);
        ed_code4.addTextChangedListener(textWatcher4);
        ed_code5.addTextChangedListener(textWatcher5);
        ed_code6.addTextChangedListener(textWatcher6);
        ed_code7.addTextChangedListener(textWatcher7);
        ed_code8.addTextChangedListener(textWatcher8);

        token = sharedHelper.getKey(this, "access_token");
        accept = getString(R.string.accept);
        type = getString(R.string.token_type);
        /*root = (CoordinatorLayout) findViewById(R.id.root);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                SendVoucherDetails.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });*/

        recyclerViewSendVoucher = (RecyclerView) findViewById(R.id.recyclerViewSendVoucher);
        recyclerViewSendVoucher.setHasFixedSize(true);
        recyclerViewSendVoucher.setLayoutManager(new LinearLayoutManager(this));
        operation(accept, type, token);
        bt_SendVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code1="", code2="", code3="", code4="", code5="", code6="", code7="", code8="",fullPin;
                code1 = ed_code1.getText().toString().trim();
                code2 = ed_code2.getText().toString().trim();
                code3 = ed_code3.getText().toString().trim();
                code4 = ed_code4.getText().toString().trim();
                code5 = ed_code5.getText().toString().trim();
                code6 = ed_code6.getText().toString().trim();
                code7 = ed_code7.getText().toString().trim();
                code8 = ed_code8.getText().toString().trim();
                fullPin = code1+code2+code3+code4+code5+code6+code7+code8;
              //  progressDialog.show();
                if (code1.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code2.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code3.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code4.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code5.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code6.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code7.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else if (code8.length()==0){
                    progressDialog.dismiss();
                    Toast.makeText(SendVoucherDetails.this, getString(R.string.ribbon_pin_mandatory)+"", Toast.LENGTH_SHORT).show();
                }else {
                  //  Toast.makeText(SendVoucherDetails.this, rId+"", Toast.LENGTH_SHORT).show();
                    alertOperation(accept,type,token,fullPin,rId);
                }

            }
        });
    }

    private void operation(String accept, String type, String token) {
        if (isConnected) {
            progressDialog.show();
            apiService.getMyRibbonData(accept, type + " " + token, "458").enqueue(new Callback<MyRibbonDataModel>() {
                @Override
                public void onResponse(Call<MyRibbonDataModel> call, Response<MyRibbonDataModel> response) {
                    progressDialog.dismiss();
                    Log.e(TAG, " onResponce Success --> " + response.isSuccessful());
                    try {
                        if (response.isSuccessful()) {
                            Log.e(TAG, " data " + response.body().getError());
                            if (response.body().getError().equalsIgnoreCase("false")) {
                                List<Myribbon> myribbons = response.body().getMyribbons();

                                sendVoucherAdapter = new SendVoucherAdapter(SendVoucherDetails.this, myribbons);
                                recyclerViewSendVoucher.setAdapter(sendVoucherAdapter);

                            } else if (response.body().getError().equalsIgnoreCase("true")) {
                                relativeBg.setVisibility(View.VISIBLE);
                                nested.setVisibility(View.GONE);
                                bt_SendVoucher.setVisibility(View.GONE);
                                Toast.makeText(SendVoucherDetails.this, " No Ribbons ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SendVoucherDetails.this, "Auth fail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SendVoucherDetails.this, SignIn.class));
                            SendVoucherDetails.this.finish();
                        }
                        sendVoucherAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MyRibbonDataModel> call, Throwable t) {
                    Log.e(TAG, " onResponce Fail -->" + t.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            });

        } else {
            Toast.makeText(SendVoucherDetails.this, getResources().getString(R.string.no_internet) + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertOperation(final String accept, final String type, final String token, final String fullPin, final String rId) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        final AlertDialog a = alertDialog.create();
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogue_sendvoucher, null);
        a.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        final Button bt_Yes = (Button) dialogView.findViewById(R.id.bt_Yes);
        final Button bt_No = (Button) dialogView.findViewById(R.id.bt_No);
        a.setView(dialogView);
        a.show();
        bt_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
                progressDialog.show();
                apiService.getSendVoucher(accept,type +" " +token,fullPin,rId).enqueue(new Callback<SendVoucherDataModel>() {
                    @Override
                    public void onResponse(Call<SendVoucherDataModel> call, Response<SendVoucherDataModel> response) {
                        progressDialog.dismiss();
                        Log.e(TAG," onResponce Send Success --> " +response.isSuccessful() );

                        if (response.isSuccessful()){
                            if (response.body().getError().equalsIgnoreCase("false")){


                            }else if (response.body().getError().equalsIgnoreCase("true")){
                                Toast.makeText(SendVoucherDetails.this, response.body().getErrorMsg()+ "", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<SendVoucherDataModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG," onResponce Fail --> " +t.getLocalizedMessage() );
                    }
                });
                //-------------------------------------------------------------------------------------------------------------------------------------//
               /* JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ribbonpin",fullPin);
                    jsonObject.put("ribbonid",rId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ConstantsUrl.Base_Url+ConstantsUrl.TransferRibbon_Url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"onResponce send Success --> " + response.toString());
                        progressDialog.dismiss();

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(SendVoucherDetails.this, error + "", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Accept", accept);
                        headers.put("Authorization", type + " " + token);
                        return headers;
                    }};
                AppController.getInstance().addToRequestQueue(jsonObjectRequest);*/
                //-------------------------------------------------------------------------------------------------------------------------------------//

            }
        });
        bt_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });
    }

    /*private void menuOperation() {
        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), ivMenu)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(appBar)
                .setClosedOnStart(true)
                .build();
    }*/

    TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code2.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code3.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher3 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code4.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher4 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code5.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher5 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code6.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher6 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code7.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher7 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1) {
                ed_code8.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcher8 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
