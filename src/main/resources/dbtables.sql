CREATE TABLE IF NOT EXISTS `leaderboards_players`
(
    `unique_id` VARCHAR(36) NOT NULL,
    `time_connected` BIGINT NOT NULL DEFAULT '0',
    `time_played` BIGINT NOT NULL DEFAULT '0',
    `mob_kills` INT NOT NULL DEFAULT '0',
    `player_deaths` INT NOT NULL DEFAULT '0',
    `player_kills` INT NOT NULL DEFAULT '0',
    `blocks_broken` INT NOT NULL DEFAULT '0',
    `blocks_placed` INT NOT NULL DEFAULT '0',
    `blocks_travelled` INT NOT NULL DEFAULT '0',
    `cane_broken` INT NOT NULL DEFAULT '0',
    `spawners_placed` INT NOT NULL DEFAULT '0',
    `lms_wins` INT NOT NULL DEFAULT '0',
    `envoy_claims` INT NOT NULL DEFAULT '0',
    `koth_wins` INT NOT NULL DEFAULT '0',
    CONSTRAINT `leaderboards_players_pk` PRIMARY KEY (`unique_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `leaderboards_skulls`
(
    `unique_id` VARCHAR(36) NOT NULL,
    `texture` LONGTEXT NOT NULL,
    CONSTRAINT `leaderboards_skulls_pk` PRIMARY KEY (`unique_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;