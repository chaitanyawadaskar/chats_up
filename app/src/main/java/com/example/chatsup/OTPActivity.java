package com.example.chatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.view.View;
import android.widget.Toast;

import com.example.chatsup.databinding.ActivityOtpactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

     ActivityOtpactivityBinding binding;
     private String verificatiobId;
     ProgressDialog dialog;
     private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth =  FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Checking your OTP");
        dialog.setMessage("Wait a few second");


        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.phoneLbl.setText("Verify "+phoneNumber);

        verificatiobId = getIntent().getStringExtra("verificationId");

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                if (binding.firstPinView.getText().toString().trim().isEmpty()){
                    Toast.makeText(OTPActivity.this, "Please fill the box", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (verificatiobId!=null){
                        String code = binding.firstPinView.getText().toString().trim();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificatiobId, code);
                           mAuth
                                .signInWithCredential(credential)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(OTPActivity.this,SetUpProfileActivity.class);
                                    startActivity(intent);
                                    finishAffinity(); //all activity finish


                                }else{
                                    dialog.dismiss();
                                    Toast.makeText(OTPActivity.this, "OTP is not valid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });






    }
}