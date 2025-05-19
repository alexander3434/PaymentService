package io.murkka34.repo;

import io.murkka34.service.HikariCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

/*public class PaymentRepo {

    public PaymentRepo() {}

    public void runMigration() throws ClassNotFoundException, SQLException, IOException {
        try (Connection conn = HikariCPDataSource.getConnection()) {
            System.out.println("Current connection: " + conn);
            var statement = conn.createStatement();
            statement.execute(Files.readString(Paths.get("/Users/murkka/Work/Service/src/main/resources/db.migration/V20250319154300__initi_accounts.sql")));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
            throw e;
        }
    }

    public void createAccount(String accountId) {
        try (Connection conn = HikariCPDataSource.getConnection()) {
            System.out.println("Current connection: " + conn);
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
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании аккаунта " + accountId, e);
        }
    }

    public void addMoneyToAccount(BigDecimal sum, String accountId, Connection connection) throws SQLException, ClassNotFoundException {
        var conn = getConnection(connection);
        System.out.println("Current connection: " + conn);
        var statement = conn.createStatement();
        var updatedRecordsCount = statement.executeUpdate("UPDATE accounts SET amount = amount + " + sum +
                " WHERE account_id = '" + accountId + "'");
        if (updatedRecordsCount == 1) {
            System.out.println("Успешно начислили " + sum + " на аккаунт " + accountId);
        } else {
            throw new RuntimeException("Не смогли начислить средства на счет " + accountId);
        }
    }

    private Connection getConnection(Connection connection) throws SQLException, ClassNotFoundException {
        if (connection != null) {
            return connection;
        } else {
            return HikariCPDataSource.getConnection();
        }
    }

    public void subtractMoneyFromAccount(BigDecimal sum, String accountId, Connection conn) throws SQLException {
        var statement = conn.createStatement();
        System.out.println("Current connection: " + conn);
        var updatedRecordsCount = statement.executeUpdate("UPDATE accounts SET amount = amount - " + sum +
                " WHERE account_id = '" + accountId + "'");
        if (updatedRecordsCount == 1) {
            System.out.println("Успешно списали " + sum + " с аккаунта " + accountId);
        } else {
            throw new RuntimeException("Не смогли списать средства со счета " + accountId);
        }
    }

    public void transfer(BigDecimal sum, String fromAccountId, String toAccountId, Connection connection) throws SQLException, ClassNotFoundException {
        Connection conn = HikariCPDataSource.getConnection();
        try (conn) {
            System.out.println("Current connection: " + conn);
            conn.setAutoCommit(false);
            subtractMoneyFromAccount(sum, fromAccountId, conn);
            addMoneyToAccount(sum, toAccountId, conn);
            System.out.println("Перевод выполнен успешно!");
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Ошибка перевода: " + e.getMessage());
        }
    } */
@Repository
public class PaymentRepo {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void runMigration() throws IOException {
        String sql = Files.readString(Paths.get("/Users/murkka/Work/Service/src/main/resources/db.migration/V20250319154300__initi_accounts.sql"));
        jdbcTemplate.execute(sql);
    }

    public void createAccount(String accountId) {
        int rows = jdbcTemplate.update(
                "INSERT INTO accounts (account_id, amount, currency) VALUES (?, 0.00, 'Rub')",
                accountId
        );
        if (rows != 1) {
            throw new RuntimeException("Не удалось создать аккаунт " + accountId);
        }
    }

    public void addMoneyToAccount(BigDecimal sum, String accountId) {
        int updated = jdbcTemplate.update(
                "UPDATE accounts SET amount = amount + ? WHERE account_id = ?",
                sum, accountId
        );
        if (updated != 1) {
            throw new RuntimeException("Не смогли начислить средства на счет " + accountId);
        }
    }

    public void subtractMoneyFromAccount(BigDecimal sum, String accountId) {
        int updated = jdbcTemplate.update(
                "UPDATE accounts SET amount = amount - ? WHERE account_id = ?",
                sum, accountId
        );
        if (updated != 1) {
            throw new RuntimeException("Не смогли списать средства со счета " + accountId);
        }
    }

    @Transactional
    public void transfer(BigDecimal sum, String fromAccountId, String toAccountId) {
        subtractMoneyFromAccount(sum, fromAccountId);
        addMoneyToAccount(sum, toAccountId);
    }
}
