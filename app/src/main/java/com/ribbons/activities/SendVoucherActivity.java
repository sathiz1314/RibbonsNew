package com.ribbons.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ribbons.R;

public class SendVoucherActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button bt_SendVoucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_voucher);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bt_SendVoucher = (Button) findViewById(R.id.bt_SendVoucher);
        toolbar.setLogo(null);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                SendVoucherActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        bt_SendVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SendVoucherActivity.this,SendVoucherDetails.class));
                SendVoucherActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });
    }
}
