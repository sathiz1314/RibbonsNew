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
import com.ribbons.modeldatas.BrandDetailsDataModel;
import com.ribbons.modeldatas.Ribbonslist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

/**
 * Created by User on 07-Apr-17.
 */

public class MyBrandDetailsAdapter extends RecyclerView.Adapter<MyBrandDetailsAdapter.ViewHolder> {

    private Context context;
    private List<Ribbonslist> ribbonslists = new ArrayList<Ribbonslist>();
    public MyBrandDetailsAdapter(Context brandDetailsActivity, List<Ribbonslist> ribbonslists) {
        this.context = brandDetailsActivity;
        this.ribbonslists = ribbonslists;
    }

    @Override
    public MyBrandDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyBrandDetailsAdapter.ViewHolder holder, int position) {
        Ribbonslist ribbonslist = ribbonslists.get(position);
        Glide.with(context).load(ribbonslist.getBrandbanner()).into(holder.imagaeView);
        holder.tvPrice.setText("₹"+" "+ ribbonslist.getPointsrequired());
        holder.tvTitle.setText(ribbonslist.getRibbontype());
        holder.imagaeView.setAlpha(.80f);
    }

    @Override
    public int getItemCount() {
        return ribbonslists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imagaeView;
        private TextView tvPrice,tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imagaeView = (ImageView) itemView.findViewById(R.id.imagaeView);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {

            Ribbonslist ribbonslist = ribbonslists.get(getAdapterPosition());
            String id = String.valueOf(ribbonslist.getId());
            Intent intent = new Intent(context, RibbonDetailsActivity.class);
            intent.putExtra("getId",id);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        }
    }
}
