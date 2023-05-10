package com.example.wh40kapp.fragments;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ModelViewerFragmentAdapter extends FragmentStateAdapter {
    public Fragment[] fragments = new Fragment[3];
    public ModelViewerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("TAG", "createFragment: "+position);
        if (position != 2){
            fragments[position] = new ModelViewerFragment();
            return fragments[position];
        }
        //TODO: add the battle results fragment
        fragments[position] = new ResultsViewerFragment(fragments);
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
