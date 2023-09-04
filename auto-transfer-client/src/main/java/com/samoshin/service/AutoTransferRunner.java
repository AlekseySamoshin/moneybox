package com.samoshin.service;

import com.samoshin.client.TransferServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransferRunner {
    private final TransferServiceClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void runManyThreads() {
        AutoTransfer autoTransfer1 = new AutoTransfer(client);
        AutoTransfer autoTransfer2 = new AutoTransfer(client);
        AutoTransfer autoTransfer3 = new AutoTransfer(client);
        AutoTransfer autoTransfer4 = new AutoTransfer(client);
        AutoTransfer autoTransfer5 = new AutoTransfer(client);
        AutoTransfer autoTransfer6 = new AutoTransfer(client);
        AutoTransfer autoTransfer7 = new AutoTransfer(client);
        Thread thread1 = new Thread(autoTransfer1);
        Thread thread2 = new Thread(autoTransfer2);
        Thread thread3 = new Thread(autoTransfer3);
        Thread thread4 = new Thread(autoTransfer4);
        Thread thread5 = new Thread(autoTransfer5);
        Thread thread6 = new Thread(autoTransfer6);
        Thread thread7 = new Thread(autoTransfer7);
        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();
//        thread5.start();
//        thread6.start();
//        thread7.start();
    }
}
