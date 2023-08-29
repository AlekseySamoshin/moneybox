package com.samoshin.client;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TransferServiceClient {
    private static final String URL = "http://localhost:8080/moneybox";

    private final WebClient webClient = WebClient.create(URL);
    private Integer callsCounter = 0;

    public MoneyTransferDto addMoney(Long moneyboxId, Long sum) throws RuntimeException{
        System.out.println(++callsCounter + " Client: add money " + sum);
        return webClient.post()
                .uri(URL + "/" + moneyboxId + "/add/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyTransferDto subtractMoney(Long moneyboxId, Long sum) {
        System.out.println(++callsCounter + " Client: subtract money " + sum);
        return webClient.post()
                .uri(URL + "/" + moneyboxId + "/subtract/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyboxDto getInfo(Long moneyboxId) {
        return webClient.get()
                .uri(URL + "/" + moneyboxId)
                .retrieve()
                .bodyToMono(MoneyboxDto.class)
                .block();
    }
}
