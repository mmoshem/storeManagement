package com.example.storemanagement.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.storemanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_reg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_reg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_reg.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_reg newInstance(String param1, String param2) {
        Fragment_reg fragment = new Fragment_reg();
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
        View view =  inflater.inflate(R.layout.fragment_reg, container, false);

        Button authanticateAndRegButton = view.findViewById(R.id.buttonAuthenticateAndRegister);

        authanticateAndRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextEmailBox = view.findViewById(R.id.editTextTextEmailAddressReg);
                EditText editTextPhoneNum = view.findViewById(R.id.editTextPhoneReg);
                EditText editTextPassword = view.findViewById(R.id.editTextTextPasswordReg);
                EditText editTextRePassword = view.findViewById(R.id.editTextTextPasswordReEnterReg);

                if(isInfoGoodToReg(editTextEmailBox,editTextPhoneNum,editTextPassword,editTextRePassword)){
                    String passwordStr = editTextPassword.getText().toString();
                    String emailStr = editTextEmailBox.getText().toString();
                    registerToFireBase(view ,emailStr, passwordStr);
                }


            }
        });

        return view;
    }
    public boolean isInfoGoodToReg(EditText emailView, EditText phoneNumView, EditText PasswordView, EditText RePasswordView){
        String emailStr = emailView.getText().toString();
        String phoneNumString = phoneNumView.getText().toString();
        String passwordStr = PasswordView.getText().toString();
        String rePasswordString = RePasswordView.getText().toString();


        if(!emailStr.isBlank()&&!passwordStr.isBlank()&&
                !phoneNumString.isBlank()&&!rePasswordString.isBlank()){
            if(passwordStr.equals(rePasswordString)) {
                if(passwordStr.length()>=6) {
                    return true;
                }
                else{
                    Toast.makeText(getContext(), "password should be longer than 5", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                RePasswordView.setText("");
                Toast.makeText(getContext(), "password should match", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getContext(), "fill all fields", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public void registerToFireBase(View view , String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "registration successful", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_fragment_reg_to_fragment_logIn);

                        } else {
                            Toast.makeText(getContext(), "registration filed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}