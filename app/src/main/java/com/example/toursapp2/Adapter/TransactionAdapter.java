package com.example.toursapp2.Adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.toursapp2.Model.TransactionsModel;
import com.example.toursapp2.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<TransactionsModel> transactionsModelList;

    public TransactionAdapter(List<TransactionsModel> transactionsModelList) {
        this.transactionsModelList = transactionsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_details_layout,parent,false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String date=transactionsModelList.get(position).getDate();
        String name=transactionsModelList.get(position).getProdname();
        String amount=transactionsModelList.get(position).getAmount();
        holder.setData(name,date,amount);


        if(position %2 == 1)
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#e1e1f0"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }


    }

    @Override
    public int getItemCount() {
        return transactionsModelList.size();    }

    public class ViewHolder extends RecyclerView.ViewHolder{

            private TextView txtprodname;
            private TextView txtamount;
            private TextView txtdate;

        public ViewHolder(View itemView) {
                super(itemView);
                txtprodname=itemView.findViewById(R.id.txt_trans_name);
                txtamount=itemView.findViewById(R.id.txt_trans_amount);
                txtdate=itemView.findViewById(R.id.txt_trans_date);
            }
            private void setData(String name, String date, String amount){
                txtdate.setText("Time: "+date.substring(0,5)+" Date: "+date.substring(9));
                txtprodname.setText(name);
                txtamount.setText("- ₹"+amount);

            }
        }
    }

