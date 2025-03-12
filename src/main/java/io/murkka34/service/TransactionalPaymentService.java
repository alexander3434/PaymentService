package io.murkka34.service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionalPaymentService implements PaymentService {
    private final Map<String, BigDecimal> accounts = new HashMap<>();

    @Override
    public BigDecimal balance(String accountId) {
        return accounts.getOrDefault(accountId, BigDecimal.ZERO);
    }
    @Override
    public synchronized boolean transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false; //Корректна ли сумма
        }

        try {
            BigDecimal fromBalance = accounts.getOrDefault(fromAccountId, BigDecimal.ZERO);
            BigDecimal toBalance = accounts.getOrDefault(toAccountId, BigDecimal.ZERO);

            if (fromBalance.compareTo(amount) < 0) {
                return false;
            }

            // выполняется/не выполняется
            accounts.put(fromAccountId, fromBalance.subtract(amount));
            accounts.put(toAccountId, toBalance.add(amount));
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении транзакции: " + e.getMessage());
            return false;
        }
    }
}