package com.samoshin.moneybox.controller;

import com.samoshin.moneybox.dto.MoneyTransferDto;
import com.samoshin.moneybox.dto.MoneyboxDto;
import com.samoshin.moneybox.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/moneybox")
@RequiredArgsConstructor
public class MoneyboxController {
    private final MoneyTransferService moneyTransferService;

    @PostMapping("/{moneyboxId}/add/{sum}")
    MoneyTransferDto addMoney(@PathVariable Long moneyboxId,
                              @PathVariable Long sum) {
        return moneyTransferService.addMoney(moneyboxId, sum);
    }

    @PostMapping("/{moneyboxId}/subtract/{sum}")
    MoneyTransferDto subtractMoney(@PathVariable Long moneyboxId,
                                   @PathVariable Long sum) {
        return moneyTransferService.subtractMoney(moneyboxId, sum);
    }

    @GetMapping("/{moneyboxId}")
    MoneyboxDto getInfo(@PathVariable Long moneyboxId) {
        return moneyTransferService.getInfo(moneyboxId);
    }
}
