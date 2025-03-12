package io.murkka34.service;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SimplePaymentServiceTest {
    @Test
    public void SimplePaymentService() {
        SimplePaymentService service = new SimplePaymentService();

        service.transfer("A", "B", new BigDecimal("100"));
        assertEquals(BigDecimal.ZERO, service.balance("A"));
        assertEquals(new BigDecimal("100"), service.balance("B"));

        service.transfer("B", "A", new BigDecimal("50"));
        assertEquals(new BigDecimal("50"), service.balance("A"));
        assertEquals(new BigDecimal("50"), service.balance("B"));
    }
}