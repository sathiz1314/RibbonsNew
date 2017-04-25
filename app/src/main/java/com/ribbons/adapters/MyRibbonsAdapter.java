package com.ribbons.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Myribbon;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 07-Mar-17.
 */

public class MyRibbonsAdapter extends RecyclerView.Adapter<MyRibbonsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private List<Myribbon> myribbonList = new ArrayList<Myribbon>();

    public MyRibbonsAdapter(Context context, ArrayList<HomeModelData> modelDatasList) {
        this.context = context;
        this.modelDatasList=modelDatasList;
    }

    public MyRibbonsAdapter(Context context, List<Myribbon> myribbons) {

        this.myribbonList = myribbons;
        this.context = context;

    }

    @Override
    public MyRibbonsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRibbonsAdapter.ViewHolder holder, int position) {
       // HomeModelData homeModelData = modelDatasList.get(position);
        Myribbon myribbon = myribbonList.get(position);
        Glide.with(context).load(myribbon.getBrandbanner()).into(holder.imagaeView);
        holder.tvPrice.setText(myribbon.getPointsrequired());
        holder.tvTitle.setText(myribbon.getBrandname());

    }

    @Override
    public int getItemCount() {
        return myribbonList.size();
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
