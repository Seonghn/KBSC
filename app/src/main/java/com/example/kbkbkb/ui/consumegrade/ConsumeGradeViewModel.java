package com.example.kbkbkb.ui.consumegrade;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConsumeGradeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConsumeGradeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ConsumeGrade fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}