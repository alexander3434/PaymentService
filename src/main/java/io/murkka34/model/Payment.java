package io.murkka34.model;

import java.math.BigDecimal;

public class Payment {
    public Long paymentId;
    public BigDecimal accountTo;
    public BigDecimal accountFrom;
    public BigDecimal amount;
    public String status;
    public String failedReason;
    public Long createdAt;
    public Long updatedAt;
    public String extra;
}
