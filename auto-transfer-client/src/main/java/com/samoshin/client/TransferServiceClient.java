package com.samoshin.client;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class TransferServiceClient {

    @Value("${moneybox-server.url}")
    private String url;
//    private static final String URL = "http://localhost:8080/moneybox";

    private final WebClient webClient = WebClient.create(url);
    private Integer callsCounter = 0;

    public MoneyTransferDto addMoney(Long moneyboxId, Long sum) throws RuntimeException{
        log.info("{} Transfer Service Client: add money request sending[moneyboxId={}, sum={}]",
                ++callsCounter,
                moneyboxId,
                sum);
        return webClient.post()
                .uri(url + "/" + moneyboxId + "/add/" + sum)
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
                .uri(url + "/" + moneyboxId + "/subtract/" + sum)
                .retrieve()
                .bodyToMono(MoneyTransferDto.class)
                .block();
    }

    public MoneyboxDto getInfoRequest(Long moneyboxId) {
        log.info("Transfer Service Client: get info request sending; moneyboxId={}", moneyboxId);
        return webClient.get()
                .uri(url + "/" + moneyboxId)
                .retrieve()
                .bodyToMono(MoneyboxDto.class)
                .block();
    }
}
