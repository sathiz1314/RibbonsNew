package com.ribbons.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.activities.RibbonDetailsActivity;
import com.ribbons.modeldatas.HomeModelData;
import com.ribbons.modeldatas.Sliderribbon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathishDevendran on 2/21/17.
 */

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    private List<Sliderribbon> sliderribbons = new ArrayList<Sliderribbon>();

    public MyPagerAdapter(Context  context, ArrayList<HomeModelData> modelDatasList) {
        this.context = context;
        this.modelDatasList=modelDatasList;

    }

    public MyPagerAdapter(Context context, List<Sliderribbon> sliderribbons) {
        this.context = context;
        this.sliderribbons = sliderribbons;
    }

    @Override
    public int getCount() {
        return sliderribbons.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.row,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imagaeView);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        View viewsImage = (View) view.findViewById(R.id.viewsImage);
//        HomeModelData modelData = modelDatasList.get(position);
        final Sliderribbon sliderribbon = sliderribbons.get(position);
        if (sliderribbons.size()==0){
            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();

        }
        Glide.with(context).load(sliderribbon.getBrandlogo()).into(imageView);
        viewsImage.setBackgroundResource(R.drawable.transparent);
        imageView.setAlpha(.50f);
        tvTitle.setText(sliderribbon.getOutletname());
        tvPrice.setText("₹"+" "+ sliderribbon.getPointsrequired());
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(sliderribbon.getId());
                Intent intent = new Intent(context, RibbonDetailsActivity.class);
                intent.putExtra("getId",id);
                context.startActivity(intent);
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
