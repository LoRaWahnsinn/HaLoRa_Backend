CREATE TABLE IF NOT EXISTS messaging_services (
    ms_id INTEGER PRIMARY KEY AUTOINCREMENT ,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    receiveAt INTEGER NULL,
    FOREIGN KEY (receiveAt) REFERENCES messaging_services (ms_id)
);


CREATE TABLE IF NOT EXISTS user_accounts (
    user_id INTEGER NOT NULL,
    ms_id INTEGER NOT NULL,
    account_id TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (ms_id) REFERENCES messaging_services (ms_id),
    CONSTRAINT con_primary_key PRIMARY KEY (user_id, ms_id)
);

ALTER TABLE users ADD COLUMN receiveAt INTEGER NULL REFERENCES user_accounts (user_account_id);

INSERT INTO messaging_services (ms_id, name) VALUES (1, 'Telegram');
INSERT INTO messaging_services (ms_id, name) VALUES (2, 'Dora');

INSERT INTO users (name) VALUES ('Oskar');
INSERT INTO users (name) VALUES ('Jakob');
INSERT INTO users (name) VALUES ('Tabita');
INSERT INTO users (name) VALUES ('Alex');

