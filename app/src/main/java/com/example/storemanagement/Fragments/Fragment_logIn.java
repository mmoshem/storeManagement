package com.example.storemanagement.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton$InspectionCompanion;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.storemanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_logIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_logIn extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;

    public Fragment_logIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_logIn.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_logIn newInstance(String param1, String param2) {
        Fragment_logIn fragment = new Fragment_logIn();
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
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        Button buttonLogIn = view.findViewById(R.id.buttonLogIn);

        buttonLogIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String emailStr = ((TextView)view.findViewById(R.id.editTextTextEmailAddressLogIn)).getText().toString();//can do it this way
                TextView passwordLogin = view.findViewById(R.id.editTextTextPasswordLogIn);// or can separate in two **
//                String emailStr = emailAddressLogin.getText().toString();
                String passwordStr = passwordLogin.getText().toString();// ** the second part

                if(!emailStr.isBlank()&&!passwordStr.isBlank()) {
                    login(emailStr, passwordStr);
                }
                else{
                    Toast.makeText(getContext(),"Must fill password and email",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonReg = view.findViewById(R.id.buttonReg);

        buttonReg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Navigation.findNavController(view).navigate(R.id.action_fragment_logIn_to_fragment_reg);
            }
        });

        return view;
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (getView() != null) {
                                Toast.makeText(getContext(),"login success",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).navigate(R.id.action_fragment_logIn_to_fragmentStore);
                            } else {
                                Toast.makeText(getContext(), "Navigation failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "can't connect check if you filled correctly the info ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}