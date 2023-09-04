package com.samoshin.service;

import com.samoshin.client.TransferServiceClient;
import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoTransfer implements Runnable {
    private final TransferServiceClient client;
    private long transferSum = 0;

    private Integer requestsQuantity = 1000;

    @Override
    public void run() {
        for (int i = 0; i < requestsQuantity; i++) {
                client.addMoney(1L, ++transferSum);//(long)(Math.random()*1000));
                try{
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Wake up, Neo");
                }
                client.subtractMoney(1L, ++transferSum);// (long)(Math.random()*500));
            try{
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("The Matrix has you");
            }
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
            log.error("DATABASE IS IN A NON-CONSISTENT STATE: moneybox record is {}; sum of transfers is {}",
                    moneybox.getSum(),
                    transfersSum);
        } else {
            log.info("Database is consistent: {} - {}", moneybox.getSum(), transfersSum);
        }
    }
}
