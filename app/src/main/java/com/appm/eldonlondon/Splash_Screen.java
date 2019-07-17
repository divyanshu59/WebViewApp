package com.appm.eldonlondon;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (isNetworkAvailable()){
                    Intent mainIntent = new Intent(Splash_Screen.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else{
                    Toast.makeText(Splash_Screen.this, "Please Open Your Internet Connection and Open App Again", Toast.LENGTH_SHORT).show();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activenetworkInfo != null && activenetworkInfo.isConnected();
    }
}