package com.example.storemanagement.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storemanagement.CustomeAdapter;
import com.example.storemanagement.ProductModel;
import com.example.storemanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cartFragment extends Fragment {
    private ArrayList<ProductModel> cartData;
    private RecyclerView recyclerView;
    private CustomeAdapter adapter;
    private DatabaseReference cartRef;
    private TextView tvTotalItems; // Displays the total price
    private Button btnClearCart;   // The clear cart button

    public cartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (updated layout with clear button)
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Set up the RecyclerView and its adapter (using cart mode)
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartData = new ArrayList<>();
        adapter = new CustomeAdapter(cartData, true);
        recyclerView.setAdapter(adapter);

        // Initialize the summary TextView and clear button
        tvTotalItems = view.findViewById(R.id.tv_total_items);
        btnClearCart = view.findViewById(R.id.btn_clear_cart);

        // Optional: Set up search functionality
        EditText searchEditText = view.findViewById(R.id.searchEditText2);
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterDataset(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        // Set up the clear cart button click listener
        btnClearCart.setOnClickListener(v -> {
            // Remove all items from the cart by removing the entire node from Firebase.
            if (cartRef != null) {
                cartRef.removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Cart cleared", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to clear cart", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Load cart data from Firebase.
        fetchCartData();

        return view;
    }

    private void fetchCartData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            cartRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("cart");

            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    cartData.clear();
                    Log.d("CART_FRAGMENT", "DataSnapshot: " + snapshot.getValue());

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        ProductModel product = productSnapshot.getValue(ProductModel.class);
                        if (product != null) {
                            cartData.add(product);
                            Log.d("CART_FRAGMENT", "Product added: "
                                    + product.getM_name() + " - Count: " + product.getM_countItem());
                        }
                    }
                    adapter.filterDataset(""); // Update the filtered list
                    updateCartSummary();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("CART_FRAGMENT", "Failed to load cart data: " + error.getMessage());
                    Toast.makeText(getContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Calculate and display the total price of all items in the cart.
    private void updateCartSummary() {
        double totalPrice = 0.0;
        for (ProductModel p : cartData) {
            // Remove the currency symbol "₪" and any spaces
            String cleanedPrice = p.getM_price().replace("₪", "").trim();
            try {
                double price = Double.parseDouble(cleanedPrice);
                int count = p.getM_countItem();
                totalPrice += price * count;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Log.e("CART_FRAGMENT", "Failed to parse price: " + cleanedPrice);
            }
        }
        tvTotalItems.setText("Total Price: ₪" + String.format("%.2f", totalPrice));
    }
}
