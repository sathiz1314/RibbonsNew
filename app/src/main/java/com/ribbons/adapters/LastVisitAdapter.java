package com.ribbons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ribbons.activities.LastVisitActivity;
import com.ribbons.R;
import com.ribbons.modeldatas.Lastvisit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21-Apr-17.
 */

public class LastVisitAdapter extends RecyclerView.Adapter<LastVisitAdapter.ViewHolder> {

    private Context context;
    private List<Lastvisit> lastvisitList = new ArrayList<Lastvisit>();
    public LastVisitAdapter(LastVisitActivity lastVisitActivity, List<Lastvisit> lastvisitList) {
        this.context = lastVisitActivity;
        this.lastvisitList = lastvisitList;
    }

    @Override
    public LastVisitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_visit_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LastVisitAdapter.ViewHolder holder, int position) {

        try {
            Lastvisit lastvisit = lastvisitList.get(position);
            holder.tvTitle.setText(lastvisit.getBrandname());
            String type = lastvisit.getType();
            if (type.equalsIgnoreCase("percentage")){
                holder.tvPrice.setText(lastvisit.getDiscount()+" "+"%");
            }else if (type.equalsIgnoreCase("cash")){
                holder.tvPrice.setText("₹"+" "+ lastvisit.getDiscount());
            }else if (type.equalsIgnoreCase(null)){
                holder.tvPrice.setText(lastvisit.getDiscount());
            }
            Glide.with(context).load(lastvisit.getBrandbanner()).into(holder.imagaeView);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return lastvisitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagaeView;
        private TextView tvPrice,tvTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            imagaeView = (ImageView) itemView.findViewById(R.id.imagaeView);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
