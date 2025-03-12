package io.murkka34.service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimplePaymentService implements PaymentService {
    private final Map<String, BigDecimal> accounts = new HashMap<>();

    @Override
    public BigDecimal balance(String accountId) {
        return accounts.getOrDefault(accountId, BigDecimal.ZERO);
    }
    @Override
    public boolean transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        BigDecimal fromBalance = accounts.getOrDefault(fromAccountId, BigDecimal.ZERO);
        if (fromBalance.compareTo(amount) < 0) {
            return false;
        }

        accounts.put(fromAccountId, fromBalance.subtract(amount));
        accounts.put(toAccountId, accounts.getOrDefault(toAccountId, BigDecimal.ZERO).add(amount)); // Зачисление средств
        return true;
    }
}