package com.example.toursapp2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.toursapp2.Model.HorizontalProductScrollModel;
import com.example.toursapp2.R;
import com.example.toursapp2.Views.PartnerList;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;
    private Context mContext;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList,Context context) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        mContext=context;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertview,final ViewGroup parent) {
        View view;
        if(convertview==null)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
            view.setBackgroundColor(Color.parseColor("#ffffff"));



                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final long catid=horizontalProductScrollModelList.get(i).getCatID();
                            Intent intent=new Intent(parent.getContext(), PartnerList.class);
                            intent.putExtra("catid",catid);
                            parent.getContext().startActivity(intent);
                        }
                    });



            ImageView prodimg=view.findViewById(R.id.cat_prod_image);
            TextView prodtxt=view.findViewById(R.id.cat_prod_name);


          Glide.with(mContext)
                    .load(horizontalProductScrollModelList.get(i).getCatprodimage())
                    .into(prodimg);





            prodtxt.setText(horizontalProductScrollModelList.get(i).getCatprodname());
        }else{
            view=convertview;
        }


        return view;
    }
}
