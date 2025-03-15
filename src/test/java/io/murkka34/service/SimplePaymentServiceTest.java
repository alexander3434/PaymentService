package io.murkka34.service;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SimplePaymentServiceTest {
    @Test
    public void simplePaymentServiceTest() {
        SimplePaymentService service = new SimplePaymentService();

        service.add("A", new BigDecimal("200"));

        service.transfer("A", "B", new BigDecimal("100"));
        assertEquals(new BigDecimal("100"), service.balance("A"));
        assertEquals(new BigDecimal("100"), service.balance("B"));

        service.transfer("B", "A", new BigDecimal("50"));
        assertEquals(new BigDecimal("150"), service.balance("A"));
        assertEquals(new BigDecimal("50"), service.balance("B"));
    }
}