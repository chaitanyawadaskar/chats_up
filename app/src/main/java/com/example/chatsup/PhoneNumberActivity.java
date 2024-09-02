package com.example.chatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatsup.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        mAuth =FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Verifying your phone number");
        dialog.setMessage("Wait a few second");

        if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(PhoneNumberActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(PhoneNumberActivity.this,OTPActivity.class);
//                intent.putExtra("phoneNumber",binding.phoneBox.getText().toString());
//                startActivity(intent);
                dialog.show();
                
                if (binding.phoneBox.getText().toString().trim().isEmpty()){
                    Toast.makeText(PhoneNumberActivity.this, "Please enter your number", Toast.LENGTH_SHORT).show();
                }
                else if (binding.phoneBox.getText().toString().trim().length() != 10){
                    Toast.makeText(PhoneNumberActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
                else {
                    otpSend();
                }

            }
        });
    }

    private void otpSend() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                dialog.dismiss();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                dialog.dismiss();
                Toast.makeText(PhoneNumberActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                dialog.dismiss();

                Toast.makeText(PhoneNumberActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PhoneNumberActivity.this,OTPActivity.class);
                intent.putExtra("phoneNumber",binding.phoneBox.getText().toString());
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+binding.phoneBox.getText().toString().trim())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}