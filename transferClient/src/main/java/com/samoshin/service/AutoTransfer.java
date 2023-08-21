package com.samoshin.service;

import com.samoshin.client.TransferServiceClient;
import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.dto.MoneyboxDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransfer implements Runnable {
    private final TransferServiceClient client;

    private Integer operationsNumber = 500;

    @Override
    public void run() {
        for (int i = 0; i < operationsNumber; i++) {
                client.addMoney(1L, (long)(Math.random()*1000));
                client.subtractMoney(1L, (long)(Math.random()*1000));
        }
        checkConsistency();
    }

    public void checkConsistency() {
        long transfersSum = 0L;
        MoneyboxDto moneybox = client.getInfo(1L);
        for (MoneyTransferDto transfer : moneybox.transfers()) {
            transfersSum = transfer.increase() ? transfersSum + transfer.sum() : transfersSum - transfer.sum();
        }
        if (transfersSum != moneybox.sum().longValue()) {
            System.out.println("ERROR! No matching: \nmoneybox record is " + moneybox.sum()
                                + "\nsum of transfers is " + transfersSum);
        } else {
            System.out.println("CONSISTENT!");
        }
    }
}
