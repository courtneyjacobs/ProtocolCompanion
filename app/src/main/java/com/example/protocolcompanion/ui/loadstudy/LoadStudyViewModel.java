package com.example.protocolcompanion.ui.loadstudy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoadStudyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LoadStudyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}