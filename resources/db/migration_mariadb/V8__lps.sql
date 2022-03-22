CREATE TABLE IF NOT EXISTS lp (db_id bigint(20) NOT NULL AUTO_INCREMENT, version TINYINT, lp_id bigint(20) NOT NULL,
    name varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
    swap_fee INT NOT NULL, platform_fee INT NOT NULL, platform_account_id BIGINT NOT NULL,
    height INT NOT NULL, latest BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`db_id`));

CREATE TABLE IF NOT EXISTS lp_tokens (db_id bigint(20) NOT NULL AUTO_INCREMENT, lp_id bigint(20) NOT NULL,
    token_id bigint(20) NOT NULL, factor INT NOT NULL,
    height INT NOT NULL, latest BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`db_id`));

CREATE TABLE IF NOT EXISTS lp_balances (db_id bigint(20) NOT NULL AUTO_INCREMENT, lp_id bigint(20) NOT NULL,
    token_id bigint(20) NOT NULL, balance BIGINT NOT NULL,
    height INT NOT NULL, latest BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`db_id`));

