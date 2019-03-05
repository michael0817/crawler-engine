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

 Date: 25/02/2019 14:57:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Records of crawler_action_info
-- ----------------------------
INSERT INTO `crawler_action_info` VALUES (1, '政策法规-目录', 2, 1, '/chinese/newListDoc/111003/{1~2}.html', 'http://www.cbrc.gov.cn', '//table[@id=\'testUI\']//a[@target=\'_blank\']/@href||//table[@id=\'testUI\']//a[@target=\'_blank\']/text()||//table[@id=\'testUI\']//a[@target=\'_blank\']');
INSERT INTO `crawler_action_info` VALUES (2, '政策法规-资讯', 4, 1, '[action_id_1]', 'http://www.cbrc.gov.cn', '/html/head/title/text()||||/html/body//div[@id=\'docTitle\']/div[1]/text()||/html/body//div[@class=\'Section0\']/p/tidyText()');
INSERT INTO `crawler_action_info` VALUES (3, '监管动态-目录', 2, 1, '/chinese/newListDoc/111005/{1~2}.html', 'http://www.cbrc.gov.cn', '//table[@id=\'testUI\']//a[@target=\'_blank\']/@href||//table[@id=\'testUI\']//a[@target=\'_blank\']/text()||//table[@id=\'testUI\']//a[@target=\'_blank\']');
INSERT INTO `crawler_action_info` VALUES (4, '监管动态-资讯', 4, 1, '[action_id_3]', 'http://www.cbrc.gov.cn', '/html/head/title/text()||||/html/body//div[@id=\'docTitle\']/div[1]/text()||/html/body//div[@class=\'Section0\']/p/tidyText()');
INSERT INTO `crawler_action_info` VALUES (5, '政策法规-目录', 2, 1, '/nifa/2955686/2955720/e9b7b132/index{1~2}.html', 'http://www.nifa.org.cn', '//td[@id=\'list\']//a[@target=\'_blank\']/@href||//td[@id=\'list\']//a[@target=\'_blank\']/text()||//td[@id=\'list\']//a[@target=\'_blank\']');
INSERT INTO `crawler_action_info` VALUES (6, '政策法规-资讯', 4, 1, '[action_id_5]', 'http://www.nifa.org.cn', '/html/head/title/text()||/html/body//td[@id=\'zhengwen\']//td[@class=\'zi8\']/text()||||/html/body//td[@id=\'zhengwen\']//tbody/tr[2]/td/p/tidyText()');
INSERT INTO `crawler_action_info` VALUES (7, '财经要闻-目录', 2, 1, '/nifa/2961652/2961656/c965ca1a/index{1~2}.html', 'http://www.nifa.org.cn', '//td[@id=\'list\']//a[@target=\'_blank\']/@href||//td[@id=\'list\']//a[@target=\'_blank\']/text()||//td[@id=\'list\']//a[@target=\'_blank\']');
INSERT INTO `crawler_action_info` VALUES (8, '财经要闻-资讯', 4, 1, '[action_id_7]', 'http://www.nifa.org.cn', '/html/head/title/text()||/html/body//td[@id=\'zhengwen\']//td[@class=\'zi8\']/text()||||/html/body//td[@id=\'zhengwen\']//tbody/tr[2]/td/p/tidyText()');
INSERT INTO `crawler_action_info` VALUES (9, '银行动态-目录', 2, 1, '/news/search.php?id=3&p={1~2}', 'http://www.51kaxun.com', '//ul[@id=\'news_list\']//a/@href||//ul[@id=\'news_list\']//a/text()||//ul[@id=\'news_list\']//a');
INSERT INTO `crawler_action_info` VALUES (10, '银行动态-资讯', 4, 1, '[action_id_9]', 'http://www.51kaxun.com', '/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (11, '专家点评-目录', 2, 1, '/news/search.php?id=4&p={1~2}', 'http://www.51kaxun.com', '//ul[@id=\'news_list\']//a/@href||//ul[@id=\'news_list\']//a/text()||//ul[@id=\'news_list\']//a');
INSERT INTO `crawler_action_info` VALUES (12, '专家点评-资讯', 4, 1, '[action_id_11]', 'http://www.51kaxun.com', '/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (13, '新卡快报-目录', 2, 1, '/news/search.php?id=5&p={1~2}', 'http://www.51kaxun.com', '//ul[@id=\'news_list\']//a/@href||//ul[@id=\'news_list\']//a/text()||//ul[@id=\'news_list\']//a');
INSERT INTO `crawler_action_info` VALUES (14, '新卡快报-资讯', 4, 1, '[action_id_13]', 'http://www.51kaxun.com', '/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (15, '网贷要闻-目录', 2, 1, '', 'https://www.wdzj.com', '//div[@class=\'tabclist on\']/ul[@class=\'newslist\']//a[@title]/@href||//div[@class=\'tabclist on\']/ul[@class=\'newslist\']//a[@title]/text()||//div[@class=\'tabclist on\']/ul[@class=\'newslist\']//a[@title]');
INSERT INTO `crawler_action_info` VALUES (16, '网贷要闻-资讯', 4, 1, '[action_id_15]', 'https://www.wdzj.com', '/html/body//h1[@class=\'page-title\']/text()||/html/body//div[@class=\'page-time\']/span[2]/text()||/html/body//div[@class=\'page-summary\']/div/text()||/html/body//div[@class=\'page-content\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (17, '艾瑞金融-目录', 2, 2, 'products/GetReportList?classId=70&fee=0&date=&lastId=', 'http://www.iresearch.com.cn', '//body/text()||$..VisitUrl');
INSERT INTO `crawler_action_info` VALUES (18, '艾瑞金融-资讯', 4, 1, '[action_id_17]', 'http://www.iresearch.com.cn', NULL);
INSERT INTO `crawler_action_info` VALUES (19, '易观金融-目录', 2, 1, '/article/analysis/{1~2}?keyword=金融', 'https://www.analysys.cn', '//li/div/a/@href');
INSERT INTO `crawler_action_info` VALUES (20, '易观金融-资讯', 4, 1, '[action_id_19]', 'https://www.analysys.cn', 'p[@id=\'anal_icon\']/span[3]/text(),h1[@class=\'flf\']/text(),div[@class=\'detail_left\']');
INSERT INTO `crawler_action_info` VALUES (21, '新流财经-目录', 2, 1, '/author/iconfin.html?page={1~2}&per-page=10', 'http://www.bugutime.com', '//ul/li/div[@class=\'item-left\']//a/@href');
INSERT INTO `crawler_action_info` VALUES (22, '新流财经-资讯', 4, 1, '[action_id_21]', 'http://www.bugutime.com', 'time/text(),h1[@class=\'view-title\']/text(),div[@class=\'wrap\']');
INSERT INTO `crawler_action_info` VALUES (23, '雪球-COOKIE', 2, 1, '', 'https://xueqiu.com', NULL);
INSERT INTO `crawler_action_info` VALUES (24, '雪球金融-目录', 2, 2, '/v4/statuses/user_timeline.json?user_id=7558914709&page={1~2}', 'https://xueqiu.com', '//body/text()||$. statuses..target');
INSERT INTO `crawler_action_info` VALUES (25, '雪球金融-资讯', 4, 1, '[action_id_23]', 'https://xueqiu.com', 'a[@class=\'time\']/text(),h1[@class=\'article__bd__title\']/text(),div[@id=\'app\']');
INSERT INTO `crawler_action_info` VALUES (26, '虎嗅微语-目录', 2, 1, '/member/1453857/article/{1~2}.html', 'https://www.huxiu.com', '/article/\\d+\\.html\\?f=member_article');
INSERT INTO `crawler_action_info` VALUES (27, '虎嗅微语-目录', 4, 1, '[action_id_25]', 'https://www.huxiu.com', 'span[@class=\'article-time\']/text(),h1[@class=\'t-h1\']/text(),div[@class=\'article-section-wrap\']');
INSERT INTO `crawler_action_info` VALUES (28, '招行掌上商城-一级目录', 2, 1, '/_CL5_/Category/GetAllCategories', 'https://ssl.mall.cmbchina.com', '//div[@id=\'divC1Panel\']//li/a/@data_id||//div[@id=\'divC1Panel\']//li/a/text()//div[@id=\'divC1Panel\']//li/a');
INSERT INTO `crawler_action_info` VALUES (29, '招行掌上商城-二级目录', 2, 1, '/_CL5_/Category/GetCategories?id=[action_id_28]', 'https://ssl.mall.cmbchina.com', '//dl[@class=\'catalog_list\']//dd/a/@c3id||//dl[@class=\'catalog_list\']//dd/a/text||//dl[@class=\'catalog_list\']//dd/a');
INSERT INTO `crawler_action_info` VALUES (30, '招行掌上商城-商品目录', 2, 1, '/_CL5_/Product/ProductListAjaxLoad?subCategory=[action_id_29]&navigationKey=&sort=70&pageIndex={1~999}', 'https://ssl.mall.cmbchina.com', '//div[@class=\'item-detail\']//p[@class=\'clearfix\']/span[@class=\'fr grand-total\']/@productid||//div[@class=\'item-detail\']/h4/text()||//div[@class=\'item-detail\']/a');
INSERT INTO `crawler_action_info` VALUES (31, '招行掌上商城-手机目录', 7, 1, '/_CL5_/Product/ProductListAjaxLoad?subCategory=370&navigationKey=&sort=70&pageIndex={1~999}', 'https://ssl.mall.cmbchina.com', NULL);
INSERT INTO `crawler_action_info` VALUES (32, '焦点新闻-目录', 2, 1, '/news', 'http://www.51kaxun.com', '//div[@id=\'news_top\']//ul//a/@href||//div[@id=\'news_top\']//ul//a/text()||//div[@id=\'news_top\']//ul//a');
INSERT INTO `crawler_action_info` VALUES (33, '焦点新闻-资讯', 4, 1, '[action_id_32]', 'http://www.51kaxun.com', '/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (34, '业界新闻-目录', 2, 1, '/news/search.php?id=1&p={1~2}', 'http://www.51kaxun.com', '//ul[@id=\'news_list\']//a/@href||//ul[@id=\'news_list\']//a/text()||//ul[@id=\'news_list\']//a');
INSERT INTO `crawler_action_info` VALUES (35, '业界新闻-资讯', 4, 1, '[action_id_34]', 'http://www.51kaxun.com', '/html/body//h1[@class=\'nr01_tit\']/text()||/html/body//div[@class=\'yhym\']/p[1]/text(2)||||/html/body//div[@class=\'right01_nr\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (36, '网贷行业-目录', 2, 1, '/news', 'https://www.wdzj.com', '//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[2]//ul//div[@class=\'text\']//h3//a/@href||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[2]//ul//div[@class=\'text\']//h3//a/text()||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[2]//ul//div[@class=\'text\']//h3//a');
INSERT INTO `crawler_action_info` VALUES (37, '网贷行业-资讯', 4, 1, '[action_id_36]', 'https://www.wdzj.com', '/html/body//h1[@class=\'page-title\']/text()||/html/body//div[@class=\'page-time\']/allText()||/html/body//div[@class=\'page-summary\']/div/text()||/html/body//div[@class=\'page-content\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (38, '平台动态-目录', 2, 1, '/news', 'https://www.wdzj.com', '//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[4]//ul//div[@class=\'text\']//h3//a/@href||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[4]//ul//div[@class=\'text\']//h3//a/text()||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[4]//ul//div[@class=\'text\']//h3//a');
INSERT INTO `crawler_action_info` VALUES (39, '平台动态-资讯', 4, 1, '[action_id_38]', 'https://www.wdzj.com', '/html/body//h1[@class=\'page-title\']/text()||/html/body//div[@class=\'page-time\']/allText()||/html/body//div[@class=\'page-summary\']/div/text()||/html/body//div[@class=\'page-content\']/tidyText()');
INSERT INTO `crawler_action_info` VALUES (40, '互联网金融-目录', 2, 1, '/news', 'https://www.wdzj.com', '//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[7]//ul//div[@class=\'text\']//h3//a/@href||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[7]//ul//div[@class=\'text\']//h3//a/text()||//div[@id=\'nav-menu\']//div[@class=\'tab-cont\']//div[7]//ul//div[@class=\'text\']//h3//a');
INSERT INTO `crawler_action_info` VALUES (41, '互联网金融-资讯', 4, 1, '[action_id_40]', 'https://www.wdzj.com', '/html/body//h1[@class=\'page-title\']/text()||/html/body//div[@class=\'page-time\']/allText()||/html/body//div[@class=\'page-summary\']/div/text()||/html/body//div[@class=\'page-content\']/tidyText()');

-- ----------------------------
-- Records of crawler_action_target_param
-- ----------------------------
INSERT INTO `crawler_action_target_param` VALUES (10, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (12, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (14, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (15, 'MILESTONE', 'SAVE_TYPE', 'ALL');
INSERT INTO `crawler_action_target_param` VALUES (16, 'MILESTONE', 'SAVE_TYPE', 'ALL');
INSERT INTO `crawler_action_target_param` VALUES (33, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (35, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (37, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (39, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');
INSERT INTO `crawler_action_target_param` VALUES (41, 'FILTER', 'KEYWORD', '花呗,借呗,支付,财付通,消费金融,消金行业,阿里,腾讯,营业收入,净利润,征信');

-- ----------------------------
-- Records of crawler_flow_detail
-- ----------------------------
INSERT INTO `crawler_flow_detail` VALUES (1, 1, 1);
INSERT INTO `crawler_flow_detail` VALUES (1, 2, 2);
INSERT INTO `crawler_flow_detail` VALUES (2, 3, 1);
INSERT INTO `crawler_flow_detail` VALUES (2, 4, 2);
INSERT INTO `crawler_flow_detail` VALUES (3, 5, 1);
INSERT INTO `crawler_flow_detail` VALUES (3, 6, 2);
INSERT INTO `crawler_flow_detail` VALUES (4, 7, 1);
INSERT INTO `crawler_flow_detail` VALUES (4, 8, 2);
INSERT INTO `crawler_flow_detail` VALUES (5, 9, 1);
INSERT INTO `crawler_flow_detail` VALUES (5, 10, 2);
INSERT INTO `crawler_flow_detail` VALUES (6, 11, 1);
INSERT INTO `crawler_flow_detail` VALUES (6, 12, 2);
INSERT INTO `crawler_flow_detail` VALUES (7, 13, 1);
INSERT INTO `crawler_flow_detail` VALUES (7, 14, 2);
INSERT INTO `crawler_flow_detail` VALUES (8, 15, 1);
INSERT INTO `crawler_flow_detail` VALUES (8, 16, 2);
INSERT INTO `crawler_flow_detail` VALUES (9, 17, 1);
INSERT INTO `crawler_flow_detail` VALUES (9, 18, 2);
INSERT INTO `crawler_flow_detail` VALUES (10, 19, 1);
INSERT INTO `crawler_flow_detail` VALUES (10, 20, 2);
INSERT INTO `crawler_flow_detail` VALUES (11, 21, 1);
INSERT INTO `crawler_flow_detail` VALUES (11, 22, 2);
INSERT INTO `crawler_flow_detail` VALUES (12, 23, 1);
INSERT INTO `crawler_flow_detail` VALUES (12, 24, 2);
INSERT INTO `crawler_flow_detail` VALUES (12, 25, 3);
INSERT INTO `crawler_flow_detail` VALUES (13, 26, 1);
INSERT INTO `crawler_flow_detail` VALUES (13, 27, 2);
INSERT INTO `crawler_flow_detail` VALUES (14, 28, 1);
INSERT INTO `crawler_flow_detail` VALUES (14, 29, 2);
INSERT INTO `crawler_flow_detail` VALUES (14, 30, 3);
INSERT INTO `crawler_flow_detail` VALUES (15, 32, 1);
INSERT INTO `crawler_flow_detail` VALUES (15, 33, 2);
INSERT INTO `crawler_flow_detail` VALUES (16, 34, 1);
INSERT INTO `crawler_flow_detail` VALUES (16, 35, 2);
INSERT INTO `crawler_flow_detail` VALUES (17, 36, 1);
INSERT INTO `crawler_flow_detail` VALUES (17, 37, 2);
INSERT INTO `crawler_flow_detail` VALUES (18, 38, 1);
INSERT INTO `crawler_flow_detail` VALUES (18, 39, 2);
INSERT INTO `crawler_flow_detail` VALUES (19, 40, 1);
INSERT INTO `crawler_flow_detail` VALUES (19, 41, 2);

-- ----------------------------
-- Records of crawler_flow_info
-- ----------------------------
INSERT INTO `crawler_flow_info` VALUES (1, '银保监_政策法规', 1, '');
INSERT INTO `crawler_flow_info` VALUES (2, '银保监_监管动态', 1, '');
INSERT INTO `crawler_flow_info` VALUES (3, '中国互联网金融协会_政策法规', 1, '');
INSERT INTO `crawler_flow_info` VALUES (4, '中国互联网金融协会_财经要闻', 1, '');
INSERT INTO `crawler_flow_info` VALUES (5, '卡讯网_银行动态', 1, '');
INSERT INTO `crawler_flow_info` VALUES (6, '卡讯网_专家点评', 1, '');
INSERT INTO `crawler_flow_info` VALUES (7, '卡讯网_新卡快报', 1, '');
INSERT INTO `crawler_flow_info` VALUES (8, '网贷之家_要闻', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (9, '艾瑞咨询_ 金融', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (10, '易观金融', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (11, '新流财经_公众号', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (12, '雪球网_金融行业资讯', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (13, '虎嗅_洪言微语', 1, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (14, 'CMBMALL', 2, 'OFF');
INSERT INTO `crawler_flow_info` VALUES (15, '卡讯网_焦点新闻', 1, '');
INSERT INTO `crawler_flow_info` VALUES (16, '卡讯网_业界新闻', 1, '');
INSERT INTO `crawler_flow_info` VALUES (17, '网贷之家_网贷行业', 1, '');
INSERT INTO `crawler_flow_info` VALUES (18, '网贷之家_平台动态', 1, '');
INSERT INTO `crawler_flow_info` VALUES (19, '网贷之家_互联网金融', 1, '');

SET FOREIGN_KEY_CHECKS = 1;