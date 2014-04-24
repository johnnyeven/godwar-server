SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`role` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`role` (
  `role_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NOT NULL,
  `level` INT NOT NULL DEFAULT 1,
  `nick_name` CHAR(32) NOT NULL DEFAULT '',
  `role_picrutre` CHAR(16) NOT NULL DEFAULT '',
  `account_cash` BIGINT(20) NOT NULL DEFAULT 0,
  `direction` TINYINT NOT NULL DEFAULT 0,
  `action` TINYINT NOT NULL DEFAULT 0,
  `speed` INT NOT NULL DEFAULT 10,
  `honor` INT NOT NULL DEFAULT 0,
  `energy` INT NOT NULL DEFAULT 1000,
  `max_energy` INT NOT NULL DEFAULT 1000,
  `account_lastlogin` BIGINT NOT NULL DEFAULT 0,
  `account_lastlogout` BIGINT NOT NULL DEFAULT 0,
  `map_id` INT NOT NULL DEFAULT 0,
  `x` INT NOT NULL DEFAULT 0,
  `y` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`role_id`),
  INDEX `guid` (`role_id` ASC))
ENGINE = MyISAM;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_closure_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_closure_account` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_closure_account` (
  `GUID` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `account_closure_reason` TEXT NOT NULL,
  `account_closure_starttime` INT(11) NOT NULL,
  `account_closure_endtime` INT(11) NOT NULL,
  PRIMARY KEY (`GUID`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_log_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_log_account` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_log_account` (
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
-- Table `profzone_godwarv2_gamedb`.`game_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_order` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_order` (
  `funds_id` INT(11) NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT(20) NOT NULL,
  `funds_flow_dir` ENUM('CHECK_IN','CHECK_OUT') NOT NULL,
  `funds_amount` INT(11) NOT NULL,
  `funds_time` INT(11) NOT NULL,
  PRIMARY KEY (`funds_id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_card_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_card_group` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_card_group` (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `account_id` BIGINT NOT NULL,
  `group_name` CHAR(32) NOT NULL,
  `current` TINYINT NOT NULL DEFAULT 0,
  `card_list` TEXT NOT NULL,
  PRIMARY KEY (`group_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_card`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_card` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_card` (
  `account_id` BIGINT NOT NULL,
  `card_list` TEXT NOT NULL,
  `hero_card_list` TEXT NOT NULL,
  PRIMARY KEY (`account_id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
