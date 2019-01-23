# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 101.132.187.131 (MySQL 5.7.23-log)
# Database: crawler_db
# Generation Time: 2019-01-23 06:02:04 +0000
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

LOCK TABLES `crawler_action_info` WRITE;
/*!40000 ALTER TABLE `crawler_action_info` DISABLE KEYS */;

INSERT INTO `crawler_action_info` (`action_id`, `action_desc`, `action_type`, `url_type`, `url_addr`, `base_url_addr`, `crawler_regex`)
VALUES
	(1,'政策法规-目录',2,1,'/chinese/newListDoc/111003/{1~2}.html','http://www.cbrc.gov.cn','//table[@id=\'testUI\']//a[@target=\'_blank\']'),
	(2,'政策法规-资讯',4,1,'[action_id_1]','http://www.cbrc.gov.cn','/html/head/title/text()||||/html/body//div[@id=\'docTitle\']/div[1]/text()||/html/body//div[@class=\'Section0\']/p/allText()'),
	(3,'监管动态-目录',2,1,'/chinese/newListDoc/111005/{1~2}.html','http://www.cbrc.gov.cn','//table[@id=\'testUI\']//a[@target=\'_blank\']'),
	(4,'监管动态-资讯',4,1,'[action_id_3]','http://www.cbrc.gov.cn','/html/head/title/text()||||/html/body//div[@id=\'docTitle\']/div[1]/text()||/html/body//div[@class=\'Section0\']/p/allText()'),
	(5,'政策法规-目录',2,1,'/nifa/2955686/2955720/e9b7b132/index{1~2}.html','http://www.nifa.org.cn','//td[@id=\'list\']//a[@target=\'_blank\']'),
	(6,'政策法规-资讯',4,1,'[action_id_5]','http://www.nifa.org.cn','/html/head/title/text()||/html/body//td[@id=\'zhengwen\']//td[@class=\'zi8\']/text()||||/html/body//td[@id=\'zhengwen\']//tbody/tr[2]/td/p/allText()'),
	(7,'财经要闻-目录',2,1,'/nifa/2961652/2961656/c965ca1a/index{1~2}.html','http://www.nifa.org.cn','//td[@id=\'list\']//a[@target=\'_blank\']'),
	(8,'财经要闻-资讯',4,1,'[action_id_7]','http://www.nifa.org.cn','/html/head/title/text()||/html/body//td[@id=\'zhengwen\']//td[@class=\'zi8\']/text()||||/html/body//td[@id=\'zhengwen\']//tbody/tr[2]/td/p/allText()'),
	(9,'银行动态-目录',2,1,'/news/search.php?id=3&p={1~2}','http://www.51kaxun.com','//ul[@id=\'news_list\']//a'),
	(10,'银行动态-资讯',4,1,'[action_id_9]','http://www.51kaxun.com','/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()'),
	(11,'专家点评-目录',2,1,'/news/search.php?id=4&p={1~2}','http://www.51kaxun.com','//ul[@id=\'news_list\']//a'),
	(12,'专家点评-资讯',4,1,'[action_id_11]','http://www.51kaxun.com','/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()'),
	(13,'新卡快报-目录',2,1,'/news/search.php?id=5&p={1~2}','http://www.51kaxun.com','//ul[@id=\'news_list\']//a'),
	(14,'新卡快报-资讯',4,1,'[action_id_13]','http://www.51kaxun.com','/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()'),
	(15,'网贷要闻-目录',2,1,'','https://www.wdzj.com','//div[@class=\'tabclist on\']/ul[@class=\'newslist\']//a[@title]'),
	(16,'网贷要闻-资讯',4,1,'[action_id_15]','https://www.wdzj.com','/html/body//h1[@class=\'page-title\']/text()||/html/body//div[@class=\'page-time\']/span[2]/text()||/html/body//div[@class=\'page-summary\']/div/text()||/html/body//div[@class=\'page-content\']/tidyText()'),
	(17,'艾瑞金融-目录',2,2,'products/GetReportList?classId=70&fee=0&date=&lastId=','http://www.iresearch.com.cn','//body/text()||$..VisitUrl'),
	(18,'艾瑞金融-资讯',4,1,'[action_id_17]','http://www.iresearch.com.cn',NULL),
	(19,'易观金融-目录',2,1,'/article/analysis/{1~2}?keyword=金融','https://www.analysys.cn','//li/div/a/@href'),
	(20,'易观金融-资讯',4,1,'[action_id_19]','https://www.analysys.cn','p[@id=\'anal_icon\']/span[3]/text(),h1[@class=\'flf\']/text(),div[@class=\'detail_left\']'),
	(21,'新流财经-目录',2,1,'/author/iconfin.html?page={1~2}&per-page=10','http://www.bugutime.com','//ul/li/div[@class=\'item-left\']//a/@href'),
	(22,'新流财经-资讯',4,1,'[action_id_21]','http://www.bugutime.com','time/text(),h1[@class=\'view-title\']/text(),div[@class=\'wrap\']'),
	(23,'雪球-COOKIE',2,1,'','https://xueqiu.com',NULL),
	(24,'雪球金融-目录',2,2,'/v4/statuses/user_timeline.json?user_id=7558914709&page={1~2}','https://xueqiu.com','//body/text()||$. statuses..target'),
	(25,'雪球金融-资讯',4,1,'[action_id_23]','https://xueqiu.com','a[@class=\'time\']/text(),h1[@class=\'article__bd__title\']/text(),div[@id=\'app\']'),
	(26,'虎嗅微语-目录',2,1,'/member/1453857/article/{1~2}.html','https://www.huxiu.com','/article/\\d+\\.html\\?f=member_article'),
	(27,'虎嗅微语-目录',4,1,'[action_id_25]','https://www.huxiu.com','span[@class=\'article-time\']/text(),h1[@class=\'t-h1\']/text(),div[@class=\'article-section-wrap\']'),
	(28,'招行掌上商城-一级目录',5,1,'/_CL5_/Category/GetAllCategories','https://ssl.mall.cmbchina.com','//div[@id=\'divC1Panel\']//li/a'),
	(29,'招行掌上商城-二级目录',6,1,'/_CL5_/Category/GetCategories?id={5~14}','https://ssl.mall.cmbchina.com','//dl[@class=\'catalog_list\']//dd/a'),
	(30,'招行掌上商城-商品目录',7,1,'/_CL5_/Product/ProductListAjaxLoad?subCategory=[action_id_29]&navigationKey=&sort=70&pageIndex={1~999}',NULL,NULL),
	(31,'招行掌上商城-手机目录',7,1,'/_CL5_/Product/ProductListAjaxLoad?subCategory=370&navigationKey=&sort=70&pageIndex={1~999}','https://ssl.mall.cmbchina.com',NULL);

