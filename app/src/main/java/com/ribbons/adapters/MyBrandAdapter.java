package com.ribbons.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.activities.BrandDetailsActivity;
import com.ribbons.activities.RibbonDetailsActivity;
import com.ribbons.modeldatas.Brand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07-Apr-17.
 */

public class MyBrandAdapter extends RecyclerView.Adapter<MyBrandAdapter.ViewHolder> {

    private Context context;
    private List<Brand> brandList = new ArrayList<Brand>();

    public MyBrandAdapter(Context context, List<Brand> brands) {
        this.context = context;
        this.brandList = brands;
    }

    @Override
    public MyBrandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brandrow,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyBrandAdapter.ViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        Glide.with(context).load(brand.getBrandbanner()).into(holder.imagaeView);
        holder.tvTitle.setText(brand.getBrandname());
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagaeView;
        private TextView  tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imagaeView = (ImageView) itemView.findViewById(R.id.imagaeView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            Brand brand = brandList.get(getAdapterPosition());
            String id = String.valueOf(brand.getId());
            Intent intent = new Intent(context, BrandDetailsActivity.class);
            intent.putExtra("getId",id);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }
    }
}
