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
import com.ribbons.activities.RibbonDetailsActivity;
import com.ribbons.modeldatas.Ribbon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07-Apr-17.
 */

public class RibbonAdapter extends RecyclerView.Adapter<RibbonAdapter.ViewHolder> {

    private Context context;
    private List<Ribbon> ribbonList = new ArrayList<Ribbon>();

    public RibbonAdapter(Context context, List<Ribbon> ribbonList) {
        this.context = context;
        this.ribbonList = ribbonList;
    }

    @Override
    public RibbonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RibbonAdapter.ViewHolder holder, int position) {

        Ribbon ribbon = ribbonList.get(position);
        Glide.with(context).load(ribbon.getBrandlogo()).into(holder.imagaeView);
        holder.tvPrice.setText("₹"+" "+ ribbon.getPointsrequired());
        holder.tvTitle.setText(ribbon.getBrandname());
        holder.viewsImage.setBackgroundResource(R.drawable.transparent);
        holder.imagaeView.setAlpha(.50f);

    }

    @Override
    public int getItemCount() {
        return ribbonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagaeView;
        private TextView tvPrice, tvTitle;
        private View viewsImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imagaeView = (ImageView) itemView.findViewById(R.id.imagaeView);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            viewsImage = (View) itemView.findViewById(R.id.viewsImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Ribbon ribbon = ribbonList.get(getAdapterPosition());
            String id = String.valueOf(ribbon.getId());
            Intent intent = new Intent(context, RibbonDetailsActivity.class);
            intent.putExtra("getId",id);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }
    }
}
