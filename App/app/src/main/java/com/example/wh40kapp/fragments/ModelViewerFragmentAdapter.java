package com.example.wh40kapp.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ModelViewerFragmentAdapter extends FragmentStateAdapter {
    public ModelViewerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ModelViewerFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
