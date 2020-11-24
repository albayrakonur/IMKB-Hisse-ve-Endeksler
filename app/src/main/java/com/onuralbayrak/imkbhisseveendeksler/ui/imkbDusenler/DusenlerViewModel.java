package com.onuralbayrak.imkbhisseveendeksler.ui.imkbDusenler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DusenlerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DusenlerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is imkbDusenler fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}