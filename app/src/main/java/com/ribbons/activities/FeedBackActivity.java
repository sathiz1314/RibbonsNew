package com.ribbons.activities;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ribbons.R;

import java.util.ArrayList;

public class FeedBackActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppCompatSpinner spinnerFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle("FeedBack");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorGlobal));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                FeedBackActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        spinnerFeedback = (AppCompatSpinner) findViewById(R.id.spinnerFeedback);
        ArrayList<String> strings = new ArrayList<String>();

        strings.add("Select a brand");
        strings.add("Cafe Coffee Day");
        strings.add("Hotel Buhari");
        strings.add("The Park Hotel");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FeedBackActivity.this,android.R.layout.simple_list_item_1,strings);
        spinnerFeedback.setAdapter(adapter);

    }
}
