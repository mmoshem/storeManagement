package com.example.storemanagement.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public cartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static cartFragment newInstance(String param1, String param2) {
        cartFragment fragment = new cartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize dataset and adapter
        ArrayList<ProductModel> cartData = new ArrayList<>();
        CustomeAdapter adapter = new CustomeAdapter(cartData);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");

            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    cartData.clear(); // Clear old data

                    // Iterate through the children in the "cart" list
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        ProductModel product = productSnapshot.getValue(ProductModel.class); // Deserialize
                        if (product != null) {
                            cartData.add(product); // Add product to dataset
                        }
                    }

                    adapter.notifyDataSetChanged(); // Notify adapter to refresh RecyclerView
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle database error
                    Toast.makeText(getContext(), "Failed to load cart data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }


}