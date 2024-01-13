
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
    account_id TEXT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (ms_id) REFERENCES messaging_services (ms_id),
    CONSTRAINT con_primary_key PRIMARY KEY (user_id, ms_id)
);

CREATE TABLE IF NOT EXISTS groups (
    group_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS group_users (
    group_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT con_primary_key PRIMARY KEY (group_id, user_id)
);

CREATE TABLE IF NOT EXISTS user_user_contacts (
    user_id INTEGER NOT NULL,
    contact_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (contact_id) REFERENCES users (user_id),
    CONSTRAINT con_primary_key PRIMARY KEY (user_id, contact_id)
);

CREATE TABLE IF NOT EXISTS user_group_contacts (
    user_id INTEGER NOT NULL,
    group_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (group_id) REFERENCES groups (group_id),
    CONSTRAINT con_primary_key PRIMARY KEY (user_id, group_id)
);

INSERT INTO messaging_services (ms_id, name) VALUES (1, 'telegram');
INSERT INTO messaging_services (ms_id, name) VALUES (2, 'dora');

