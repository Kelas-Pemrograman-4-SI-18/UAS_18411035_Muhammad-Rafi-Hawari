package com.rafihawari.penjualantiket.users.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.rafihawari.penjualantiket.R;
import com.rafihawari.penjualantiket.users.LoginActivity;
import com.rafihawari.penjualantiket.users.session.PrefSetting;
import com.rafihawari.penjualantiket.users.session.SessionManager;

public class HomeAdminActivity extends AppCompatActivity {

    SessionManager session;
    SharedPreferences prefs;
    PrefSetting prefSetting;
    CardView cardExit, cardDataPenumpang, cardInputDataPenumpang, cardProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        prefSetting = new PrefSetting( this );
        prefs = prefSetting.getSharePreferences();

        session = new SessionManager( HomeAdminActivity.this );

        prefSetting.isLogin(session, prefs);

        cardExit = (CardView) findViewById(R.id.cardExit);
        cardDataPenumpang = (CardView) findViewById(R.id.cardDataPenumpang);
        cardInputDataPenumpang = (CardView) findViewById(R.id.cardInputDataPenumpang);
        cardProfil = (CardView) findViewById(R.id.cardProfil);

        cardExit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                session.setSessid(0);
                Intent i = new Intent(HomeAdminActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        cardDataPenumpang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this, ActivityDataPenumpang.class);
                startActivity(i);
                finish();
            }
        });

        cardInputDataPenumpang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this, InputDataPenumpangActivity.class);
                startActivity(i);
                finish();
            }
        });

        cardProfil.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeAdminActivity.this, ProfilActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}