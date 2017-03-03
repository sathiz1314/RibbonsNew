package com.ribbons.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ribbons.R;
import com.ribbons.adapters.MyPagerAdapter;
import com.ribbons.modeldatas.HomeModelData;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context context;
    private ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    ArrayList<HomeModelData> modelDatasList = new ArrayList<HomeModelData>();
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context=getActivity();
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator_unselected_background);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setPadding(60,0,60,0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(40);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int pageWidth = viewPager.getMeasuredWidth() -
                        viewPager.getPaddingLeft() - viewPager.getPaddingRight();
                int pageHeight = viewPager.getHeight();
                int paddingLeft = viewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() - (viewPager.getScrollX() + paddingLeft)) / pageWidth;
                int max = pageHeight / 10;

                if (transformPos< -1) {
                    page.setAlpha(1f);
                    page.setScaleY(0.8f);

                } else if (transformPos <= 1) {
                    page.setScaleY(1f);
                } else {
                    page.setAlpha(1f);
                    page.setScaleY(0.8f);
                }

            }


        });
        modelDatasList.clear();
        modelDatasList.add(new HomeModelData(R.drawable.coff));
        modelDatasList.add(new HomeModelData(R.drawable.coff));
        modelDatasList.add(new HomeModelData(R.drawable.coff));
        modelDatasList.add(new HomeModelData(R.drawable.coff));
        modelDatasList.add(new HomeModelData(R.drawable.coff));
        myPagerAdapter = new MyPagerAdapter(context,modelDatasList);
        viewPager.setAdapter(myPagerAdapter);
        indicator.setViewPager(viewPager);
        return view;
    }

}
