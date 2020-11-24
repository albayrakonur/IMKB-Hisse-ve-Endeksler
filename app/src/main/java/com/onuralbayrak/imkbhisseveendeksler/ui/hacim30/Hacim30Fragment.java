package com.onuralbayrak.imkbhisseveendeksler.ui.hacim30;

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

public class Hacim30Fragment extends Fragment {

    private Hacim30ViewModel hacim30ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hacim30ViewModel =
                new ViewModelProvider(this).get(Hacim30ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hacim30, container, false);
        final TextView textView = root.findViewById(R.id.text_hacim30);
        hacim30ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
