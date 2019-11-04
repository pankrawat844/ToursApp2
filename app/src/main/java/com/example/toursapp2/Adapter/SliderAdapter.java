package com.example.toursapp2.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.toursapp2.Model.SliderModel;
import com.example.toursapp2.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private List<SliderModel> slidermodellist;

    public SliderAdapter(List<SliderModel> slidermodellist,Context context) {

        this.slidermodellist = slidermodellist;
        mContext=context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false);
        ConstraintLayout bannercontainer=view.findViewById(R.id.banner_container);
        ImageView banner=view.findViewById(R.id.banner_slide);

        Glide.with(mContext)
                .load(slidermodellist.get(position).getBanner())
                .into(banner);
        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return slidermodellist.size();
    }
}