/*!40000 ALTER TABLE `crawler_action_info` ENABLE KEYS */;
UNLOCK TABLES;


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

LOCK TABLES `crawler_action_target_param` WRITE;
/*!40000 ALTER TABLE `crawler_action_target_param` DISABLE KEYS */;

INSERT INTO `crawler_action_target_param` (`action_id`, `param_type`, `param_name`, `param_value`)
VALUES
	(15,'MILESTONE','SAVE_TYPE','ALL'),
	(16,'MILESTONE','SAVE_TYPE','ALL');

/*!40000 ALTER TABLE `crawler_action_target_param` ENABLE KEYS */;
UNLOCK TABLES;


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

LOCK TABLES `crawler_flow_detail` WRITE;
/*!40000 ALTER TABLE `crawler_flow_detail` DISABLE KEYS */;

INSERT INTO `crawler_flow_detail` (`flow_id`, `action_id`, `order_num`)
VALUES
	(1,1,1),
	(1,2,2),
	(2,3,1),
	(2,4,2),
	(3,5,1),
	(3,6,2),
	(4,7,1),
	(4,8,2),
	(5,9,1),
	(5,10,2),
	(6,11,1),
	(6,12,2),
	(7,13,1),
	(7,14,2),
	(8,15,1),
	(8,16,2),
	(9,17,1),
	(9,18,2),
	(10,19,1),
	(10,20,2),
	(11,21,1),
	(11,22,2),
	(12,23,1),
	(12,24,2),
	(13,26,1),
	(13,27,2),
	(14,28,1),
	(14,29,2),
	(12,25,3);

/*!40000 ALTER TABLE `crawler_flow_detail` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table crawler_flow_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `crawler_flow_info`;

CREATE TABLE `crawler_flow_info` (
  `flow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '爬取任务ID',
  `flow_desc` varchar(200) DEFAULT NULL COMMENT '爬取任务描述',
  `flow_type` int(11) NOT NULL COMMENT '任务分类(1-新闻资讯;2-商品信息;3-金融数据)',
  `flow_schedule` varchar(20) DEFAULT NULL COMMENT '定时设置',
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫任务配置表';

LOCK TABLES `crawler_flow_info` WRITE;
/*!40000 ALTER TABLE `crawler_flow_info` DISABLE KEYS */;

INSERT INTO `crawler_flow_info` (`flow_id`, `flow_desc`, `flow_type`, `flow_schedule`)
VALUES
	(1,'银保监_政策法规',1,'OFF'),
	(2,'银保监_监管动态',1,'OFF'),
	(3,'中国互联网金融协会_政策法规',1,''),
	(4,'中国互联网金融协会_财经要闻',1,''),
	(5,'卡讯网_银行动态',1,'OFF'),
	(6,'卡讯网_专家点评',1,'OFF'),
	(7,'卡讯网_新卡快报',1,'OFF'),
	(8,'网贷之家_要闻',1,'OFF'),
	(9,'艾瑞咨询_ 金融',1,'OFF'),
	(10,'易观金融',1,'OFF'),
	(11,'新流财经_公众号',1,'OFF'),
	(12,'雪球网_金融行业资讯',1,'OFF'),
	(13,'虎嗅_洪言微语',1,'OFF'),
	(14,'招行掌上商城_手机',2,'OFF');

/*!40000 ALTER TABLE `crawler_flow_info` ENABLE KEYS */;
UNLOCK TABLES;


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
  `milestone` varchar(8000) NOT NULL DEFAULT '' COMMENT '里程碑',
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫任务断点标志';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
