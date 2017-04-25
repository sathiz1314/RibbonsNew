package com.ribbons.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.ribbons.R;
import com.ribbons.helper.SharedHelper;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    SharedHelper sharedHelper;
    String log = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedHelper = new SharedHelper(SplashActivity.this);
        log = sharedHelper.getKey(SplashActivity.this, "login_type");

        Timer RunSplash = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                if (log.equalsIgnoreCase("Logged")) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    SplashActivity.this.finish();
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                else {
                    startActivity(new Intent(SplashActivity.this,SignIn.class));
                    SplashActivity.this.finish();
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }

                /*startActivity(new Intent(SplashActivity.this,SignIn.class));
                SplashActivity.this.finish();*/

            }
        };
        RunSplash.schedule(timerTask,2000);
    }
}
