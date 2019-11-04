package com.example.toursapp2.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.toursapp2.Model.PartnerItemModel;
import com.example.toursapp2.R;
import com.example.toursapp2.Views.PartnerDetails;

import java.util.List;


public class PartnerItemAdapter extends RecyclerView.Adapter<PartnerItemAdapter.ViewHolder> {


    private Context mContext;
    private List<PartnerItemModel> partnerItemModelList;

    public PartnerItemAdapter(Context mContext, List<PartnerItemModel> partnerItemModelList) {
        this.mContext = mContext;
        this.partnerItemModelList = partnerItemModelList;

    }

    @NonNull
    @Override
    public PartnerItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_partner_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PartnerItemAdapter.ViewHolder holder, int position) {

        PartnerItemModel partnerItemModel = partnerItemModelList.get(position);




        holder.name.setText(partnerItemModel.getName());
        String rat= String.valueOf(partnerItemModel.getRating());
        holder.rating.setText(rat);
        String ratno= String.valueOf(partnerItemModel.getRatingcount());
        holder.noofrating.setText("("+ratno+" ratings)");
        holder.address.setText(partnerItemModel.getAddress());
        String img= partnerItemModelList.get(position).getIcon();
        holder.setIcon(img);
        String off= String.valueOf(partnerItemModelList.get(position).getOfferpercent());
        holder.offer.setText("Pay restaurant using mPAY and get "+off+"% instant discount.");
        holder.partid.setText(partnerItemModel.getId());


        holder.partnerdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, PartnerDetails.class);
                intent.putExtra("partnername",holder.name.getText());
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return partnerItemModelList.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder
{
    public TextView name,rating,noofrating,address,offer,heart,partid;
    public LinearLayout partnerdetails;
    public ImageView icon;



    public ViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.txt_partner_name);
        rating = (TextView) view.findViewById(R.id.txt_user_rating);
        noofrating = (TextView) view.findViewById(R.id.txt_user_rating_count);
        address=view.findViewById(R.id.txt_parrner_address);
        offer=view.findViewById(R.id.txt_partner_offer);
        icon=view.findViewById(R.id.partner_image);
        partnerdetails =view.findViewById(R.id.linear_partnerdetails);
        heart=view.findViewById(R.id.txt_heart);
        partid=view.findViewById(R.id.txt_partid);



    }
    private void  setIcon(String iconUrl)
    {

        Glide.with(mContext)
                .load(iconUrl)
                .into(icon);
    }


}



}
