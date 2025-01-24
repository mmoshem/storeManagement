package com.example.storemanagement.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.storemanagement.CustomeAdapter;
import com.example.storemanagement.ProductModel;
import com.example.storemanagement.R;
import com.example.storemanagement.StoreData;
import java.util.ArrayList;

public class FragmentStore extends Fragment {

    private ArrayList<ProductModel> dataSet;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public FragmentStore() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        // Initialize RecyclerView and other elements
        recyclerView = view.findViewById(R.id.resView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Populate the dataset for RecyclerView
        dataSet = new ArrayList<>();
        for (int i = 0; i < StoreData.ProductArray.length; i++) {
            dataSet.add(new ProductModel(
                    StoreData.drawableArray[i],
                    StoreData.ProductArray[i],
                    StoreData.price[i],
                    0,
                    StoreData.id_[i]
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
}
