CREATE TABLE IF NOT EXISTS payment (
                                        account_to              TEXT PRIMARY KEY,
                                        account_from            TEXT PRIMARY KEY,
                                        amount                  NUMERIC NOT NULL,
                                        status                  TEXT    NOT NULL,
                                        failed_reason             TEXT    NOT NULL,
                                        created_at              NUMERIC NOT NULL,
                                        updated_at              NUMERIC NOT NULL
);