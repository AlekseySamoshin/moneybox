package com.samoshin.client;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class TransferServiceClient {
    private static final String URL = "http://localhost:8080/moneybox";

    private final WebClient webClient = WebClient.create(URL);
    private Integer callsCounter = 0;

    public MoneyTransferDto addMoney(Long moneyboxId, Long sum) throws RuntimeException{
        log.info("{} Transfer Service Client: add money request sending[moneyboxId={}, sum={}]",
                ++callsCounter,
                moneyboxId,
                sum);
        return webClient.post()
                .uri(URL + "/" + moneyboxId + "/add/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyTransferDto subtractMoney(Long moneyboxId, Long sum) {
        log.info("{} Transfer Service Client: subtract money request sending[moneyboxId={}, sum={}]",
                ++callsCounter,
                moneyboxId,
                sum);
        return webClient.post()
                .uri(URL + "/" + moneyboxId + "/subtract/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyboxDto getInfo(Long moneyboxId) {
        log.info("Transfer Service Client: get info request sending; moneyboxId={}", moneyboxId);
        return webClient.get()
                .uri(URL + "/" + moneyboxId)
                .retrieve()
                .bodyToMono(MoneyboxDto.class)
                .block();
    }
}
