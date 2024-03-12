package com.adjing.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    ScrollView scrollView;
    ImageView backBtn;
    ProgressBar progressBar;
    RelativeLayout versionC;
    TextView about,version,versionCode,versionChecker,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        backBtn=findViewById(R.id.back_btn);
        logout=findViewById(R.id.logout);
        about=findViewById(R.id.about);
        versionC=findViewById(R.id.versionc);
        version=findViewById(R.id.version);
        versionCode=findViewById(R.id.version_code);
        versionChecker=findViewById(R.id.version_checker);
        progressBar=findViewById(R.id.progress_bar);
        versionCode.setText(BuildConfig.VERSION_NAME);

        versionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionCode.setVisibility(View.GONE);
                versionChecker.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        versionCode.setVisibility(View.VISIBLE);
                        versionChecker.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                },2000);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.super.onBackPressed();
            }
        });
    }
}