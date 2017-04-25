package com.ribbons.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.activities.BrandDetailsActivity;
import com.ribbons.activities.HomeSearch;
import com.ribbons.modeldatas.HomeSearchModel;
import com.ribbons.modeldatas.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 07-Mar-17.
 */

public class HomeSearchAdapter extends RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<HomeSearchModel> filterList = new ArrayList<HomeSearchModel>();
    private ArrayList<HomeSearchModel> homeSearchList = new ArrayList<HomeSearchModel>();
    private List<Result> resultsList = Collections.emptyList();
    public HomeSearchAdapter(Context context, ArrayList<HomeSearchModel> homeSearchList) {
        this.context = context;
        this.homeSearchList = homeSearchList;
        this.filterList = new ArrayList<HomeSearchModel>();
        this.filterList.addAll(this.homeSearchList);
    }

    public HomeSearchAdapter(HomeSearch context, List<Result> results) {
        this.context = context;
        this.resultsList = results;

    }

    @Override
    public HomeSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeSearchAdapter.ViewHolder holder, int position) {
       // HomeSearchModel homeSearchModel = filterList.get(position);
        Result result = resultsList.get(position);

        holder.tv_BrandName.setText(result.getBrandname());
        holder.tv_BrandType.setText("in" + " "+ result.getBrandtype());
    }

    @Override
    public int getItemCount() {

       // return (null != filterList ? filterList.size() : 0);
        return resultsList.size();
    }

    public void filter(String newText) {
        filterList.clear();

        if (TextUtils.isEmpty(newText)) {
            filterList.addAll(homeSearchList);

        } else {
            for (HomeSearchModel item : homeSearchList) {
                if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                    filterList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_HomeSearch,tv_BrandName,tv_BrandType;
        private ImageView iv_HomeSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_BrandName = (TextView) itemView.findViewById(R.id.tv_BrandName);
            tv_BrandType = (TextView) itemView.findViewById(R.id.tv_BrandType);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Result result = resultsList.get(getAdapterPosition());
            String id = String.valueOf(result.getId());
            Intent intent = new Intent(context, BrandDetailsActivity.class);
            intent.putExtra("getId",id);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }
}
