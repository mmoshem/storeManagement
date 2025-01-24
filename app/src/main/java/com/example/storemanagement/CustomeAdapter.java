package com.example.storemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder>  {


    private ArrayList<ProductModel> dataSet;
    private ArrayList<ProductModel> filterdDataset;

    public CustomeAdapter(ArrayList<ProductModel> dataSet) {
        this.dataSet = dataSet;
        this.filterdDataset = new ArrayList<>(dataSet);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView pricePerPiece;
        TextView itemCount;
        ImageView imageView;
        Button btAdd;
        Button btRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tv_product_name);
            pricePerPiece = itemView.findViewById(R.id.tv_price_per_piece);
            itemCount = itemView.findViewById(R.id.tv_item_count);
            imageView = itemView.findViewById(R.id.img_product);
            btAdd =  itemView.findViewById(R.id.btAdd);
            btRemove =  itemView.findViewById(R.id.btRemove);
        }
    }

    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrow, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductModel product = filterdDataset.get(position);
        holder.productName.setText(product.getM_name());
        holder.pricePerPiece.setText(product.getM_price());
        holder.itemCount.setText(String.valueOf(product.getM_countItem()));
        holder.imageView.setImageResource(product.getM_image());

        holder.btAdd.setOnClickListener(v -> {
            int count = product.getM_countItem() + 1;
            product.setM_countItem(count);
            holder.itemCount.setText(String.valueOf(count));
            notifyItemChanged(position);
        });

        holder.btRemove.setOnClickListener(v -> {
            int count = product.getM_countItem();
            if (count > 0) {
                product.setM_countItem(count - 1);
                holder.itemCount.setText(String.valueOf(count - 1));
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterdDataset.size(); // Return the size of the filtered dataset

    }public void filterDataset(String query) {
        filterdDataset.clear(); // Clear the current filtered dataset

        if (query.isEmpty()) {
            filterdDataset.addAll(dataSet); // If the query is empty, show all data
        } else {
            for (ProductModel product : dataSet) {
                if (product.getM_name().toLowerCase().startsWith(query.toLowerCase())) {
                    filterdDataset.add(product); // Add matching items
                }
            }
        }

        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

}
