package com.ribbons.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.ribbons.R;
import com.ribbons.helper.DialogCaller;



public class SignUp extends AppCompatActivity {

    private AppCompatEditText ed_Email,ed_Password,ed_RePassword;
    private String mEmail,mPassword,mRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ed_Email = (AppCompatEditText) findViewById(R.id.ed_Email);
        ed_Password = (AppCompatEditText) findViewById(R.id.ed_Password);
        ed_RePassword = (AppCompatEditText) findViewById(R.id.ed_RePassword);
    }

    public void next(View view) {
        mEmail = ed_Email.getText().toString().trim();
        mPassword = ed_Password.getText().toString().trim();
        mRePassword = ed_RePassword.getText().toString().trim();

        if (mEmail.length()==0&& mPassword.length()==0&&mRePassword.length()==0){
            DialogCaller.showDialog(SignUp.this,"Ribbons","All fields are mandatory",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (mEmail.length()==0){
            DialogCaller.showDialog(SignUp.this,"Ribbons","Required Field",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }else if (mPassword.length()==0){
            DialogCaller.showDialog(SignUp.this,"Ribbons","Required Field",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }else if (mRePassword.length()==0){
            DialogCaller.showDialog(SignUp.this,"Ribbons","Required Field",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }else {
            startActivity(new Intent(SignUp.this,SignUpSecondPage.class));
        }

    }
}
