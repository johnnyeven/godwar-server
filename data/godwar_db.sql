SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema profzone_godwarv2_gamedb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `profzone_godwarv2_gamedb` ;
CREATE SCHEMA IF NOT EXISTS `profzone_godwarv2_gamedb` DEFAULT CHARACTER SET utf8 ;
USE `profzone_godwarv2_gamedb` ;

-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`role` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`role` (
  `role_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `game_guid` CHAR(36) NOT NULL DEFAULT '',
  `account_id` BIGINT(20) NOT NULL,
  `level` INT NOT NULL DEFAULT 1,
  `nick_name` CHAR(32) NOT NULL DEFAULT '',
  `role_picture` CHAR(16) NOT NULL DEFAULT '',
  `account_cash` BIGINT(20) NOT NULL DEFAULT 0,
  `direction` TINYINT NOT NULL DEFAULT 0,
  `action` TINYINT NOT NULL DEFAULT 0,
  `speed` INT NOT NULL DEFAULT 210,
  `honor` INT NOT NULL DEFAULT 0,
  `energy` INT NOT NULL DEFAULT 1000,
  `max_energy` INT NOT NULL DEFAULT 1000,
  `account_lastlogin` BIGINT NOT NULL DEFAULT 0,
  `account_lastlogout` BIGINT NOT NULL DEFAULT 0,
  `map_id` INT NOT NULL DEFAULT 1001,
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
  `role_id` BIGINT NOT NULL,
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
  `card_id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT NOT NULL,
  `resource_id` CHAR(32) NOT NULL,
  `name` CHAR(16) NOT NULL,
  `attack` INT NOT NULL,
  `def` INT NOT NULL,
  `mdef` INT NOT NULL,
  `health` INT NOT NULL,
  `energy` INT NOT NULL,
  `level` INT NOT NULL,
  `race` TINYINT NOT NULL,
  `skills` TEXT NOT NULL,
  PRIMARY KEY (`card_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_item` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_item` (
  `item_id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT NOT NULL,
  `original_id` INT NOT NULL,
  `name` CHAR(40) NOT NULL,
  `comment` TEXT NOT NULL,
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '1=�' /* comment truncated */ /*器
2=盔甲
3=头盔
4=鞋子
5=项链
6=戒指*/,
  `level` INT NOT NULL DEFAULT 1 COMMENT '物品等级',
  `grade` TINYINT NOT NULL DEFAULT 0 COMMENT '0=�' /* comment truncated */ /*通
1=蓝装
2=绿装
3=紫装
4=金装*/,
  `upgrade_level` INT NOT NULL DEFAULT 0 COMMENT '强化等级',
  `upgrade_level_max` INT NOT NULL DEFAULT 0 COMMENT '最高强化等级',
  `job` CHAR(20) NOT NULL DEFAULT '[]',
  `atk_base` INT NOT NULL DEFAULT 0 COMMENT '基础物理攻击',
  `def_base` INT NOT NULL DEFAULT 0 COMMENT '基础防御',
  `mdef_base` INT NOT NULL DEFAULT 0 COMMENT '基础魔抗',
  `health_max_base` INT NOT NULL DEFAULT 0 COMMENT '基础生命值',
  `hit_base` INT NOT NULL DEFAULT 0 COMMENT '基础命中',
  `flee_base` INT NOT NULL DEFAULT 0 COMMENT '基础闪避',
  `atk_inc` INT NOT NULL DEFAULT 0 COMMENT '攻击加成',
  `def_inc` INT NOT NULL DEFAULT 0 COMMENT '防御加成',
  `mdef_inc` INT NOT NULL DEFAULT 0 COMMENT '魔抗加成',
  `health_max_inc` INT NOT NULL DEFAULT 0 COMMENT '生命加成',
  `hit_inc` INT NOT NULL DEFAULT 0 COMMENT '命中加成',
  `flee_inc` INT NOT NULL DEFAULT 0 COMMENT '闪避加成',
  `atk_upgrade` INT NOT NULL DEFAULT 0 COMMENT '强化加成',
  `def_upgrade` INT NOT NULL DEFAULT 0,
  `mdef_upgrade` INT NOT NULL DEFAULT 0,
  `health_max_upgrade` INT NOT NULL DEFAULT 0,
  `hit_upgrade` INT NOT NULL DEFAULT 0,
  `flee_upgrade` INT NOT NULL DEFAULT 0,
  `magic_words` TEXT NOT NULL,
  `price` INT NOT NULL DEFAULT 0,
  `is_equipped` TINYINT NOT NULL DEFAULT 0,
  `is_locked` TINYINT NOT NULL DEFAULT 0,
  `count` TINYINT NOT NULL DEFAULT 1,
  `relative_item_id` BIGINT NOT NULL DEFAULT 0,
  `block_position` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`item_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `profzone_godwarv2_gamedb`.`game_instance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `profzone_godwarv2_gamedb`.`game_instance` ;

CREATE TABLE IF NOT EXISTS `profzone_godwarv2_gamedb`.`game_instance` (
  `role_id` BIGINT NOT NULL,
  `instance_id` BIGINT NOT NULL,
  `level` INT NOT NULL DEFAULT 1 COMMENT '第几关',
  PRIMARY KEY (`role_id`, `instance_id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
