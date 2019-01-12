# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 101.132.187.131 (MySQL 5.7.23-log)
# Database: crawler_db
# Generation Time: 2019-01-12 01:06:51 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table crawler_action_dynamic_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_action_dynamic_info`;

CREATE TABLE `crawler_action_dynamic_info` (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `article` varchar(200) DEFAULT '' COMMENT '主题',
  `content` varchar(200) NOT NULL DEFAULT '' COMMENT 'url地址',
  `order_num` int(11) NOT NULL COMMENT '排序编号',
  `oper_time` datetime NOT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table crawler_action_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_action_info`;

CREATE TABLE `crawler_action_info` (
  `action_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '爬取对象ID',
  `action_desc` varchar(100) DEFAULT NULL COMMENT '爬取对象描述',
  `action_type` int(11) NOT NULL COMMENT '爬取动作类型(1-点击;2-翻页;3-抓取cookie;4-抓取新闻)',
  `url_type` int(11) NOT NULL COMMENT 'url类型(1-html;2-json;3-txt)',
  `url_addr` varchar(200) DEFAULT NULL COMMENT 'url地址',
  `base_url_addr` varchar(200) DEFAULT NULL COMMENT '根url地址',
  `crawler_regex` varchar(200) DEFAULT NULL COMMENT '对象选择器',
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫执行动作配置表';



# Dump of table crawler_action_target_param
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_action_target_param`;

CREATE TABLE `crawler_action_target_param` (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `param_type` varchar(200) NOT NULL DEFAULT '' COMMENT '参数类型(1-xpath;2-urlparam)',
  `param_name` varchar(200) NOT NULL DEFAULT '' COMMENT '参数名称',
  `param_value` varchar(200) NOT NULL DEFAULT '' COMMENT '参数值',
  PRIMARY KEY (`action_id`,`param_type`,`param_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫执行动作参数配置表';



# Dump of table crawler_content
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_content`;

CREATE TABLE `crawler_content` (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `action_type` int(11) DEFAULT NULL COMMENT '爬取动作类型',
  `crawler_date` date NOT NULL COMMENT '爬取日期',
  `content` longtext NOT NULL COMMENT '内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬取内容存储表';



# Dump of table crawler_flow_detail
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_flow_detail`;

CREATE TABLE `crawler_flow_detail` (
  `flow_id` int(11) NOT NULL COMMENT '爬取任务ID',
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `order_num` int(11) NOT NULL COMMENT '爬取顺序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫任务明细配置表';



# Dump of table crawler_flow_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_flow_info`;

CREATE TABLE `crawler_flow_info` (
  `flow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '爬取任务ID',
  `flow_desc` varchar(200) DEFAULT NULL COMMENT '爬取任务描述',
  `flow_type` int(11) NOT NULL COMMENT '任务分类(1-新闻资讯;2-O2O数据;3-金融数据)',
  `flow_schedule` varchar(20) DEFAULT NULL COMMENT '定时设置',
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫任务配置表';



# Dump of table crawler_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_log`;

CREATE TABLE `crawler_log` (
  `flow_id` int(11) NOT NULL COMMENT '爬取任务ID',
  `action_id` int(11) NOT NULL COMMENT '爬取动作ID',
  `oper_time` datetime NOT NULL COMMENT '操作时间',
  `result_code` varchar(10) NOT NULL DEFAULT '' COMMENT '执行结果代码',
  `result_message` varchar(2000) NOT NULL DEFAULT '' COMMENT '执行结果描述',
  KEY `flow_id` (`flow_id`,`action_id`,`oper_time`,`result_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table crawler_milestone
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_milestone`;

CREATE TABLE `crawler_milestone` (
  `action_id` int(11) NOT NULL COMMENT '爬取对象ID',
  `crawler_date` date NOT NULL COMMENT '最后爬取日期',
  `milestone` varchar(200) NOT NULL DEFAULT '' COMMENT '里程碑',
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫任务断点标志';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
