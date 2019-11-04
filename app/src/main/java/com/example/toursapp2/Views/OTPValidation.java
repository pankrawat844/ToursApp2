package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPValidation extends AppCompatActivity {
    private Button btnverify;
    private EditText txtmobile,txtotp;
    TextView txtcode;
    FirebaseAuth auth;
    SharedPreferences sp;
    String code;
    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;
    String cust_id,mobile;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String Verification_code;
    private String TAG ="OtpVerification";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        sp = getSharedPreferences("otp",MODE_PRIVATE);

        if(sp.getBoolean("logged",false)) {
            LoadLocation();
        }
        else{
            sp.getBoolean("logged",false);
        }
      /* if (user != null) {
        LoadLocation();
       } else {
           LoadOTP();

        }*/





        btnverify =findViewById(R.id.btnverify);
        txtmobile=findViewById(R.id.txtmobile);
        txtotp=findViewById(R.id.txtotp);
        auth=FirebaseAuth.getInstance();


        mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {



                code = phoneAuthCredential.getSmsCode();
               txtotp.setText(code);
                sp.edit().putBoolean("logged",true).apply();

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.e(TAG, ": "+e.getMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Verification_code=s;
                Toast.makeText(getApplicationContext(),"Code sent to number",Toast.LENGTH_SHORT).show();
            }
        };




    }

    public void sendsms(View v)
    {
            String number=txtmobile.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                  number,120, TimeUnit.SECONDS,this,mCallback
        );
    }

    public void singnInWithPhone(PhoneAuthCredential credential)
    {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"User Signed in successfully",Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    public void verify(View v)
    {

        code=txtotp.getText().toString();
        verifyPhonenumber(Verification_code,code);


    }

    public void verifyPhonenumber(String verifyCode,String input_code)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verifyCode,input_code);
signInWithPhoneAuthCredential(credential);
    }

    public void LoadHome()
    {


        Intent intent = new Intent(OTPValidation.this, HomePage.class);
        intent.putExtra("cust_mobile",txtmobile.getText().toString());
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            if (isNewUser) {
                                Toast.makeText(getApplicationContext(),"New User",Toast.LENGTH_SHORT).show();
                                firebaseFirestore=FirebaseFirestore.getInstance();
                                user = FirebaseAuth.getInstance().getCurrentUser();

                                Map<String,Object> userdata =new HashMap<>();
                                mobile=user.getPhoneNumber();
                                userdata.put("mobile",mobile);
                                userdata.put("cust_wallet",0);
                                cust_id = user.getUid();
                                userdata.put("cust_id",cust_id);

                                firebaseFirestore.collection("USERS")
                                        .add(userdata)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if(task.isSuccessful())
                                                {

                                                }
                                                else{

                                                }
                                            }
                                        });

                            } else {
                                Toast.makeText(getApplicationContext(),"Old User",Toast.LENGTH_SHORT).show();

                            }

                            FirebaseUser user = task.getResult().getUser();
                            Log.d("TAG","User:"+user);
                            sp.edit().putBoolean("logged",true).apply();
                            LoadLocation();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void LoadLocation()
    {


        Intent intent = new Intent(OTPValidation.this, SelectLocation.class);
        startActivity(intent);
    }



}
