CREATE TABLE IF NOT EXISTS payments
(
    payment_id    BIGSERIAL PRIMARY KEY NOT NULL,
    account_to    TEXT                  NOT NULL,
    account_from  TEXT                  NOT NULL,
    amount        NUMERIC               NOT NULL,
    status        TEXT                  NOT NULL,
    failed_reason TEXT,
    created_at    NUMERIC               NOT NULL,
    updated_at    NUMERIC               NOT NULL,
    extra         json
);

INSERT INTO payments (account_to, account_from, amount, status, failed_reason, created_at, updated_at, extra) VALUES
('Bot', 'Bot2', '10', 'completed', '', '1742902576124', '1742902576124', '{}'::json);

CREATE TABLE IF NOT EXISTS accounts
(
    account_id TEXT PRIMARY KEY,
    amount     NUMERIC NOT NULL,
    currency   TEXT    NOT NULL
);