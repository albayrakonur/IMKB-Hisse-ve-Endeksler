package com.onuralbayrak.imkbhisseveendeksler.ui.hacim100;

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
import com.onuralbayrak.imkbhisseveendeksler.ui.hacim30.Hacim30ViewModel;

public class Hacim100Fragment extends Fragment {

    private Hacim100ViewModel hacim100ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hacim100ViewModel =
                new ViewModelProvider(this).get(Hacim100ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hacim100, container, false);
        final TextView textView = root.findViewById(R.id.text_hacim100);
        hacim100ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
