package com.example.wh40kapp;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wh40kapp.fragments.ModelViewerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ModelViewerFragmentAdapter modelViewerFragmentAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        tabLayout = findViewById(R.id.tab_layout_battle);
        viewPager2 = findViewById(R.id.viewpager2_battle);
        modelViewerFragmentAdapter = new ModelViewerFragmentAdapter(this);
        viewPager2.setAdapter(modelViewerFragmentAdapter);
        viewPager2.setCurrentItem(0);
        
    }
}