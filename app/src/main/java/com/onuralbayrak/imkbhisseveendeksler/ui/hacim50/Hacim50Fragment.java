package com.onuralbayrak.imkbhisseveendeksler.ui.hacim50;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.onuralbayrak.imkbhisseveendeksler.R;

public class Hacim50Fragment  extends Fragment {

    private Hacim50ViewModel hacim50ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hacim50ViewModel =
                new ViewModelProvider(this).get(Hacim50ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hacim50, container, false);
        final TextView textView = root.findViewById(R.id.text_hacim50);
        hacim50ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
