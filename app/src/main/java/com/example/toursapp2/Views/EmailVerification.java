package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailVerification extends AppCompatActivity {

    private EditText email,pass;
    private TextView login;
    private ProgressBar progressBar;
    SharedPreferences sp;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);


        progressBar=findViewById(R.id.progressBar);
        email=findViewById(R.id.editemail);
        pass=findViewById(R.id.editpass);
        login=findViewById(R.id.lin);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

 //       sp = getSharedPreferences("login",MODE_PRIVATE);
 //       if(sp.getBoolean("logged",false)){
    //        LoadHome();
    //    }



        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkiputs();
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkiputs();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkemailandpass();

            }
        });
    }

    private void checkemailandpass() {

        if(email.getText().toString().matches(emailPattern)){
            if(pass.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
                login.setEnabled(false);
                login.setTextColor(Color.argb(50,255, 255, 255));




                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();


                                    String stemail = email.getText().toString();
                        //            sp.edit().putBoolean("logged",true).apply();
                                   LoadLocation();
                                    EmailVerification.this.finish();


                                }else
                                {
                                    login.setEnabled(true);
                                    login.setTextColor(Color.rgb(255, 255, 255));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String error=task.getException().toString();
                                    Toast.makeText(EmailVerification.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }else {
                Toast.makeText(EmailVerification.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(EmailVerification.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkiputs() {

        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(pass.getText())){
                login.setEnabled(true);
                login.setTextColor(Color.rgb(255, 255, 255));

            }else {
                login.setEnabled(false);
                login.setTextColor(Color.argb(50,255, 255, 255));

            }
        }else{
            login.setEnabled(false);
            login.setTextColor(Color.argb(50,255, 255, 255));

        }
    }

    public void LoadOTP(View view)
    {


        Intent intent = new Intent(EmailVerification.this, OTPValidation.class);
        startActivity(intent);
    }
    public void LoadHome()
    {


        Intent intent = new Intent(EmailVerification.this, HomePage.class);
        startActivity(intent);
    }
    public void LoadLocation()
    {


        Intent intent = new Intent(EmailVerification.this, SelectLocation.class);
        startActivity(intent);
    }
}
