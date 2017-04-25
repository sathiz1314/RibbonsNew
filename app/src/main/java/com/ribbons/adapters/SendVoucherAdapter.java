package com.ribbons.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.activities.SendVoucherDetails;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Myribbon;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 24-Mar-17.
 */

public class SendVoucherAdapter extends RecyclerView.Adapter<SendVoucherAdapter.ViewHolder> {

    private Context context;

    private List<Myribbon> myribbonsList = new ArrayList<Myribbon>();

    public SendVoucherAdapter(SendVoucherDetails context, List<Myribbon> myribbons) {
        this.context = context;
        this.myribbonsList = myribbons;
    }

    @Override
    public SendVoucherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sendvoucher,parent,false);
        ViewHolder viewHolder = new ViewHolder(view,parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SendVoucherAdapter.ViewHolder holder, int position) {

        final Myribbon myribbon = myribbonsList.get(position);


        holder.tvTitle.setText(myribbon.getBrandname());
        holder.tvPrice.setText(myribbon.getPointsrequired());
        Glide.with(context).load(myribbon.getBrandbanner()).into(holder.imageView);
        holder.viewsImage.setBackgroundResource(R.drawable.transparent);
        holder.imageView.setAlpha(.50f);

        if (myribbon.isSelected()){
            holder.tvViewOffer.setChecked(true);
        }else {
            holder.tvViewOffer.setChecked(false);
        }
        holder.tvViewOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<myribbonsList.size();i++){
                    myribbonsList.get(i).setSelected(false);
                }
                if (myribbon.isSelected()){
                    myribbon.setSelected(false);
                }else {
                    myribbon.setSelected(true);
                    SendVoucherDetails.rId = String.valueOf(myribbon.getId());
                }
                notifyDataSetChanged();
            }
        });
       /* holder.tvViewOffer.setChecked(myribbon.isSelected());
        holder.tvViewOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    myribbon.setSelected(true);
                    Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show();
                }else {
                    myribbon.setSelected(false);
                }
            }
        });
        holder.tvViewOffer.setChecked(myribbon.isSelected());*/
    }

    @Override
    public int getItemCount() {
        return myribbonsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvPrice,tvTitle;
        private ImageView imageView;
        private ViewGroup viewGroup;
        private CheckBox tvViewOffer;
        private View viewsImage;
        public ViewHolder(View itemView, ViewGroup parent) {
            super(itemView);
            this.viewGroup = parent;
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            tvViewOffer = (CheckBox) itemView.findViewById(R.id.tvViewOffer);
            viewsImage = (View) itemView.findViewById(R.id.viewsImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Myribbon myribbon = myribbonsList.get(getAdapterPosition());

        }
    }
}
