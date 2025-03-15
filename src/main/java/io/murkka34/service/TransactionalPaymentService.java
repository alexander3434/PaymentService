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

        synchronized (accounts) {
            BigDecimal fromBalance = accounts.getOrDefault(fromAccountId, BigDecimal.ZERO);
            BigDecimal toBalance = accounts.getOrDefault(toAccountId, BigDecimal.ZERO);

            if (fromBalance.compareTo(amount) < 0) {
                return false;
            }

            //откат
            BigDecimal originalFrom = fromBalance;
            BigDecimal originalTo = toBalance;

            try {
                // Вычисляем новые балансы
                BigDecimal newFrom = fromBalance.subtract(amount);
                BigDecimal newTo = toBalance.add(amount);

                //обновляем
                accounts.put(fromAccountId, newFrom);
                accounts.put(toAccountId, newTo);

                return true;
            } catch (Exception e) {
                accounts.put(fromAccountId, originalFrom);
                accounts.put(toAccountId, originalTo);

                System.err.println("Ошибка при транзакции: " + e.getMessage());
                return false;
            }
        }
    }

    @Override
    public boolean add(String toAccountId, BigDecimal amount) {
        synchronized (accounts) {
            BigDecimal currentBalance = accounts.getOrDefault(toAccountId, BigDecimal.ZERO);
            accounts.put(toAccountId, currentBalance.add(amount));
            return true;
        }
    }
}