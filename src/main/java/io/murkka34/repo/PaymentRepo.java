package io.murkka34.repo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PaymentRepo {

    private final String url;
    private final String user;
    private final String password;

    public PaymentRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void runMigration() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            var statement = conn.createStatement();
            statement.execute(Files.readString(Paths.get("/Users/murkka/Work/Service/src/main/resources/db.migration/V20250319154300__initi_accounts.sql")));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
            throw e;
        }
    }

    public void createAccount(String accountId) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            var stmt = conn.createStatement();
            int rows = stmt.executeUpdate(
                    "INSERT INTO accounts (account_id, amount, currency) " +
                            "VALUES ('" + accountId + "', 0.00, 'Rub')"
            );
            if (rows == 1) {
                System.out.println("Создан аккаунт " + accountId);
            } else {
                throw new RuntimeException("Не удалось создать аккаунт " + accountId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании аккаунта " + accountId, e);
        }
    }

    public void addMoneyToAccount(BigDecimal sum, String accountId, Connection connection) throws SQLException {
        var conn = getConnection(connection);
        var statement = conn.createStatement();
        var updatedRecordsCount = statement.executeUpdate("UPDATE accounts SET amount = amount + " + sum +
                " WHERE account_id = '" + accountId + "'");
        if (updatedRecordsCount == 1) {
            System.out.println("Успешно начислили " + sum + " на аккаунт " + accountId);
        } else {
            throw new RuntimeException("Не смогли начислить средства на счет " + accountId);
        }
    }

    private Connection getConnection(Connection connection) throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            return DriverManager.getConnection(url, user, password);
        }
    }

    public void subtractMoneyFromAccount(BigDecimal sum, String accountId, Connection conn) throws SQLException {
        var statement = conn.createStatement();
        var updatedRecordsCount = statement.executeUpdate("UPDATE accounts SET amount = amount - " + sum +
                " WHERE account_id = '" + accountId + "'");
        if (updatedRecordsCount == 1) {
            System.out.println("Успешно списали " + sum + " с аккаунта " + accountId);
        } else {
            throw new RuntimeException("Не смогли списать средства со счета " + accountId);
        }
    }

    public void transfer(BigDecimal sum, String fromAccountId, String toAccountId, Connection connection) throws SQLException {
        Connection conn = getConnection(connection);
        try (connection; conn) {
            conn.setAutoCommit(false);
            subtractMoneyFromAccount(sum, fromAccountId, conn);
            addMoneyToAccount(sum, toAccountId, conn);
            System.out.println("Перевод выполнен успешно!");
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Ошибка перевода: " + e.getMessage());
        }
    }
}