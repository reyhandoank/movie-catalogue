package com.example.mymoviecatalogue.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.fragment.SettingFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.settings);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
