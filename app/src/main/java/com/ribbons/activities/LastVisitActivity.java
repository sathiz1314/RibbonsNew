package com.ribbons.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ribbons.R;
import com.ribbons.adapters.LastVisitAdapter;
import com.ribbons.helper.ConnectivityReceiver;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.LastVisitDataModel;
import com.ribbons.modeldatas.Lastvisit;
import com.ribbons.retrohelper.APIService;
import com.ribbons.retrohelper.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastVisitActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLastVisit;
    private RelativeLayout relativeBg;
    private static final String TAG = "LastVisitActivity";
    private Toolbar toolbar;
    private boolean isConnected;
    private SharedHelper sharedHelper;
    private APIService apiService;
    private LastVisitAdapter lastVisitAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_visit);
        //
        sharedHelper = new SharedHelper(this);
        apiService = ApiUtils.getAPIService();
        isConnected = ConnectivityReceiver.isConnected();
        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_backwardarrowred);
        toolbar.setTitle(getString(R.string.last_visit));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorGlobal));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                LastVisitActivity.this.finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
        //
        String token = sharedHelper.getKey(this, "access_token");
        String accept = getString(R.string.accept);
        String type = getString(R.string.token_type);
        //
        recyclerViewLastVisit = (RecyclerView) findViewById(R.id.recyclerViewLastVisit);
        recyclerViewLastVisit.setHasFixedSize(true);
        recyclerViewLastVisit.setLayoutManager(new LinearLayoutManager(this));
        operation(accept,type,token);
    }

    private void operation(String accept, String type, String token) {

        if (isConnected){
            try {
                apiService.getLastVisit(accept,type+" "+token,"ribbon").enqueue(new Callback<LastVisitDataModel>() {
                    @Override
                    public void onResponse(Call<LastVisitDataModel> call, Response<LastVisitDataModel> response) {
                        Log.e(TAG,"onResponce Success --> " + response.isSuccessful());

                        try {
                            if (response.isSuccessful()){
                                if (response.body().getError().equalsIgnoreCase("false")){
                                    List<Lastvisit> lastvisitList = response.body().getLastvisits();
                                    lastVisitAdapter = new LastVisitAdapter(LastVisitActivity.this,lastvisitList);
                                    recyclerViewLastVisit.setAdapter(lastVisitAdapter);

                                }else {

                                }
                            }else {

                            }
                            lastVisitAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<LastVisitDataModel> call, Throwable t) {
                    Log.e(TAG, "onResponce Fail --> "+ t.getLocalizedMessage());
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(LastVisitActivity.this, getString(R.string.no_internet)+"", Toast.LENGTH_SHORT).show();
        }

    }
}
