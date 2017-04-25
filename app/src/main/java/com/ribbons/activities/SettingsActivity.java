package com.ribbons.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.ribbons.R;


public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout linearAcc,linearChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearAcc = (LinearLayout) findViewById(R.id.linearAcc);
        linearChangePass = (LinearLayout) findViewById(R.id.linearChangePass);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle(R.string.action_settings);
        toolbar.setTitleTextColor(getResources().getColor(R.color.home_menuColor));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                SettingsActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

            }
        });
        linearAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ProfileActivity.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
        linearChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ChangePassword.class));
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });
    }
}
