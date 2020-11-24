package com.onuralbayrak.imkbhisseveendeksler.ui.HisseEndeksler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is HisseEndeksler fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}