package com.example.wh40kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wh40kapp.R;
import com.example.wh40kapp.adapters.ModelViewerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ModelViewerFragmentAdapter modelViewerFragmentAdapter;
    private FirebaseAuth mAuth;
    private Button button_returnToLandingPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: prevent app from rotating - it crashes when rotating
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        }

        tabLayout = findViewById(R.id.tab_layout_battle);
        viewPager2 = findViewById(R.id.viewpager2_battle);
        modelViewerFragmentAdapter = new ModelViewerFragmentAdapter(this);
        viewPager2.setAdapter(modelViewerFragmentAdapter);
        button_returnToLandingPage = findViewById(R.id.button_returnToLandingPage1);
        button_returnToLandingPage.setOnClickListener(v -> {
            Intent intent = new Intent(this, LandingPage.class);
            startActivity(intent);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                Log.d("TEST 1", "onTabSelected: 1");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
}