package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EmailRegistration extends AppCompatActivity {



        private Animation animation;
        private ConstraintLayout layout;
        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firebaseFirestore;
        private ProgressBar progressBar;
        private Random Number;
        StringBuilder UID;
        private EditText email,pass,confirmpass;
        private Button btn;
        private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
SharedPreferences sp;
        String cust_id;
        private TextView uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);



        // Generate random id, for example 283952-V8M32


        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getBoolean("logged",false)){
            Intent intent = new Intent(EmailRegistration.this, HomePage.class);
            startActivity(intent);
        }








            progressBar=findViewById(R.id.progressBar);

            email=findViewById(R.id.editemail);
            pass=findViewById(R.id.editpass);
            confirmpass=findViewById(R.id.editconfirmpass);
            btn=findViewById(R.id.btnregister);

            layout =findViewById(R.id.signin);


            firebaseAuth = FirebaseAuth.getInstance();
            firebaseFirestore =FirebaseFirestore.getInstance();


            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkinputs();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            pass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    checkinputs();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            confirmpass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    checkinputs();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    checkmobilenumberandpass();
                }
            });




        }


        private void checkmobilenumberandpass() {


            if(email.getText().toString().matches(emailPattern)) {
                if(pass.getText().toString().equals(confirmpass.getText().toString())) {



                    progressBar.setVisibility(View.VISIBLE);
                    btn.setTextColor(Color.argb(50, 255, 255, 255));
                    btn.setEnabled(false);







                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {







                                    if (task.isSuccessful()) {
                                        Map<String,Object> userdata =new HashMap<>();
                                        userdata.put("email",email.getText().toString());
                                        userdata.put("pass",confirmpass.getText().toString());
                                        userdata.put("cust_wallet",0);
                                        cust_id = FirebaseAuth.getInstance().getUid();
                                        userdata.put("cust_id",cust_id);

                                        firebaseFirestore.collection("USERS")
                                                .add(userdata)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if(task.isSuccessful())
                                                        {


                                                            Intent intent = new Intent(EmailRegistration.this, EmailVerification.class);
                                                            startActivity(intent);
                                                            EmailRegistration.this.finish();
                                                        }
                                                        else{

                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            btn.setEnabled(true);
                                                            btn.setTextColor(Color.rgb(255, 255, 255));
                                                            String error = task.getException().getMessage();


                                                        }
                                                    }
                                                });



                                    } else {
                                        btn.setEnabled(false);
                                        btn.setTextColor(Color.argb(50, 255, 255, 255));

                                        String error = task.getException().getMessage();

                                    }
                                }
                            });
                }else{
                    confirmpass.setError("Password dosen't match");
                }

            } else
            {

                email.setError("Enter a Valid email address");
            }





        }

        private void checkinputs() {

            if (!TextUtils.isEmpty(email.getText())) {
                if (!TextUtils.isEmpty(email.getText())) {
                    if (!TextUtils.isEmpty(confirmpass.getText()) && pass.length() >= 8) {
                        btn.setEnabled(true);
                        btn.setTextColor(Color.rgb(255, 255, 255));
                    }else{
                        btn.setEnabled(false);
                        btn.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    btn.setEnabled(false);
                    btn.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                btn.setEnabled(false);
                btn.setTextColor(Color.argb(50, 255, 255, 255));
            }

        }






        public void LoadOTP(View view)
        {


            Intent intent = new Intent(EmailRegistration.this,OTPValidation.class);
            startActivity(intent);
        }
    }



