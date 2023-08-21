package com.samoshin.client;

import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.dto.MoneyboxDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TransferServiceClient {
    private static final Integer TRY_LIMIT = 15;
    private static final String URL = "http://localhost:8080/moneybox";

    private final WebClient webClient = WebClient.create(URL);

    public MoneyTransferDto addMoney(Long moneyboxId, Long sum) throws RuntimeException{
        return webClient.post()
                .uri(URL + "/" + moneyboxId + "/add/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyTransferDto subtractMoney(Long moneyboxId, Long sum) {
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
