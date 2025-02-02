package com.example.storemanagement.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storemanagement.CustomeAdapter;
import com.example.storemanagement.ProductModel;
import com.example.storemanagement.R;
import com.example.storemanagement.StoreData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentStore extends Fragment {

    private ArrayList<ProductModel> dataSet; // Store products displayed in the store

    public FragmentStore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        setTextInStore(view);

        // Initialize the dataset using your StoreData arrays
        dataSet = new ArrayList<>();
        for (int i = 0; i < StoreData.ProductArray.length; i++) {
            dataSet.add(new ProductModel(
                    StoreData.drawableArray[i],
                    StoreData.ProductArray[i],
                    StoreData.priceArray[i],
                    0,  // Initial count is 0
                    StoreData.id_Array[i]
            ));
        }

        // Set up RecyclerView and its adapter
        RecyclerView recyclerView = view.findViewById(R.id.resView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CustomeAdapter adapter = new CustomeAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        // Implement search functionality
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterDataset(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        // Handle "Add and Move to Cart" button click
        Button bt = view.findViewById(R.id.buttonCart);
//        bt.setText("Add and Move to Cart");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    DatabaseReference cartRef = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(userId)
                            .child("cart");

                    // Read the current cart from Firebase to merge with new items
                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Loop through each product in the store that has a count > 0
                            for (ProductModel product : dataSet) {
                                if (product.getM_countItem() > 0) {
                                    String productId = String.valueOf(product.getM_id());
                                    // If the product is already in the cart, merge the quantities
                                    if (snapshot.hasChild(productId)) {
                                        ProductModel existingProduct = snapshot.child(productId).getValue(ProductModel.class);
                                        if (existingProduct != null) {
                                            int newCount = existingProduct.getM_countItem() + product.getM_countItem();
                                            product.setM_countItem(newCount);
                                        }
                                    }
                                    // Update or add the product under its unique productId
                                    cartRef.child(productId).setValue(product);
                                }
                            }
                            // After merging and updating, navigate to the cart fragment
                            Navigation.findNavController(view).navigate(R.id.action_fragmentStore_to_cartFragment);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Optionally handle errors here
                        }
                    });
                }
            }
        });

        return view;
    }

    public void setTextInStore(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference myRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("name");

            // Listen for name changes and update the greeting
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    TextView temp = view.findViewById(R.id.textViewname);
                    String newstring = "Hi " + value;
                    temp.setText(newstring);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error if needed
                }
            });
        }
    }
}
