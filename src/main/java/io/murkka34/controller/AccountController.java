package io.murkka34.controller;

import io.murkka34.repo.PaymentRepo;
import io.murkka34.service.Jdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
public class AccountController {
    private final PaymentRepo paymentRepo;
    private final Jdbc jdbcService;

    @Autowired
    public AccountController(PaymentRepo paymentRepo, Jdbc jdbcService) {
        this.paymentRepo = paymentRepo;
        this.jdbcService = jdbcService;
    }

    @PostMapping
    public String createAccount() {
        String accountId = UUID.randomUUID().toString();
        paymentRepo.createAccount(accountId);
        return accountId;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            paymentRepo.transfer(amount, from, to);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/custom-status")
    public ResponseEntity<String> customStatus() {
        return ResponseEntity.status(213).body("Кастомный статус 213");
    }
}
// init метод, накатывает миграции
//http://localhost:8080/api/accounts/transfer?amount=100&from=acc1&to=acc2
//http://localhost:8080/api/accounts/custom-status