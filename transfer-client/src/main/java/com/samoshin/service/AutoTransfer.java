package com.samoshin.service;

import com.samoshin.client.TransferServiceClient;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransfer implements Runnable {
    private final TransferServiceClient client;

    private Integer operationsNumber = 100;

    @Override
    public void run() {
        for (int i = 0; i < operationsNumber; i++) {
                client.addMoney(1L, (long)(Math.random()*1000));
                client.subtractMoney(1L, (long)(Math.random()*500));
        }
        checkConsistency();
    }

    public void checkConsistency() {
        long transfersSum = 0L;
        MoneyboxDto moneybox = client.getInfo(1L);
        for (MoneyTransferDto transfer : moneybox.getTransfers()) {
            transfersSum = transfer.isIncrease() ? transfersSum + transfer.getSum() : transfersSum - transfer.getSum();
        }
        if (transfersSum != moneybox.getSum().longValue()) {
            System.out.println("ERROR! No matching: \nmoneybox record is " + moneybox.getSum()
                                + "\nsum of transfers is " + transfersSum);
        } else {
            System.out.println("CONSISTENT! (" + moneybox.getSum() + " - " + transfersSum +")");
        }
    }
}
