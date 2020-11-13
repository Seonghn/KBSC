package com.example.kbkbkb.ui.serverCom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServerCommunicationModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ServerCommunicationModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ServerCommunication Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}