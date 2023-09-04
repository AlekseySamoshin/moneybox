package com.samoshin.moneybox.controller;

import com.samoshin.dto.MoneyTransferDto;
import com.samoshin.dto.MoneyboxDto;
import com.samoshin.moneybox.service.MoneyboxGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/moneybox")
@RequiredArgsConstructor
public class MoneyboxController {
    private final MoneyboxGatewayService moneyboxGatewayService;

    @PostMapping("/{moneyboxId}/add/{sum}")
    MoneyTransferDto addMoney(@PathVariable Long moneyboxId,
                              @PathVariable Long sum) {
        Boolean increase = true;
        log.info("Request: add money[moneybox-id={}, sum={}]", moneyboxId, sum);
        return moneyboxGatewayService.sendTransfer(moneyboxId, sum, increase);
    }

    @PostMapping("/{moneyboxId}/subtract/{sum}")
    MoneyTransferDto subtractMoney(@PathVariable Long moneyboxId,
                                   @PathVariable Long sum) {
        Boolean increase = false;
        log.info("Request: subtract money[moneybox-id={}, sum={}]", moneyboxId, sum);
        return moneyboxGatewayService.sendTransfer(moneyboxId, sum, increase);
    }

    @GetMapping("/{moneyboxId}")
    String getInfo(@PathVariable Long moneyboxId) {
        log.info("Request: get info about moneybox id={}", moneyboxId);
        return moneyboxGatewayService.getInfo(moneyboxId);
    }
}
