package com.example.storemanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storemanagement.ProductModel;
import com.example.storemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.MyViewHolder> {

    private ArrayList<ProductModel> dataSet;
    private ArrayList<ProductModel> filterdDataset;
    // Flag to indicate if adapter is being used in the cart screen.
    private boolean isCart;

    // Constructor with isCart flag
    public CustomeAdapter(ArrayList<ProductModel> dataSet, boolean isCart) {
        this.dataSet = dataSet;
        this.filterdDataset = new ArrayList<>(dataSet);
        this.isCart = isCart;
    }

    // If you wish to use the adapter in store mode only, you can still call:
    public CustomeAdapter(ArrayList<ProductModel> dataSet) {
        this(dataSet, false);
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
            btAdd = itemView.findViewById(R.id.btAdd);
            btRemove = itemView.findViewById(R.id.btRemove);
        }
    }

    @NonNull
    @Override
    public CustomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Work on the filtered dataset
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
            // In cart mode, update Firebase immediately.
            if (isCart) {
                updateCartInFirebase(product);
            }
        });

        holder.btRemove.setOnClickListener(v -> {
            int count = product.getM_countItem();
            if (count > 0) {
                product.setM_countItem(count - 1);
                holder.itemCount.setText(String.valueOf(product.getM_countItem()));
                notifyItemChanged(position);
                if (isCart) {
                    updateCartInFirebase(product);
                }
            }
        });
    }

    // Push the updated product to Firebase under the user's cart node.
    private void updateCartInFirebase(ProductModel product) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("cart");

            if (product.getM_countItem() == 0) {
                // Remove the product if its count is 0
                cartRef.child(String.valueOf(product.getM_id())).removeValue();
            } else {
                // Otherwise, update the product in Firebase
                cartRef.child(String.valueOf(product.getM_id())).setValue(product);
            }
        }
    }


    @Override
    public int getItemCount() {
        return filterdDataset.size();
    }

    public void filterDataset(String query) {
        filterdDataset.clear();
        if (query.isEmpty()) {
            filterdDataset.addAll(dataSet);
        } else {
            for (ProductModel product : dataSet) {
                if (product.getM_name().toLowerCase().startsWith(query.toLowerCase())) {
                    filterdDataset.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
