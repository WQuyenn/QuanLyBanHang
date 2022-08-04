package com.nhomduan.quanlydathang_admin.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;


public class FlashActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String passState = OverUtils.getSPInstance(FlashActivity.this, OverUtils.PASS_FILE)
                .getString("pass", OverUtils.NO_PASS);
        setUpPassAction(passState);
        img = findViewById(R.id.img3);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slider_in_right);
        img.startAnimation(animation);
        Start();
    }

    private void setUpPassAction(String passState) {
        switch (passState) {
            case OverUtils.NO_PASS:
                setContentView(R.layout.activity_flast);
                break;
            case OverUtils.PASS_MAIN_ACTIVITY:
                setContentView(R.layout.activity_flast);
                break;
            default:
                break;
        }
    }

    public void Start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor editor = OverUtils.getSPInstance(FlashActivity.this, OverUtils.PASS_FILE).edit();
                editor.putString("pass", OverUtils.PASS_MAIN_ACTIVITY);
                editor.apply();
                startActivity(new Intent(FlashActivity.this, MainActivity.class));
                finish();
            }
        }, 1900);
    }
}
