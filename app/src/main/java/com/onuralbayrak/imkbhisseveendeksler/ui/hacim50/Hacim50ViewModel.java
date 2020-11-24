package com.onuralbayrak.imkbhisseveendeksler.ui.hacim50;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Hacim50ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Hacim50ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Hacim-50 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
