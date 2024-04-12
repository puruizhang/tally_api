/*
 Navicat Premium Data Transfer

 Source Server         : 139.224.189.67
 Source Server Type    : MySQL
 Source Server Version : 110302 (11.3.2-MariaDB-1:11.3.2+maria~ubu2204)
 Source Host           : 139.224.189.67:3306
 Source Schema         : tally

 Target Server Type    : MySQL
 Target Server Version : 110302 (11.3.2-MariaDB-1:11.3.2+maria~ubu2204)
 File Encoding         : 65001

 Date: 12/04/2024 09:35:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_room
-- ----------------------------
DROP TABLE IF EXISTS `t_room`;
CREATE TABLE `t_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) DEFAULT NULL,
  `room_status` int(11) DEFAULT NULL,
  `room_code_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_transfer_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_transfer_detail`;
CREATE TABLE `t_transfer_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `room_id` int(11) DEFAULT NULL COMMENT '房间ID',
  `payer_user_id` int(11) DEFAULT NULL COMMENT '付款人ID',
  `payee_user_id` int(11) DEFAULT NULL COMMENT '收款人ID',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '转账金额',
  `create_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '转账创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for t_user_room
-- ----------------------------
DROP TABLE IF EXISTS `t_user_room`;
CREATE TABLE `t_user_room` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(10) DEFAULT NULL COMMENT '用户id',
  `room_id` bigint(10) DEFAULT NULL COMMENT '房间id',
  `status` tinyint(1) DEFAULT NULL COMMENT '0:进行中 1:已经结算',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `victory_flag` tinyint(1) DEFAULT NULL COMMENT '0:负 1:胜利',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
