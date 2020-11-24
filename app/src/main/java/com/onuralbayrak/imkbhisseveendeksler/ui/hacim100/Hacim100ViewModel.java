package com.onuralbayrak.imkbhisseveendeksler.ui.hacim100;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Hacim100ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Hacim100ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Hacim-100 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
