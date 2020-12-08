package com.example.barcode.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.barcode.DataRepository;
import com.example.barcode.db.entity.Message;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    private final LiveData<List<Message>> mMessages;

    public MessageViewModel(Application application) {
        super(application);
        mRepository = DataRepository.getInstance(application);
        mMessages = mRepository.loadMessages();
    }

    public LiveData<List<Message>> getAllMessages() { return mMessages; }

    public void insert(Message message) { mRepository.insert(message); }
}