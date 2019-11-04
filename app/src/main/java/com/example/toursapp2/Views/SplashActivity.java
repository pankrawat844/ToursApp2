package com.example.toursapp2.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.toursapp2.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);






        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(!restorePrefData()) {
                        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(SplashActivity.this, LoginMode.class);
                        startActivity(intent);
                        finish();
                    }

            }

        },2500);
    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }


    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();


    }
}
