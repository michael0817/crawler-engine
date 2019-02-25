/*
 Navicat Premium Data Transfer

 Source Server         : 爬虫
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 101.132.187.131:3306
 Source Schema         : crawler_db

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 25/02/2019 14:03:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for crawler_action_dynamic_info
-- ----------------------------
DROP TABLE IF EXISTS `crawler_action_dynamic_info`;
CREATE TABLE `crawler_action_dynamic_info`  (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `article_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主题编号',
  `article_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主题名称',
  `article_content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '内容',
  `order_num` int(11) NOT NULL COMMENT '排序编号',
  `oper_time` datetime(0) NOT NULL COMMENT '操作时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_action_info
-- ----------------------------
DROP TABLE IF EXISTS `crawler_action_info`;
CREATE TABLE `crawler_action_info`  (
  `action_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '爬取对象ID',
  `action_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬取对象描述',
  `action_type` int(11) NOT NULL COMMENT '爬取动作类型(1-点击;2-翻页;3-抓取cookie;4-抓取新闻)',
  `url_type` int(11) NOT NULL COMMENT 'url类型(1-html;2-json;3-txt)',
  `url_addr` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url地址',
  `base_url_addr` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '根url地址',
  `crawler_regex` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象选择器',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬虫执行动作配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_action_target_param
-- ----------------------------
DROP TABLE IF EXISTS `crawler_action_target_param`;
CREATE TABLE `crawler_action_target_param`  (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `param_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '参数类型(1-xpath;2-urlparam)',
  `param_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '参数名称',
  `param_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '参数值',
  PRIMARY KEY (`action_id`, `param_type`, `param_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬虫执行动作参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_content
-- ----------------------------
DROP TABLE IF EXISTS `crawler_content`;
CREATE TABLE `crawler_content`  (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `action_type` int(11) NULL DEFAULT NULL COMMENT '爬取动作类型',
  `crawler_date` date NOT NULL COMMENT '爬取日期',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬取内容存储表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_flow_detail
-- ----------------------------
DROP TABLE IF EXISTS `crawler_flow_detail`;
CREATE TABLE `crawler_flow_detail`  (
  `flow_id` int(11) NOT NULL COMMENT '爬取任务ID',
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `order_num` int(11) NOT NULL COMMENT '爬取顺序',
  PRIMARY KEY (`flow_id`, `action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬虫任务明细配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_flow_info
-- ----------------------------
DROP TABLE IF EXISTS `crawler_flow_info`;
CREATE TABLE `crawler_flow_info`  (
  `flow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '爬取任务ID',
  `flow_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '爬取任务描述',
  `flow_type` int(11) NOT NULL COMMENT '任务分类(1-新闻资讯;2-商品信息;3-金融数据)',
  `flow_schedule` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时设置',
  PRIMARY KEY (`flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬虫任务配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_log
-- ----------------------------
DROP TABLE IF EXISTS `crawler_log`;
CREATE TABLE `crawler_log`  (
  `flow_id` int(11) NOT NULL COMMENT '爬取任务ID',
  `action_id` int(11) NOT NULL COMMENT '爬取动作ID',
  `oper_time` datetime(0) NOT NULL COMMENT '操作时间',
  `result_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '执行结果代码',
  `result_message` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '执行结果描述',
  INDEX `flow_id`(`flow_id`, `action_id`, `oper_time`, `result_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for crawler_milestone
-- ----------------------------
DROP TABLE IF EXISTS `crawler_milestone`;
CREATE TABLE `crawler_milestone`  (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `crawler_date` date NOT NULL COMMENT '最后爬取日期',
  `milestone` varchar(8000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '里程碑',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '爬虫任务断点标志' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
