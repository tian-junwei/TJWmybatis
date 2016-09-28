/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : mybatis

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2016-09-20 23:28:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


存储过程

DELIMITER $
CREATE PROCEDURE mybatis.ges_user_count(IN age INT, OUT user_count INT)
BEGIN  
SELECT COUNT(*) FROM users WHERE users.age=age INTO user_count;
END 
$

DELIMITER ;
SET @user_count = 0;
CALL mybatis.ges_user_count(12, @user_count);
SELECT @user_count;

