package com.onuralbayrak.imkbhisseveendeksler.ui.imkbYukselenler;

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

public class YukselenlerFragment extends Fragment {

    private YukselenlerViewModel yukselenlerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yukselenlerViewModel =
                new ViewModelProvider(this).get(YukselenlerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_imkb_yukselenler, container, false);
        final TextView textView = root.findViewById(R.id.text_yukselenler);
        yukselenlerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}