package com.example.barcode;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.barcode.api.grpc.DemoRequest;
import com.example.barcode.api.grpc.DemoResponse;
import com.example.barcode.api.grpc.DemoServiceGrpc;
import com.example.barcode.db.AppDatabase;
import com.example.barcode.db.dao.MessageDao;
import com.example.barcode.db.entity.Message;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DataRepository {

    private static volatile DataRepository sInstance;

    private final MessageDao messageDao;
    DemoServiceGrpc.DemoServiceBlockingStub stub;

    private DataRepository(final MessageDao messageDao,
                           DemoServiceGrpc.DemoServiceBlockingStub stub) {
        this.messageDao = messageDao;
        this.stub = stub;
    }

    public static DataRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    ManagedChannel mChannel = ManagedChannelBuilder
                            .forAddress("192.168.0.141", 8081)
                            .usePlaintext()
                            .build();
                    DemoServiceGrpc.DemoServiceBlockingStub stub
                            = DemoServiceGrpc.newBlockingStub(mChannel);

                    sInstance = new DataRepository(
                            AppDatabase.getDatabase(application.getApplicationContext()).messageDao(),
                            stub
                    );
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Message>> loadMessages() {
        return messageDao.getAll();
    }

    public void insert(Message message) {
        DemoRequest demoRequest = DemoRequest.newBuilder()
                .setMessage(message.getText())
                .build();
        DemoResponse demoResponse = stub.demo(demoRequest);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            messageDao.insert(new Message(demoResponse.getStatusText()));
        });
    }
}
