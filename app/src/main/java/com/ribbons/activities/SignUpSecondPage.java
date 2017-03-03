package com.ribbons.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.ribbons.R;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SignUpSecondPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AppCompatEditText ed_FirstName, ed_LastName, ed_phone, ed_location, ed_Dob;
    private TextView tv_Pin1, tv_Pin2, tv_Pin3, tv_Pin4, tv_Pin5, tv_Pin6, tv_Pin7, tv_Pin8,txt;
    private AppCompatTextView tv_Dob;
    private RadioGroup rg_Button;
    private RadioButton rb_Male, rb_Female;
    private Calendar calendar,cal1;
    private DateFormat dateFormat;
    private AppCompatSpinner spinner_Location;
    private String mFirstName, mLastName, mPhone, mDob, mLocation, mGender;
    private TextWatcher textWatcherPin1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_second_page);

        calendar = Calendar.getInstance();
        cal1 = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -17);
        calendar.getTimeInMillis();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
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
        txt = (TextView) findViewById(R.id.txt);
        spinner_Location = (AppCompatSpinner) findViewById(R.id.spinner_Location);
        rg_Button = (RadioGroup) findViewById(R.id.rg_Button);
        rb_Male = (RadioButton) findViewById(R.id.rb_Male);
        rb_Female = (RadioButton) findViewById(R.id.rb_Female);
        ed_FirstName.addTextChangedListener(textWatcherFirstName);
        ed_LastName.addTextChangedListener(textWatcherLastName);
        dateOperation();
        spinnerOperation();
        rGOperation();

    }
    private void rGOperation() {
        rg_Button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rb_Male.isChecked()) {
                    mGender = "Male";
                } else if (rb_Female.isChecked()) {
                    mGender = "Female";
                }
            }
        });

    }

    private void spinnerOperation() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Royapettah");
        list.add("Triplicane");
        list.add("Mylapore");
        list.add("Kodambakkam");
        list.add("Nungambakkam");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpSecondPage.this, R.layout.spinner_item, list);
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

    private void dateOperation() {
        tv_Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(SignUpSecondPage.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //  datePickerDialog.setMaxDate(calendar);
                datePickerDialog.show(getFragmentManager(), "datePicker");

            }
        });
    }

    public void next(View view) {
        mFirstName = ed_FirstName.getText().toString().trim();
        mLastName = ed_LastName.getText().toString().trim();
        mPhone = ed_phone.getText().toString().trim();
        mDob = tv_Dob.getText().toString().trim();
        mGender = tv_Dob.getText().toString().trim();
        startActivity(new Intent(SignUpSecondPage.this, VerificationCodeActivity.class));
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        tv_Dob.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
    }

    TextWatcher textWatcherFirstName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                tv_Pin1.setText(charSequence.toString().substring(0,1));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    TextWatcher textWatcherLastName = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                tv_Pin2.setText(charSequence.toString().substring(0,1));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
}
