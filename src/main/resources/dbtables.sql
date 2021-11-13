CREATE TABLE IF NOT EXISTS `leaderboards_players`
(
    `unique_id` VARCHAR(36) NOT NULL,
    `time_connected` BIGINT NOT NULL DEFAULT '0',
    `time_played` BIGINT NOT NULL DEFAULT '0',
    `mob_kills` INT NOT NULL DEFAULT '0',
    `blocks_broken` INT NOT NULL DEFAULT '0',
    `blocks_travelled` INT NOT NULL DEFAULT '0',
    `ores_mined` INT NOT NULL DEFAULT '0',
    `wood_mined` INT NOT NULL DEFAULT '0',
    `crops_harvested` INT NOT NULL DEFAULT '0',
    `fish_caught` INT NOT NULL DEFAULT '0',
    `skill_xp` INT NOT NULL DEFAULT '0',
    CONSTRAINT `leaderboards_players_pk` PRIMARY KEY (`unique_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `leaderboards_skulls`
(
    `unique_id` VARCHAR(36) NOT NULL,
    `texture` TEXT NOT NULL,
    CONSTRAINT `leaderboards_skulls_pk` PRIMARY KEY (`unique_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;