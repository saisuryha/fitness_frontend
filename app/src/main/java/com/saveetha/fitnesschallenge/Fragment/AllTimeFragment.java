package com.saveetha.fitnesschallenge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class AllTimeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // This connects the fragment to its layout file
        return inflater.inflate(R.layout.fragment_alltime, container, false);
    }
}