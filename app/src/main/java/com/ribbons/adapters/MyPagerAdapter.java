package com.ribbons.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ribbons.R;
import com.ribbons.modeldatas.HomeModelData;

import java.util.ArrayList;

/**
 * Created by sathishDevendran on 2/21/17.
 */

public class MyPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();

    public MyPagerAdapter(Context  context, ArrayList<HomeModelData> modelDatasList) {
        this.context = context;
        this.modelDatasList=modelDatasList;

    }

    @Override
    public int getCount() {
        return modelDatasList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.row,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imagaeView);
        HomeModelData modelData = modelDatasList.get(position);
        if (modelDatasList.size()==0){
            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();

        }
        Glide.with(context).load(modelData.getImage()).into(imageView);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
