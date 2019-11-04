package com.example.toursapp2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toursapp2.Model.CityModel;
import com.example.toursapp2.Model.PartnerItemModel;
import com.example.toursapp2.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<CityModel> cityModelList;

    public CityAdapter(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list, parent, false);

        return new CityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CityModel cityModel = cityModelList.get(position);
        holder.txtcity.setText(cityModel.getCityname());
    }

    @Override
    public int getItemCount() {
        return cityModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtcity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            txtcity= itemView.findViewById(R.id.text_city);
        }
    }
}
