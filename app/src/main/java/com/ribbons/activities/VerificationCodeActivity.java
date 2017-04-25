package com.ribbons.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.ribbons.R;


public class VerificationCodeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatEditText ed_code1,ed_code2,ed_code3,ed_code4;
    private Button fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(null);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        ed_code1 = (AppCompatEditText) findViewById(R.id.ed_code1);
        ed_code2 = (AppCompatEditText) findViewById(R.id.ed_code2);
        ed_code3 = (AppCompatEditText) findViewById(R.id.ed_code3);
        ed_code4 = (AppCompatEditText) findViewById(R.id.ed_code4);
        fab = (Button) findViewById(R.id.fab);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                VerificationCodeActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
});
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(1);
        ed_code1.setFilters(FilterArray);
        ed_code2.setFilters(FilterArray);
        ed_code3.setFilters(FilterArray);
        ed_code4.setFilters(FilterArray);
        ed_code1.addTextChangedListener(new MyTextWatcher());
        ed_code2.addTextChangedListener(textWatcherCode2);
        ed_code3.addTextChangedListener(textWatcherCode3);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VerificationCodeActivity.this,MainActivity.class));
                finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

    }

    public class MyTextWatcher implements TextWatcher{

        public MyTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length()==1){
                ed_code2.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
   TextWatcher textWatcherCode2 = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

       }

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
           if (charSequence.length()==1){
               ed_code3.requestFocus();
           }
       }

       @Override
       public void afterTextChanged(Editable editable) {

       }
   };

   TextWatcher textWatcherCode3 = new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

       }

       @Override
       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           if (charSequence.length()==1){
               ed_code4.requestFocus();
           }
       }

       @Override
       public void afterTextChanged(Editable editable) {

       }
   };


}
