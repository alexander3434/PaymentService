package io.murkka34.service;

import java.math.BigDecimal;
public interface PaymentService {

    // Возвращает баланс счёта с идентификатором accountId
    BigDecimal balance(String accountId);

    /**
     * Переводит сумму amount с счёта с идентификатором fromAccountId на счёт с идентификатором toAccountId
     *
     * @param fromAccountId идентификатор счёта, с которого осуществляется перевод
     * @param toAccountId идентификатор счёта, на который осуществляется перевод
     * @param amount сумма перевода
     * @return true, если перевод прошел успешно
     */

    boolean transfer(String fromAccountId, String toAccountId, BigDecimal amount);

    boolean add(String toAccountId, BigDecimal amount);
}
