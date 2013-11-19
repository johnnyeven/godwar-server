SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `godwar_game_db` ;
CREATE SCHEMA IF NOT EXISTS `godwar_game_db` DEFAULT CHARACTER SET utf8 ;
DROP SCHEMA IF EXISTS `pulse_platform_db` ;
CREATE SCHEMA IF NOT EXISTS `pulse_platform_db` DEFAULT CHARACTER SET utf8 ;
USE `godwar_game_db` ;

-- -----------------------------------------------------
-- Table `godwar_game_db`.`game_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godwar_game_db`.`game_account` ;

CREATE TABLE IF NOT EXISTS `godwar_game_db`.`game_account` (
  `account_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `game_guid` CHAR(36) NOT NULL DEFAULT '',
  `account_guid` BIGINT(20) NOT NULL,
  `level` INT NOT NULL DEFAULT 1,
  `nick_name` CHAR(32) NOT NULL DEFAULT '',
  `role_picture` CHAR(16) NOT NULL DEFAULT '',
  `account_cash` BIGINT(20) NOT NULL DEFAULT 0,
  `account_lastlogin` BIGINT NOT NULL DEFAULT 0,
  `account_lastlogout` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`account_id`),
  UNIQUE INDEX `guid` (`account_guid` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `godwar_game_db`.`game_closure_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godwar_game_db`.`game_closure_account` ;

CREATE TABLE IF NOT EXISTS `godwar_game_db`.`game_closure_account` (
  `GUID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_closure_reason` TEXT NOT NULL,
  `account_closure_starttime` INT(11) NOT NULL,
  `account_closure_endtime` INT(11) NOT NULL,
  PRIMARY KEY (`GUID`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `godwar_game_db`.`game_log_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godwar_game_db`.`game_log_account` ;

CREATE TABLE IF NOT EXISTS `godwar_game_db`.`game_log_account` (
  `log_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `log_account_id` BIGINT(20) NOT NULL,
  `log_account_name` CHAR(32) NOT NULL,
  `log_action` CHAR(64) NOT NULL,
  `log_parameter` TEXT NOT NULL,
  `log_time` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`log_id`),
  INDEX `log_GUID` USING BTREE (`log_account_id` ASC),
  INDEX `log_account_name` USING BTREE (`log_account_name` ASC),
  INDEX `log_time` USING BTREE (`log_time` ASC),
  INDEX `log_action` USING BTREE (`log_action` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `godwar_game_db`.`game_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godwar_game_db`.`game_order` ;

CREATE TABLE IF NOT EXISTS `godwar_game_db`.`game_order` (
  `funds_id` INT(11) NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NOT NULL,
  `funds_flow_dir` ENUM('CHECK_IN','CHECK_OUT') NOT NULL,
  `funds_amount` INT(11) NOT NULL,
  `funds_time` INT(11) NOT NULL,
  PRIMARY KEY (`funds_id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `godwar_game_db`.`game_card_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `godwar_game_db`.`game_card_group` ;

CREATE TABLE IF NOT EXISTS `godwar_game_db`.`game_card_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT NOT NULL,
  `group_name` CHAR(32) NOT NULL,
  `current` TINYINT NOT NULL DEFAULT 0,
  `card_list` TEXT NOT NULL,
  PRIMARY KEY (`group_id`))
ENGINE = InnoDB;

USE `pulse_platform_db` ;

-- -----------------------------------------------------
-- Table `pulse_platform_db`.`pulse_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pulse_platform_db`.`pulse_account` ;

CREATE TABLE IF NOT EXISTS `pulse_platform_db`.`pulse_account` (
  `GUID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_name` CHAR(32) NOT NULL DEFAULT '',
  `account_pass` CHAR(64) NOT NULL DEFAULT '',
  `account_email` CHAR(64) NOT NULL DEFAULT '',
  `account_nickname` CHAR(16) NOT NULL DEFAULT '',
  `account_secret_key` CHAR(128) NOT NULL DEFAULT '',
  `account_firstname` CHAR(32) NOT NULL DEFAULT '',
  `account_lastname` CHAR(32) NOT NULL DEFAULT '',
  `account_country` CHAR(16) NOT NULL DEFAULT '',
  `account_pass_question` CHAR(128) NOT NULL DEFAULT '',
  `account_pass_answer` CHAR(128) NOT NULL DEFAULT '',
  `account_point` INT(11) NOT NULL DEFAULT 0,
  `account_regtime` INT(11) NOT NULL DEFAULT 0,
  `account_lastlogin` INT(11) NOT NULL DEFAULT 0,
  `account_currentlogin` INT(11) NOT NULL DEFAULT 0,
  `account_lastip` CHAR(16) NOT NULL DEFAULT '',
  `account_currentip` CHAR(16) NOT NULL DEFAULT '',
  `account_status` TINYINT(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`GUID`),
  INDEX `account_name` (`account_name` ASC, `account_pass` ASC),
  UNIQUE INDEX `account_name1` (`account_name` ASC))
ENGINE = MyISAM
AUTO_INCREMENT = 10;


-- -----------------------------------------------------
-- Table `pulse_platform_db`.`pulse_closure_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pulse_platform_db`.`pulse_closure_account` ;

CREATE TABLE IF NOT EXISTS `pulse_platform_db`.`pulse_closure_account` (
  `GUID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_closure_reason` TEXT NULL DEFAULT NULL,
  `account_closure_starttime` INT(11) NOT NULL,
  `account_closure_endtime` INT(11) NOT NULL,
  PRIMARY KEY (`GUID`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pulse_platform_db`.`pulse_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pulse_platform_db`.`pulse_order` ;

CREATE TABLE IF NOT EXISTS `pulse_platform_db`.`pulse_order` (
  `funds_id` INT(11) NOT NULL AUTO_INCREMENT,
  `account_guid` BIGINT(20) NOT NULL,
  `game_id` INT(11) NULL DEFAULT NULL,
  `server_id` INT(11) NULL DEFAULT NULL,
  `funds_flow_dir` ENUM('CHECK_IN','CHECK_OUT') NOT NULL,
  `funds_amount` INT(11) NOT NULL,
  `funds_time` INT(11) NOT NULL,
  PRIMARY KEY (`funds_id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pulse_platform_db`.`pulse_product`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pulse_platform_db`.`pulse_product` ;

CREATE TABLE IF NOT EXISTS `pulse_platform_db`.`pulse_product` (
  `game_id` INT(11) NOT NULL,
  `game_name` CHAR(64) NOT NULL,
  `game_version` CHAR(16) NOT NULL,
  `game_platform` ENUM('web','ios','android') NOT NULL DEFAULT 'ios',
  `auth_key` CHAR(128) NOT NULL,
  `game_pic_small` TEXT NOT NULL,
  `game_pic_middium` TEXT NOT NULL,
  `game_pic_big` TEXT NOT NULL,
  `game_download_iphone` TEXT NOT NULL,
  `game_download_ipad` TEXT NOT NULL,
  `game_status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0=正式,1=内测,2=公测',
  PRIMARY KEY (`game_id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `pulse_platform_db`.`pulse_product`
-- -----------------------------------------------------
START TRANSACTION;
USE `pulse_platform_db`;
INSERT INTO `pulse_platform_db`.`pulse_product` (`game_id`, `game_name`, `game_version`, `game_platform`, `auth_key`, `game_pic_small`, `game_pic_middium`, `game_pic_big`, `game_download_iphone`, `game_download_ipad`, `game_status`) VALUES (1001, '黑暗轨迹', '1.0.0', 'web', 'bbc904d185bb824e5ae5eebf5cc831cf49f44b2b', '1', '1', '1', '1', '1', 0);

COMMIT;

