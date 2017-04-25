package com.ribbons.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.ribbons.R;
import com.ribbons.helper.DialogCaller;
import com.ribbons.helper.SharedHelper;


public class SignUp extends AppCompatActivity {

    private AppCompatEditText ed_Email,ed_Password,ed_RePassword;
    private String mEmail,mPassword,mRePassword;
    private SharedHelper sharedHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedHelper = new SharedHelper(this);
        ed_Email = (AppCompatEditText) findViewById(R.id.ed_Email);
        ed_Password = (AppCompatEditText) findViewById(R.id.ed_Password);
        ed_RePassword = (AppCompatEditText) findViewById(R.id.ed_RePassword);
    }

    public void next(View view) {
        mEmail = ed_Email.getText().toString().trim();
        mPassword = ed_Password.getText().toString().trim();
        mRePassword = ed_RePassword.getText().toString().trim();

        if (mEmail.length()==0&& mPassword.length()==0&&mRePassword.length()==0){
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }else if (mEmail.length()==0){
            Toast.makeText(this, "Required Field Email", Toast.LENGTH_SHORT).show();


        }else if (mPassword.length()==0){
            Toast.makeText(this, "Required Field Password", Toast.LENGTH_SHORT).show();


        }else if (mRePassword.length()==0){
            Toast.makeText(this, "Required Field Confirm Password", Toast.LENGTH_SHORT).show();

        }else {
            if (mPassword.equalsIgnoreCase(mRePassword)){
                Intent intent = new Intent(SignUp.this,SignUpSecondPage.class);
                intent.putExtra("mEmail",mEmail);
                intent.putExtra("mPassword",mPassword);
                intent.putExtra("ed_RePassword",mRePassword);
                intent.putExtra("Social_Type","normal");
                sharedHelper.putKey(SignUp.this, "typeNormal", "normal");
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                SignUp.this.finish();
            }
            else {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
