package com.onuralbayrak.imkbhisseveendeksler.ui.hacim30;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Hacim30ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Hacim30ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Hacim-30 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
