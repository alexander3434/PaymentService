package io.murkka34.service;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TransactionalPaymentServiceTest {

    @Test
    public void transactionalPaymentServiceTest() {
        TransactionalPaymentService service = new TransactionalPaymentService();

        service.add("A", new BigDecimal("300"));

        service.transfer("A", "B", new BigDecimal("120"));
        assertEquals(new BigDecimal("180"), service.balance("A"));
        assertEquals(new BigDecimal("120"), service.balance("B"));

        service.transfer("B", "A", new BigDecimal("30"));
        assertEquals(new BigDecimal("210"), service.balance("A"));
        assertEquals(new BigDecimal("90"), service.balance("B"));
    }
}
