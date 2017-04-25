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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.activities.RibbonDetailsActivity;
import com.ribbons.helper.SharedHelper;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Ribbonobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04-Mar-17.
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private List<Ribbonobject> ribbonobjectsList = new ArrayList<Ribbonobject>();
    private SharedHelper sharedHelper;

    public HomePageAdapter(Context context, ArrayList<HomeModelData> modelDatasList) {
        this.context = context;
        this.modelDatasList = modelDatasList;
    }

    public HomePageAdapter(Context context, List<Ribbonobject> ribbonobjects) {
        this.context = context;
        this.ribbonobjectsList = ribbonobjects;
        sharedHelper = new SharedHelper(context);
    }

    @Override
    public HomePageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomePageAdapter.ViewHolder holder, int position) {
      //  HomeModelData homeModelData = modelDatasList.get(position);
        Ribbonobject ribbonobject = ribbonobjectsList.get(position);
        Glide.with(context).load(ribbonobject.getBrandlogo()).into(holder.imagaeView);
        holder.tvPrice.setText("₹"+" "+ ribbonobject.getPointsrequired());
        holder.tvTitle.setText(ribbonobject.getOutletname());
        holder.viewsImage.setBackgroundResource(R.drawable.transparent);
        holder.imagaeView.setAlpha(.50f);
       // Toast.makeText(context, ribbonobject.getId()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return ribbonobjectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imagaeView;
        private TextView tvPrice,tvTitle;
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
            Ribbonobject ribbonobject = ribbonobjectsList.get(getAdapterPosition());
            String id = String.valueOf(ribbonobject.getId());
          //  sharedHelper.putKey(context,"getId",id);
          //  Toast.makeText(context, ribbonobject.getId()+"", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, RibbonDetailsActivity.class);
            intent.putExtra("getId",id);
            context.startActivity(intent);
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }
}
