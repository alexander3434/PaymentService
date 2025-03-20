package io.murkka34.service;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionalPaymentServiceTest {

    @Test
    public void transactionalPaymentServiceTest() {
        TransactionalPaymentService service = new TransactionalPaymentService();

        service.add("A", new BigDecimal("300"));

        var successful = service.transfer("A", "B", new BigDecimal("120"));

        if (successful) {
            assertEquals(new BigDecimal("180"), service.balance("A"));
            assertEquals(new BigDecimal("120"), service.balance("B"));
        } else {
            assertEquals(new BigDecimal("300"), service.balance("A"));
            assertEquals(new BigDecimal("0"), service.balance("B"));
        }

        boolean secondTransferSuccessful;
        do {
            secondTransferSuccessful = service.transfer("B", "A", new BigDecimal("30"));
        } while (!secondTransferSuccessful);
        assertEquals(new BigDecimal("210"), service.balance("A"));
        assertEquals(new BigDecimal("90"), service.balance("B"));
    }
}
