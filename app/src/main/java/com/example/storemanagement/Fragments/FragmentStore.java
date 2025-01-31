package com.example.storemanagement.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

    public FragmentStore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        setTextInStore(view);

        // Initialize RecyclerView and other elements
        RecyclerView recyclerView = view.findViewById(R.id.resView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Populate the dataset for RecyclerView
        ArrayList<ProductModel> dataSet = new ArrayList<>();
        for (int i = 0; i < StoreData.ProductArray.length; i++) {
            dataSet.add(new ProductModel(
                    StoreData.drawableArray[i],
                    StoreData.ProductArray[i],
                    StoreData.priceArray[i],
                    0,
                    StoreData.id_Array[i]
            ));
        }

        // Set up the adapter
        CustomeAdapter adapter = new CustomeAdapter(dataSet);
        recyclerView.setAdapter(adapter);
        EditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filterDataset(s.toString()); // Call filter method
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return view;
    }


    public void setTextInStore(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("name");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                TextView temp =view.findViewById(R.id.textViewname);
                String newstring ="hi "+ value;
                temp.setText(newstring);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }
}
