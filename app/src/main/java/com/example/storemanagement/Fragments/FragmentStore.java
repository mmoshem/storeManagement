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

    private ArrayList<ProductModel> dataSet; // Declare dataset as a class-level variable

    public FragmentStore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        setTextInStore(view);

        // Initialize the dataset
        dataSet = new ArrayList<>();
        for (int i = 0; i < StoreData.ProductArray.length; i++) {
            dataSet.add(new ProductModel(
                    StoreData.drawableArray[i],
                    StoreData.ProductArray[i],
                    StoreData.priceArray[i],
                    0,  // Initial count
                    StoreData.id_Array[i]
            ));
        }

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.resView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Set up adapter and attach to RecyclerView
        CustomeAdapter adapter = new CustomeAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        // Handle search functionality
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterDataset(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle button click to save cart and navigate
        Button bt = view.findViewById(R.id.buttonCart);
        bt.setText("Add and Move to Cart"); // Update button text
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");

                    ArrayList<ProductModel> cartItems = new ArrayList<>();
                    for (ProductModel product : dataSet) {
                        if (product.getM_countItem() > 0) {
                            String s = String.valueOf(product.getM_id());
//                            cartRef.child(s).setValue(cartItems);
                           cartItems.add(product);
                        }
                    }

                    cartRef.setValue(cartItems).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Navigation.findNavController(view).navigate(R.id.action_fragmentStore_to_cartFragment);
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
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("name");

            // Read user name from the database and set greeting text
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
                    // Failed to read value
                }
            });
        }
    }
}
