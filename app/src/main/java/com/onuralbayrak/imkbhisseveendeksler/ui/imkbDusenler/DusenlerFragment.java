package com.onuralbayrak.imkbhisseveendeksler.ui.imkbDusenler;

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

public class DusenlerFragment extends Fragment {

    private DusenlerViewModel dusenlerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dusenlerViewModel =
                new ViewModelProvider(this).get(DusenlerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_imkb_dusenler, container, false);
        final TextView textView = root.findViewById(R.id.text_dusenler);
        dusenlerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}