package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.toursapp2.R;

public class LoginMode extends AppCompatActivity {

    TextView txtnewuser,txtexistinguser,txtskip;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mode);


        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getBoolean("logged",false)){
            Intent intent = new Intent(LoginMode.this, HomePage.class);
            startActivity(intent);
        }

        txtnewuser=findViewById(R.id.txt_newuser);
        txtexistinguser=findViewById(R.id.txt_existinguser);
        txtskip=findViewById(R.id.txt_skip1);


        txtnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginMode.this, EmailRegistration.class);
                startActivity(intent);

            }
        });

        txtexistinguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginMode.this, EmailVerification.class);
                startActivity(intent);

            }
        });


        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginMode.this, HomePage.class);
                startActivity(intent);

            }
        });

    }
}
