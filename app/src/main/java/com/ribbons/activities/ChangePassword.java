package com.ribbons.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ribbons.R;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.ResponceDataModel;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText ed_oldPass,ed_newPass,ed_confirmPass;
    private Button bt_FooterSave;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private static final String TAG = "ChangePassword";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        //
        toolbar = (Toolbar) findViewById(R.id.global_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle(R.string.change_pass);
        toolbar.setTitleTextColor(getResources().getColor(R.color.home_menuColor));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                ChangePassword.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }});
        //
        ed_oldPass = (EditText) findViewById(R.id.ed_oldPass);
        ed_newPass = (EditText) findViewById(R.id.ed_newPass);
        ed_confirmPass = (EditText) findViewById(R.id.ed_confirmPass);
        bt_FooterSave = (Button) findViewById(R.id.bt_FooterSave);
        //
        final String token = sharedHelper.getKey(this,"access_token");
        final String accept = getString(R.string.accept);
        final String type = getString(R.string.token_type);
        bt_FooterSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpass = ed_oldPass.getText().toString().trim();
                String newpass = ed_newPass.getText().toString().trim();
                String confirmpass = ed_confirmPass.getText().toString().trim();
                if (oldpass.length()==0 && newpass.length()==0&& confirmpass.length()==0){
                    Toast.makeText(ChangePassword.this, "All fields are mandatory ", Toast.LENGTH_SHORT).show();

                }else if (oldpass.length()==0){
                    Toast.makeText(ChangePassword.this, "OldPassword required field ", Toast.LENGTH_SHORT).show();

                }else if (newpass.length()==0){
                    Toast.makeText(ChangePassword.this, "NewPassword required field ", Toast.LENGTH_SHORT).show();

                }else if (confirmpass.length()==0){
                    Toast.makeText(ChangePassword.this, "ConfirmPassword required field ", Toast.LENGTH_SHORT).show();

                }else {
                    if (newpass.equalsIgnoreCase(confirmpass)){
                        operation(accept,type,token,oldpass,newpass,confirmpass);

                    }else {
                        Toast.makeText(ChangePassword.this, "Password doesn't match", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }

    private void operation(String accept, String type, String token, String oldpass, String newpass, String confirmpass) {
        progressDialog.show();
        try {
            apiService.getChangePasswordData(accept,type+" "+ token,oldpass,newpass,confirmpass).enqueue(new Callback<ResponceDataModel>() {
                @Override
                public void onResponse(Call<ResponceDataModel> call, Response<ResponceDataModel> response) {
                    progressDialog.dismiss();
                    Log.e(TAG,"onResponce Success -->" + response.isSuccessful());
                    if (response.isSuccessful()){
                        if (response.body().getError().equalsIgnoreCase("false")){
                            Toast.makeText(ChangePassword.this, response.body().getErrorMsg()+"", Toast.LENGTH_SHORT).show();
                        }
                        else if (response.body().getError().equalsIgnoreCase("true")){
                            Toast.makeText(ChangePassword.this, response.body().getErrorMsg()+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponceDataModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e(TAG,"onResponce Fail -->" + t.getLocalizedMessage());

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
