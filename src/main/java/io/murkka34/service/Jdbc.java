package io.murkka34.service;

import io.murkka34.model.Account;
import io.murkka34.model.Payment;
import io.murkka34.repo.PaymentRepo;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Jdbc {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        PaymentRepo repo = new PaymentRepo();

        try (Connection connection = HikariCPDataSource.getConnection()) {
            repo.transfer(new BigDecimal("100.00"), "Bot1", "Bot2", connection);
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении main: " + e.getMessage());
        }

        repo.runMigration();

        var accountId = UUID.randomUUID().toString();
        repo.createAccount(accountId);
        repo.addMoneyToAccount(new BigDecimal("123.45"), accountId, null);

        try (Connection conn = HikariCPDataSource.getConnection()) {
            var statement = conn.createStatement();
            var rs = statement.executeQuery("SELECT * FROM payments");
            var payments = new ArrayList<Payment>();
            while (rs.next()) {
                var payment = new Payment();
                payment.paymentId = rs.getLong("payment_id");
                payment.accountTo = rs.getBigDecimal("account_to");
                payment.accountFrom = rs.getBigDecimal("account_from");
                payment.amount = rs.getBigDecimal("amount");
                payment.status = rs.getString("status");
                payment.failedReason = rs.getString("failed_reason");
                payment.createdAt = rs.getLong("created_at");
                payment.updatedAt = rs.getLong("updated_at");
                payment.extra = rs.getString("extra");
                payments.add(payment);
            }

            var accounts = new ArrayList<>();
            while (rs.next()){
                var account = new Account();
                account.accountId = rs.getBigDecimal("account_id");
                account.amount = rs.getBigDecimal("amount");
                account.currency = rs.getString("currency");
                accounts.add(account);
            }

            System.out.println("Ok.200");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        }
    }
}