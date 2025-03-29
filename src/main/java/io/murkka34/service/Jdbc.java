package io.murkka34.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Jdbc {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String url = "jdbc:postgresql://localhost:5438/postgres";
        String name = "postgres";
        String password = "12345";

        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(url, name, password)) {
            var statement = conn.createStatement();
            statement.execute(Files.readString(Paths.get("/Users/murkka/Work/Service/src/main/resources/db.migration/V20250319154300__initi_accounts.sql")));
            var rs = statement.executeQuery("SELECT  *" +
                    "FROM payments");
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
                var account = new Accounts();
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
