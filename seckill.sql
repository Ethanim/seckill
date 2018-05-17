/*
Navicat MySQL Data Transfer

Source Server         : a
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : seckill

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-05-17 22:28:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL,
  `goods_name` varchar(30) DEFAULT NULL,
  `goods_title` varchar(64) DEFAULT NULL,
  `goods_img` varchar(64) DEFAULT NULL,
  `goods_detail` longtext,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `goods_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', 'iphoneX', 'Apple/苹果iPhone X 全网通4G手机苹果X 10', '/img/iphonex.png', 'Apple/苹果iPhone X 全网通4G手机苹果X 10', '7788.00', '100');
INSERT INTO `goods` VALUES ('2', '华为 Mate 10', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', '/img/meta10.png', 'Huawei/华为 Mate 10 6G+128G 全网通4G智能手机', '4199.00', '50');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL,
  `goods_name` varchar(30) DEFAULT NULL,
  `goods_count` int(11) DEFAULT NULL,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `order_channel` tinyint(4) DEFAULT NULL COMMENT '订单渠道，1在线，2android，3ios',
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('6', '15812341234', '1', '0', 'iphoneX', '1', '0.01', '1', '0', '2018-05-01 12:31:03', null);
INSERT INTO `order_info` VALUES ('7', '15812341234', '2', '0', '华为 Mate 10', '1', '0.01', '1', '0', '2018-05-01 14:09:42', null);

-- ----------------------------
-- Table structure for s_goods
-- ----------------------------
DROP TABLE IF EXISTS `s_goods`;
CREATE TABLE `s_goods` (
  `id` bigint(20) NOT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `seckill_price` decimal(10,2) DEFAULT NULL,
  `stock_count` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_goods
-- ----------------------------
INSERT INTO `s_goods` VALUES ('1', '1', '0.01', '7', '2018-05-01 10:22:50', '2018-05-02 10:23:00');
INSERT INTO `s_goods` VALUES ('2', '2', '0.01', '9', '2018-04-29 22:56:10', '2018-05-01 22:56:15');

-- ----------------------------
-- Table structure for s_order
-- ----------------------------
DROP TABLE IF EXISTS `s_order`;
CREATE TABLE `s_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_order
-- ----------------------------
INSERT INTO `s_order` VALUES ('3', '15812341234', '1', '1');
INSERT INTO `s_order` VALUES ('4', '15812341234', '1', '2');

-- ----------------------------
-- Table structure for s_user
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
  `id` bigint(20) unsigned NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES ('15812341234', 'jack', 'b631975e482e55b7692106f55a5b0a82', '1a2b3c', null, '2018-04-30 11:10:36', '2018-04-30 11:10:40', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'tom');
INSERT INTO `user` VALUES ('2', 'jack');
