-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema courseworlddb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `courseworlddb` ;

-- -----------------------------------------------------
-- Schema courseworlddb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `courseworlddb` DEFAULT CHARACTER SET utf8 ;
USE `courseworlddb` ;

-- -----------------------------------------------------
-- Table `courseworlddb`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`users` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(200) NOT NULL,
  `login` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`course` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `subject` VARCHAR(45) NULL DEFAULT NULL,
  `creator_id` INT NOT NULL,
  `price` DOUBLE NULL DEFAULT NULL,
  `rate` DOUBLE NULL DEFAULT NULL,
  `sum_rate` INT NULL DEFAULT NULL,
  `number_of_vote` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_course_users_idx` (`creator_id` ASC) VISIBLE,
  CONSTRAINT `fk_course_users`
    FOREIGN KEY (`creator_id`)
    REFERENCES `courseworlddb`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`hibernate_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`hibernate_sequence` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`hibernate_sequence` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`lecture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`lecture` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`lecture` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `theme` VARCHAR(45) NULL DEFAULT NULL,
  `content` LONGTEXT NULL DEFAULT NULL,
  `position` INT NULL DEFAULT NULL,
  `course_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_lecture_course1_idx` (`course_id` ASC) VISIBLE,
  CONSTRAINT `fk_lecture_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `courseworlddb`.`course` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`tests`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`tests` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`tests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(200) NULL DEFAULT NULL,
  `lecture_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_tests_lecture1_idx` (`lecture_id` ASC) VISIBLE,
  CONSTRAINT `fk_tests_lecture1`
    FOREIGN KEY (`lecture_id`)
    REFERENCES `courseworlddb`.`lecture` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`questions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`questions` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`questions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `question` VARCHAR(200) NOT NULL,
  `first_answer` VARCHAR(200) NULL DEFAULT NULL,
  `second_answer` VARCHAR(200) NULL DEFAULT NULL,
  `third_answer` VARCHAR(200) NULL DEFAULT NULL,
  `fourth_answer` VARCHAR(200) NULL DEFAULT NULL,
  `correct_answer` VARCHAR(200) NULL DEFAULT NULL,
  `description` VARCHAR(200) NULL DEFAULT NULL,
  `tests_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_questions_tests1_idx` (`tests_id` ASC) VISIBLE,
  CONSTRAINT `fk_questions_tests1`
    FOREIGN KEY (`tests_id`)
    REFERENCES `courseworlddb`.`tests` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `courseworlddb`.`subscription`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`subscription` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`subscription` (
  `users_id` INT NOT NULL,
  `course_id` INT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  `current_mark` INT NOT NULL,
  `voted` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_users_has_course_course1_idx` (`course_id` ASC) VISIBLE,
  INDEX `fk_users_has_course_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_has_course_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `courseworlddb`.`course` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_users_has_course_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `courseworlddb`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `courseworlddb`.`users_has_test`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `courseworlddb`.`users_has_test` ;

CREATE TABLE IF NOT EXISTS `courseworlddb`.`users_has_test`
(
    `tests_id` INT NOT NULL,
    `users_id` INT NOT NULL,
    `id`       INT NOT NULL AUTO_INCREMENT,
    `mark`     INT NOT NULL,
    `attempts` INT NOT NULL,
    INDEX `fk_tests_has_users_users1_idx` (`users_id` ASC) VISIBLE,
    INDEX `fk_tests_has_users_tests1_idx` (`tests_id` ASC) VISIBLE,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_tests_has_users_tests1`
        FOREIGN KEY (`tests_id`)
            REFERENCES `courseworlddb`.`tests` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_tests_has_users_users1`
        FOREIGN KEY (`users_id`)
            REFERENCES `courseworlddb`.`users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARACTER SET = utf8;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO users VALUES(default, 'password', 'login', 'ROLE_USER', 'description');
INSERT INTO users VALUES(default, 'password', 'username', 'ROLE_USER', 'post works');
INSERT INTO course VALUES(default, 'course1', 'description1', 'subject1', 1, 0, 0,0,0);
INSERT INTO course VALUES(default, 'course2', 'description2', 'subject2', 1, 0, 0,0,0);
INSERT INTO course VALUES(default, 'course3', 'description3', 'subject3', 1, 13.99, 0,0,0);
INSERT INTO course VALUES(default, 'course4', 'description4', 'subject4', 1, 20, 0,0,0);
INSERT INTO subscription VALUES(2,1,'In progress',DEFAULT,0,0);
INSERT INTO lecture VALUES(default, 'lecture1', 'theme', 'content', 1, 1);
INSERT INTO lecture VALUES(default, 'lecture2', 'theme', 'content', 2, 1);
INSERT INTO lecture VALUES(default, 'lecture3', 'theme', 'content', 3, 1);
INSERT INTO lecture VALUES(default, 'lecture4', 'theme', 'content', 4, 1);
INSERT INTO lecture VALUES(default, 'lecture5', 'theme', 'content', 5, 1);
