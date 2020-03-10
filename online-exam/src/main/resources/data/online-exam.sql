SET NAMES utf8mb4;
SET GLOBAL time_zone = '+8:00'; #修改mysql全局时区为北京时间,即我们所在的东8区
SET time_zone = '+8:00'; #修改当前会话时区
set sql_safe_updates=0;
flush privileges; #立即生效

CREATE SCHEMA `online_exam` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin ;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `email_message`
-- ----------------------------
DROP TABLE IF EXISTS `email_message`;
CREATE TABLE `email_message` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `tos` varchar(255) COLLATE utf8mb4_bin NOT NULL,
                                 `subject` varchar(255) COLLATE utf8mb4_bin NOT NULL,
                                 `content` varchar(255) COLLATE utf8mb4_bin NOT NULL,
                                 `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updatetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='邮件发送记录';

-- ----------------------------
-- Records of email_message
-- ----------------------------
INSERT INTO `email_message` VALUES ('1', 'test@test.com', 'test', '你好，测试', '2019-12-19 01:17:56', '2019-12-19 01:17:56');
INSERT INTO `email_message` VALUES ('2', 'test@test.com', 'test', '你好，测试', '2019-12-19 01:20:18', '2019-12-19 01:20:18');
INSERT INTO `email_message` VALUES ('3', 'test@test.11com', 'test', '你好，测试', '2019-12-19 01:21:11', '2019-12-19 01:21:11');
INSERT INTO `email_message` VALUES ('4', 'test@test.11com', 'test', '你好，测试', '2019-12-19 01:30:46', '2019-12-19 01:30:46');
INSERT INTO `email_message` VALUES ('5', 'test@test.11com', 'test', '你好，测试', '2019-12-19 01:31:21', '2019-12-19 01:31:21');

-- ----------------------------
-- Table structure for `exam`
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
                        `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
                        `subject_id` int DEFAULT NULL COMMENT '科目id',
                        `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '试卷名称',
                        `totalScore` int NOT NULL COMMENT '试卷总分',
                        `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '试卷描述',
                        `flag` int NOT NULL DEFAULT '1' COMMENT '试卷标识,标识试卷可不可用 1:可用 0:不可用',
                        `createBy` int DEFAULT NULL COMMENT '记录试卷创建人id',
                        `updateBy` int DEFAULT NULL COMMENT '最后更新人id',
                        `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                        `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='试卷表';

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES ('1', null, '试卷一', '100', 'test试卷', '0', '62', '62', '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam` VALUES ('2', null, '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-26 17:50:01', '2020-01-26 17:50:01');
INSERT INTO `exam` VALUES ('3', null, '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-26 17:51:20', '2020-01-26 17:51:20');
INSERT INTO `exam` VALUES ('4', null, '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-26 17:54:21', '2020-01-26 17:54:21');
INSERT INTO `exam` VALUES ('5', null, '试卷一', '100', 'test试卷', '0', '62', '62', '2020-01-26 17:57:29', '2020-01-26 17:57:29');
INSERT INTO `exam` VALUES ('6', null, '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam` VALUES ('7', null, '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam` VALUES ('8', null, '试卷一', '100', 'test试卷', '0', '62', '62', '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam` VALUES ('9', '2', '试卷一', '100', 'test试卷', '0', '62', '62', '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam` VALUES ('10', '2', '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-27 01:00:46', '2020-01-27 01:00:46');
INSERT INTO `exam` VALUES ('11', '2', '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-27 01:48:30', '2020-01-27 01:48:30');
INSERT INTO `exam` VALUES ('12', '2', '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-27 01:50:56', '2020-01-27 01:50:56');
INSERT INTO `exam` VALUES ('13', '2', '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-27 01:55:36', '2020-01-27 01:55:36');
INSERT INTO `exam` VALUES ('14', '2', '试卷一', '100', 'test试卷', '1', '62', '62', '2020-01-27 02:06:11', '2020-01-27 02:06:11');
INSERT INTO `exam` VALUES ('15', '2', '测试判卷', '100', '测试判卷', '1', '62', '62', '2020-01-27 21:48:57', '2020-01-27 21:48:57');

-- ----------------------------
-- Table structure for `exam_detail`
-- ----------------------------
DROP TABLE IF EXISTS `exam_detail`;
CREATE TABLE `exam_detail` (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `exam_id` int NOT NULL,
                               `topic_id` int NOT NULL,
                               `topicmark` double DEFAULT NULL,
                               `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                               `updatetime` datetime DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of exam_detail
-- ----------------------------
INSERT INTO `exam_detail` VALUES ('1', '1', '1', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('2', '1', '3', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('3', '1', '5', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('4', '1', '6', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('5', '1', '7', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('6', '1', '8', null, '2020-01-26 17:47:07', '2020-01-26 17:47:07');
INSERT INTO `exam_detail` VALUES ('7', '2', '1', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('8', '2', '3', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('9', '2', '5', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('10', '2', '6', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('11', '2', '7', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('12', '2', '8', null, '2020-01-26 17:50:02', '2020-01-26 17:50:02');
INSERT INTO `exam_detail` VALUES ('13', '3', '1', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('14', '3', '3', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('15', '3', '5', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('16', '3', '6', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('17', '3', '7', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('18', '3', '8', null, '2020-01-26 17:51:38', '2020-01-26 17:51:38');
INSERT INTO `exam_detail` VALUES ('19', '4', '2225', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('20', '4', '2226', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('21', '4', '2227', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('22', '4', '2228', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('23', '4', '2230', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('24', '4', '2234', null, '2020-01-26 17:54:24', '2020-01-26 17:54:24');
INSERT INTO `exam_detail` VALUES ('25', '5', '2225', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('26', '5', '2226', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('27', '5', '2227', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('28', '5', '2228', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('29', '5', '2230', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('30', '5', '2234', null, '2020-01-26 17:57:35', '2020-01-26 17:57:35');
INSERT INTO `exam_detail` VALUES ('31', '6', '2225', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('32', '6', '2226', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('33', '6', '2227', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('34', '6', '2228', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('35', '6', '2230', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('36', '6', '2234', null, '2020-01-26 19:37:16', '2020-01-26 19:37:16');
INSERT INTO `exam_detail` VALUES ('37', '7', '2225', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('38', '7', '2226', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('39', '7', '2227', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('40', '7', '2228', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('41', '7', '2230', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('42', '7', '2234', null, '2020-01-26 19:37:35', '2020-01-26 19:37:35');
INSERT INTO `exam_detail` VALUES ('43', '8', '2225', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('44', '8', '2226', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('45', '8', '2227', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('46', '8', '2228', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('47', '8', '2230', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('48', '8', '2234', null, '2020-01-26 19:38:02', '2020-01-26 19:38:02');
INSERT INTO `exam_detail` VALUES ('49', '9', '2225', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('50', '9', '2226', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('51', '9', '2227', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('52', '9', '2228', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('53', '9', '2230', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('54', '9', '2234', null, '2020-01-26 19:39:56', '2020-01-26 19:39:56');
INSERT INTO `exam_detail` VALUES ('55', '10', '2040', '20', '2020-01-27 01:00:49', '2020-01-27 01:00:49');
INSERT INTO `exam_detail` VALUES ('56', '11', '2040', '200', '2020-01-27 01:48:30', '2020-01-27 01:48:30');
INSERT INTO `exam_detail` VALUES ('57', '12', '2240', '200', '2020-01-27 01:50:56', '2020-01-27 01:50:56');
INSERT INTO `exam_detail` VALUES ('58', '13', '2240', '200', '2020-01-27 01:55:36', '2020-01-27 01:55:36');
INSERT INTO `exam_detail` VALUES ('59', '13', '2241', '300', '2020-01-27 01:55:36', '2020-01-27 01:55:36');
INSERT INTO `exam_detail` VALUES ('60', '13', '2242', '400', '2020-01-27 01:55:36', '2020-01-27 01:55:36');
INSERT INTO `exam_detail` VALUES ('61', '13', '2243', '500', '2020-01-27 01:55:36', '2020-01-27 01:55:36');
INSERT INTO `exam_detail` VALUES ('62', '14', '2240', '200', '2020-01-27 02:06:11', '2020-01-27 02:06:11');
INSERT INTO `exam_detail` VALUES ('63', '14', '2241', '300', '2020-01-27 02:06:11', '2020-01-27 02:06:11');
INSERT INTO `exam_detail` VALUES ('64', '14', '2242', '400', '2020-01-27 02:06:11', '2020-01-27 02:06:11');
INSERT INTO `exam_detail` VALUES ('65', '14', '2243', '500', '2020-01-27 02:06:11', '2020-01-27 02:06:11');
INSERT INTO `exam_detail` VALUES ('66', '15', '2225', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('67', '15', '2226', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('68', '15', '2227', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('69', '15', '2228', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('70', '15', '2229', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('71', '15', '2230', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('72', '15', '2231', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('73', '15', '2232', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('74', '15', '2233', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('75', '15', '2234', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('76', '15', '2235', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('77', '15', '2236', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('78', '15', '2237', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('79', '15', '2238', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('80', '15', '2239', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('81', '15', '2240', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('82', '15', '2241', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('83', '15', '2242', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('84', '15', '2243', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');
INSERT INTO `exam_detail` VALUES ('85', '15', '2244', '5', '2020-01-27 21:48:57', '2020-01-27 21:48:57');

-- ----------------------------
-- Table structure for `exam_record`
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record` (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
                               `user_id` int NOT NULL COMMENT '用户id',
                               `schedule_id` int NOT NULL COMMENT '日程id',
                               `score` double DEFAULT NULL COMMENT '成绩',
                               `beginTime` datetime DEFAULT NULL COMMENT '考试开始时间',
                               `endTime` datetime DEFAULT NULL COMMENT '考试结束时间',
                               `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='考试记录';

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES ('1', '1', '1', null, '2020-01-27 00:00:00', null, '2020-01-27 19:42:49', '2020-01-27 19:42:49');
INSERT INTO `exam_record` VALUES ('2', '1', '1', null, null, null, '2020-01-27 19:45:05', '2020-01-27 19:45:05');
INSERT INTO `exam_record` VALUES ('3', '1', '2', '15', '2020-01-27 21:03:27', '2020-01-27 21:03:29', '2020-01-27 21:03:31', '2020-01-27 21:03:33');
INSERT INTO `exam_record` VALUES ('4', '1', '5', '30', '2020-01-27 21:51:30', '2020-01-27 21:51:32', '2020-01-27 21:51:48', '2020-01-27 21:51:48');
INSERT INTO `exam_record` VALUES ('11', '62', '4', '99', '2020-01-28 09:13:07', '2020-01-29 03:21:15', '2020-01-28 23:13:06', '2020-01-28 23:13:06');
INSERT INTO `exam_record` VALUES ('12', '62', '6', '88', '2020-03-08 13:47:25', '2020-03-08 13:47:28', '2020-03-08 13:47:31', '2020-03-08 13:47:31');

-- ----------------------------
-- Table structure for `exam_record_topic`
-- ----------------------------
DROP TABLE IF EXISTS `exam_record_topic`;
CREATE TABLE `exam_record_topic` (
                                     `id` int NOT NULL AUTO_INCREMENT,
                                     `record_id` int NOT NULL COMMENT 'exam_record表主键关联',
                                     `topic_id` int NOT NULL COMMENT '题目表主键关联',
                                     `answer` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '用户做题答案',
                                     `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                                     `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='做题记录表';

-- ----------------------------
-- Records of exam_record_topic
-- ----------------------------
INSERT INTO `exam_record_topic` VALUES ('1', '3', '2240', 'C', '2020-01-27 21:04:49', '2020-01-27 21:29:30');
INSERT INTO `exam_record_topic` VALUES ('2', '3', '2241', 'A', '2020-01-27 21:04:57', '2020-01-30 19:19:52');
INSERT INTO `exam_record_topic` VALUES ('3', '3', '2242', 'A', '2020-01-27 21:05:03', '2020-01-27 21:29:31');
INSERT INTO `exam_record_topic` VALUES ('4', '3', '2243', 'D', '2020-01-27 21:05:12', '2020-01-27 21:29:34');
INSERT INTO `exam_record_topic` VALUES ('156', '4', '21', '测试答案2', '2020-01-28 01:56:55', '2020-01-28 01:56:55');
INSERT INTO `exam_record_topic` VALUES ('157', '4', '22', '测试答案3', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('158', '4', '23', '测试答案4', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('159', '4', '24', '测试答案5', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('160', '4', '25', '测试答案6', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('161', '4', '26', '测试答案7', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('162', '4', '27', '测试答案8', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('163', '4', '28', '测试答案9', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('164', '4', '29', '测试答案10', '2020-01-28 01:56:56', '2020-01-28 01:56:56');
INSERT INTO `exam_record_topic` VALUES ('165', '8', '21', '测试答案2', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('166', '8', '22', '测试答案3', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('167', '8', '23', '测试答案4', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('168', '8', '24', '测试答案5', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('169', '8', '25', '测试答案6', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('170', '8', '26', '测试答案7', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('171', '8', '27', '测试答案8', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('172', '8', '28', '测试答案9', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('173', '8', '29', '测试答案10', '2020-01-28 22:54:05', '2020-01-28 22:54:05');
INSERT INTO `exam_record_topic` VALUES ('177', '11', '21', '测试答案2', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('178', '11', '22', '测试答案3', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('179', '11', '23', '测试答案4', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('180', '11', '24', '测试答案5', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('181', '11', '25', '测试答案6', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('182', '11', '26', '测试答案7', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('183', '11', '27', '测试答案8', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('184', '11', '28', '测试答案9', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('185', '11', '29', '测试答案10', '2020-01-29 17:21:15', '2020-01-29 17:21:15');
INSERT INTO `exam_record_topic` VALUES ('186', '3', '2244', 'A', '2020-01-30 19:09:05', '2020-01-30 19:09:05');
INSERT INTO `exam_record_topic` VALUES ('187', '3', '2225', 'B', '2020-01-30 19:09:12', '2020-01-30 19:14:16');
INSERT INTO `exam_record_topic` VALUES ('188', '3', '2226', 'D', '2020-01-30 19:09:22', '2020-01-30 19:14:19');
INSERT INTO `exam_record_topic` VALUES ('189', '3', '2227', 'D', '2020-01-30 19:09:29', '2020-01-30 19:14:21');
INSERT INTO `exam_record_topic` VALUES ('190', '3', '2228', 'D', '2020-01-30 19:09:37', '2020-01-30 19:15:10');

-- ----------------------------
-- Table structure for `exam_schedule`
-- ----------------------------
DROP TABLE IF EXISTS `exam_schedule`;
CREATE TABLE `exam_schedule` (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `exam_id` int DEFAULT NULL COMMENT '试卷id',
                                 `beginTime` datetime NOT NULL COMMENT '日程开始时间',
                                 `endTime` datetime DEFAULT NULL COMMENT '日程结束时间',
                                 `type` int NOT NULL COMMENT '日程类型',
                                 `setterId` int NOT NULL COMMENT '设定人id',
                                 `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '日程介绍',
                                 `flag` int DEFAULT '1' COMMENT '日程是否可用 0:不可用 1:可用',
                                 `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                                 `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='考试日程表';

-- ----------------------------
-- Records of exam_schedule
-- ----------------------------
INSERT INTO `exam_schedule` VALUES ('1', '1', '2020-01-27 00:00:00', null, '1', '62', 'fjalkds', '1', '2020-01-27 20:04:25', '2020-01-27 20:04:25');
INSERT INTO `exam_schedule` VALUES ('2', '14', '2020-01-27 21:02:43', null, '2', '62', 'test', '1', '2020-01-27 21:02:55', '2020-01-28 22:31:20');
INSERT INTO `exam_schedule` VALUES ('3', '1', '2020-01-27 00:00:00', null, '1', '62', 'fjalkds', '1', '2020-01-27 21:16:02', '2020-01-27 21:16:02');
INSERT INTO `exam_schedule` VALUES ('4', '14', '2020-01-27 07:15:55', '2020-03-08 14:28:16', '2', '62', '测试考试日程样例', '1', '2020-01-27 21:18:19', '2020-03-08 14:28:18');
INSERT INTO `exam_schedule` VALUES ('5', '15', '2020-01-27 21:50:24', '2020-01-27 21:50:26', '2', '62', '测试判卷', '1', '2020-01-27 21:50:54', '2020-01-27 21:50:54');
INSERT INTO `exam_schedule` VALUES ('6', '15', '2020-03-08 13:47:46', '2020-03-08 13:47:47', '2', '62', '英语期末考试(测试)', '1', '2020-03-08 13:48:09', '2020-03-08 13:48:09');

-- ----------------------------
-- Table structure for `login_user`
-- ----------------------------
DROP TABLE IF EXISTS `login_user`;
CREATE TABLE `login_user` (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
                              `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户登录名',
                              `password` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户登录密码',
                              `realname` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户真实姓名',
                              `gender` int DEFAULT NULL COMMENT '用户性别',
                              `email` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户邮箱',
                              `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
                              `school_id` int DEFAULT NULL COMMENT '学校',
                              `college_id` int DEFAULT NULL COMMENT '学院',
                              `major_id` int DEFAULT NULL COMMENT '专业id',
                              `class_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '班级id',
                              `role` int DEFAULT NULL COMMENT '用户权限 0:普通用户 1:试题管理员 2:系统管理员',
                              `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `login_user_telephone_uindex` (`telephone`),
                              UNIQUE KEY `username_UNIQUE` (`username`),
                              UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of login_user
-- ----------------------------
INSERT INTO `login_user` VALUES ('62', 'user', '1', '姓名', '0', '1@1.com', '1', '1', '2', '4', '111', '0', '2020-01-25 23:51:19', '2020-03-10 23:04:44');
INSERT INTO `login_user` VALUES ('68', 'user1', '1', '姓名1', '0', '1@1.com1', '11', '1', '2', '4', '165132', '0', '2020-03-06 18:14:38', '2020-03-10 23:04:44');

-- ----------------------------
-- Table structure for `login_user_security`
-- ----------------------------
DROP TABLE IF EXISTS `login_user_security`;
CREATE TABLE `login_user_security` (
                                       `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
                                       `login_user_id` int NOT NULL COMMENT '用户表主键',
                                       `question` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '密保问题',
                                       `answer` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '密保答案',
                                       `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                                       `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户密保';

-- ----------------------------
-- Records of login_user_security
-- ----------------------------
INSERT INTO `login_user_security` VALUES ('53', '57', '11qqq', '11aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('54', '57', '22qqq', '22aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('55', '57', '33qqq', '33aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('56', '59', '11qqq', '11aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('57', '59', '22qqq', '22aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('58', '59', '33qqq', '33aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('59', '60', '11qqq', '11aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('60', '60', '22qqq', '22aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('61', '60', '33qqq', '33aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('62', '61', '11qqq', '11aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('63', '61', '22qqq', '22aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('64', '61', '33qqq', '33aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('65', '62', '11qqq', '11aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('66', '62', '22qqq', '22aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('67', '62', '33qqq', '33aaa', '2020-01-27 16:53:29', '2020-01-27 16:53:29');
INSERT INTO `login_user_security` VALUES ('68', '68', '11qqq', '11aaa', '2020-03-06 18:14:38', '2020-03-06 18:14:38');
INSERT INTO `login_user_security` VALUES ('69', '68', '22qqq', '22aaa', '2020-03-06 18:14:38', '2020-03-06 18:14:38');
INSERT INTO `login_user_security` VALUES ('70', '68', '33qqq', '33aaa', '2020-03-06 18:14:38', '2020-03-06 18:14:38');

-- ----------------------------
-- Table structure for `options`
-- ----------------------------
DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
                           `topic_id` int NOT NULL COMMENT '题目表id',
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
                           `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of options
-- ----------------------------
INSERT INTO `options` VALUES ('2225', 'A', 'FC-SAN存储');
INSERT INTO `options` VALUES ('2225', 'B', 'IP-SAN存储');
INSERT INTO `options` VALUES ('2225', 'C', '本地硬盘');
INSERT INTO `options` VALUES ('2225', 'D', '本地内存盘');
INSERT INTO `options` VALUES ('2226', 'A', '应用按需分配资源');
INSERT INTO `options` VALUES ('2226', 'B', '广泛兼容各种软硬件');
INSERT INTO `options` VALUES ('2226', 'C', '自动化调度');
INSERT INTO `options` VALUES ('2226', 'D', '丰富的运维管理');
INSERT INTO `options` VALUES ('2226', 'E', 'testE');
INSERT INTO `options` VALUES ('2226', 'F', 'testF');
INSERT INTO `options` VALUES ('2227', 'A', '对');
INSERT INTO `options` VALUES ('2227', 'B', '错');
INSERT INTO `options` VALUES ('2228', 'A', '新建');
INSERT INTO `options` VALUES ('2228', 'B', '运行');
INSERT INTO `options` VALUES ('2228', 'C', '就绪');
INSERT INTO `options` VALUES ('2228', 'D', '死亡');
INSERT INTO `options` VALUES ('2228', 'E', 'testE');
INSERT INTO `options` VALUES ('2229', 'A', 'Q→（P∧Q）');
INSERT INTO `options` VALUES ('2229', 'B', 'P→（P∧Q）');
INSERT INTO `options` VALUES ('2229', 'C', '（P∧Q）→P ');
INSERT INTO `options` VALUES ('2229', 'D', '（P∨Q）→Q');
INSERT INTO `options` VALUES ('2230', 'A', 'short');
INSERT INTO `options` VALUES ('2230', 'B', 'long');
INSERT INTO `options` VALUES ('2230', 'C', 'int');
INSERT INTO `options` VALUES ('2230', 'D', 'byte');
INSERT INTO `options` VALUES ('2231', 'A', '类型定义机制');
INSERT INTO `options` VALUES ('2231', 'B', '数据封装机制');
INSERT INTO `options` VALUES ('2231', 'C', '类型定义机制和数据封装机制');
INSERT INTO `options` VALUES ('2231', 'D', '上述都不对');
INSERT INTO `options` VALUES ('2232', 'A', '先声明对象，然后才能使用对象');
INSERT INTO `options` VALUES ('2232', 'B', '先声明对象，喂对象分配内存空间，对对象初始化，然后才能使用对象');
INSERT INTO `options` VALUES ('2232', 'C', '先声明对象，喂对象分配内存空间，然后才能使用对象');
INSERT INTO `options` VALUES ('2232', 'D', '上述说法都对');
INSERT INTO `options` VALUES ('2233', 'A', '用基本数据类型作为参数');
INSERT INTO `options` VALUES ('2233', 'B', '用对象作为参数');
INSERT INTO `options` VALUES ('2233', 'C', 'A和B都对');
INSERT INTO `options` VALUES ('2233', 'D', 'A和B都不对');
INSERT INTO `options` VALUES ('2234', 'A', '一个子类可以有多个父类，一个父类也可以有多个子类');
INSERT INTO `options` VALUES ('2234', 'B', '一个子类可以有多个父类，但一个父类只可以有一个子类');
INSERT INTO `options` VALUES ('2234', 'C', '一个子类可以有一个父类，但一个父类可以有多个子类');
INSERT INTO `options` VALUES ('2234', 'D', '上述说法都不对');
INSERT INTO `options` VALUES ('2235', 'A', '安全性');
INSERT INTO `options` VALUES ('2235', 'B', '多线性');
INSERT INTO `options` VALUES ('2235', 'C', '跨平台');
INSERT INTO `options` VALUES ('2235', 'D', '可移植');
INSERT INTO `options` VALUES ('2236', 'A', '每次读入的字节数不同');
INSERT INTO `options` VALUES ('2236', 'B', '前者带有缓冲，后者没有');
INSERT INTO `options` VALUES ('2236', 'C', '前者是字符读写，后者是字节读写');
INSERT INTO `options` VALUES ('2236', 'D', '二者没有区别，可以互换使用');
INSERT INTO `options` VALUES ('2237', 'A', 'default String s;');
INSERT INTO `options` VALUES ('2237', 'B', 'public final static native int w()');
INSERT INTO `options` VALUES ('2237', 'C', 'abstract double d;');
INSERT INTO `options` VALUES ('2237', 'D', 'abstract final double hyperbolicCosine()');
INSERT INTO `options` VALUES ('2238', 'A', 'class HasStatic {}');
INSERT INTO `options` VALUES ('2238', 'B', 'pravite static int x=100;');
INSERT INTO `options` VALUES ('2238', 'C', 'public static void main(String args[]){}');
INSERT INTO `options` VALUES ('2238', 'D', 'HasStatic hs1=new HasStatic();');
INSERT INTO `options` VALUES ('2239', 'A', 'int');
INSERT INTO `options` VALUES ('2239', 'B', 'float');
INSERT INTO `options` VALUES ('2239', 'C', 'double');
INSERT INTO `options` VALUES ('2239', 'D', 'void');
INSERT INTO `options` VALUES ('2240', 'A', 'start()');
INSERT INTO `options` VALUES ('2240', 'B', 'init()');
INSERT INTO `options` VALUES ('2240', 'C', 'run()');
INSERT INTO `options` VALUES ('2240', 'D', 'synchronized()');
INSERT INTO `options` VALUES ('2241', 'A', 'const');
INSERT INTO `options` VALUES ('2241', 'B', 'double');
INSERT INTO `options` VALUES ('2241', 'C', 'hello');
INSERT INTO `options` VALUES ('2241', 'D', 'BigMeaninglessName');
INSERT INTO `options` VALUES ('2242', 'A', 'transient');
INSERT INTO `options` VALUES ('2242', 'B', 'static');
INSERT INTO `options` VALUES ('2242', 'C', 'serialize');
INSERT INTO `options` VALUES ('2242', 'D', 'synchronized');
INSERT INTO `options` VALUES ('2243', 'A', 'static');
INSERT INTO `options` VALUES ('2243', 'B', 'package');
INSERT INTO `options` VALUES ('2243', 'C', 'private');
INSERT INTO `options` VALUES ('2243', 'D', 'public');
INSERT INTO `options` VALUES ('2244', 'A', 'java EE');
INSERT INTO `options` VALUES ('2244', 'B', 'java Card');
INSERT INTO `options` VALUES ('2244', 'C', 'java ME');
INSERT INTO `options` VALUES ('2244', 'D', 'java HE');
INSERT INTO `options` VALUES ('2244', 'E', 'testE');
INSERT INTO `options` VALUES ('2244', 'F', 'java HE');
INSERT INTO `options` VALUES ('2245', 'A', 'boolean = 1;');
INSERT INTO `options` VALUES ('2245', 'B', 'boolean a=(9>=10);');
INSERT INTO `options` VALUES ('2245', 'C', 'boolean a=\"真\"；');
INSERT INTO `options` VALUES ('2245', 'D', 'boolean a ==false;');
INSERT INTO `options` VALUES ('2246', 'A', 'STRING');
INSERT INTO `options` VALUES ('2246', 'B', 'x3x;');
INSERT INTO `options` VALUES ('2246', 'C', 'void');
INSERT INTO `options` VALUES ('2246', 'D', 'de$f');
INSERT INTO `options` VALUES ('2247', 'A', '31.0');
INSERT INTO `options` VALUES ('2247', 'B', '0.0');
INSERT INTO `options` VALUES ('2247', 'C', '1.0');
INSERT INTO `options` VALUES ('2247', 'D', '2.0');
INSERT INTO `options` VALUES ('2248', 'A', 'i++;');
INSERT INTO `options` VALUES ('2248', 'B', 'i>5;');
INSERT INTO `options` VALUES ('2248', 'C', 'bEqual = str.equals(\"q\");');
INSERT INTO `options` VALUES ('2248', 'D', 'count ==i;');
INSERT INTO `options` VALUES ('2249', 'A', '<%--与--%>>');
INSERT INTO `options` VALUES ('2249', 'B', '/');
INSERT INTO `options` VALUES ('2249', 'C', '/**与**/');
INSERT INTO `options` VALUES ('2249', 'D', '<!--与-->');
INSERT INTO `options` VALUES ('2250', 'A', 'Session');
INSERT INTO `options` VALUES ('2250', 'B', 'application');
INSERT INTO `options` VALUES ('2250', 'C', 'pageContext');
INSERT INTO `options` VALUES ('2250', 'D', 'cookie');
INSERT INTO `options` VALUES ('2251', 'A', 'short');
INSERT INTO `options` VALUES ('2251', 'B', 'Boolean');
INSERT INTO `options` VALUES ('2251', 'C', 'Double');
INSERT INTO `options` VALUES ('2251', 'D', 'float');
INSERT INTO `options` VALUES ('2252', 'A', '数据组是一种对象');
INSERT INTO `options` VALUES ('2252', 'B', '数组属于一种原生类');
INSERT INTO `options` VALUES ('2252', 'C', 'int number=[]={31,23,33,43,35,63}');
INSERT INTO `options` VALUES ('2252', 'D', '数组的大小可以任意改变');
INSERT INTO `options` VALUES ('2253', 'A', 'private');
INSERT INTO `options` VALUES ('2253', 'B', 'public');
INSERT INTO `options` VALUES ('2253', 'C', 'protected');
INSERT INTO `options` VALUES ('2253', 'D', 'static');
INSERT INTO `options` VALUES ('2254', 'A', '在类方法中可用this来调用本类的类方法');
INSERT INTO `options` VALUES ('2254', 'B', '在类方法中调用本类的类方法时可直接调用');
INSERT INTO `options` VALUES ('2254', 'C', '在类方法中只能调用本类的类方法');
INSERT INTO `options` VALUES ('2254', 'D', '在类方法中绝对不能调用实例方法');
INSERT INTO `options` VALUES ('2255', 'A', 'new');
INSERT INTO `options` VALUES ('2255', 'B', '$Usdollars');
INSERT INTO `options` VALUES ('2255', 'C', '1234.0');
INSERT INTO `options` VALUES ('2255', 'D', 'car.taxi');
INSERT INTO `options` VALUES ('2256', 'A', 'void methods();');
INSERT INTO `options` VALUES ('2256', 'B', 'public double methods();');
INSERT INTO `options` VALUES ('2256', 'C', 'public final double methods();');
INSERT INTO `options` VALUES ('2256', 'D', 'static void methods(double d1):');
INSERT INTO `options` VALUES ('2257', 'A', 'Error');
INSERT INTO `options` VALUES ('2257', 'B', 'Throwable');
INSERT INTO `options` VALUES ('2257', 'C', 'Exception');
INSERT INTO `options` VALUES ('2257', 'D', 'RuntimeException');
INSERT INTO `options` VALUES ('2258', 'A', '默认构造器有和它所在类相同的访问修饰词');
INSERT INTO `options` VALUES ('2258', 'B', '默认构造器可调用其他父类的无参构造器');
INSERT INTO `options` VALUES ('2258', 'C', '如果一个雷没有无参构造器，编译器会为它创建一个默认构造器');
INSERT INTO `options` VALUES ('2258', 'D', '只有当一个雷没有任何构造器时，编译器会为它创建一个默认构造器');
INSERT INTO `options` VALUES ('2259', 'A', 'final void methods(){}');
INSERT INTO `options` VALUES ('2259', 'B', 'void final methods(){}');
INSERT INTO `options` VALUES ('2259', 'C', 'static void methods(){}');
INSERT INTO `options` VALUES ('2259', 'D', 'static final void methods(){}');
INSERT INTO `options` VALUES ('2260', 'A', 'long number = 345L;');
INSERT INTO `options` VALUES ('2260', 'B', 'long number = 0345;');
INSERT INTO `options` VALUES ('2260', 'C', 'long number = 0345L;');
INSERT INTO `options` VALUES ('2260', 'D', 'long number = 0x345L;');
INSERT INTO `options` VALUES ('2261', 'A', 'start()');
INSERT INTO `options` VALUES ('2261', 'B', 'stop()');
INSERT INTO `options` VALUES ('2261', 'C', 'init()');
INSERT INTO `options` VALUES ('2261', 'D', 'paint()');
INSERT INTO `options` VALUES ('2262', 'A', 'x[9]为0');
INSERT INTO `options` VALUES ('2262', 'B', 'x[9]未定义');
INSERT INTO `options` VALUES ('2262', 'C', 'x[10]为0');
INSERT INTO `options` VALUES ('2262', 'D', 'x[0]为空');
INSERT INTO `options` VALUES ('2263', 'A', '对象、消息');
INSERT INTO `options` VALUES ('2263', 'B', '继承、多态');
INSERT INTO `options` VALUES ('2263', 'C', '类、封装');
INSERT INTO `options` VALUES ('2263', 'D', '过程调用');
INSERT INTO `options` VALUES ('2264', 'A', '（1011011）2');
INSERT INTO `options` VALUES ('2264', 'B', '（142）8');
INSERT INTO `options` VALUES ('2264', 'C', '（62）16');
INSERT INTO `options` VALUES ('2264', 'D', '（97）10');
INSERT INTO `options` VALUES ('2265', 'A', '23.5');
INSERT INTO `options` VALUES ('2265', 'B', '23.625');
INSERT INTO `options` VALUES ('2265', 'C', '23.75');
INSERT INTO `options` VALUES ('2265', 'D', '23.5125');
INSERT INTO `options` VALUES ('2266', 'A', '198.0');
INSERT INTO `options` VALUES ('2266', 'B', '-198.0');
INSERT INTO `options` VALUES ('2266', 'C', '58.0');
INSERT INTO `options` VALUES ('2266', 'D', '-58.0');
INSERT INTO `options` VALUES ('2267', 'A', '每秒处理百万个字符');
INSERT INTO `options` VALUES ('2267', 'B', '每分钟处理百万个字符');
INSERT INTO `options` VALUES ('2267', 'C', '每秒执行百万条指令');
INSERT INTO `options` VALUES ('2267', 'D', '每分钟执行百万条指令');

-- ----------------------------
-- Table structure for `organization`
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
                                `type` int NOT NULL COMMENT '类型枚举 0:学校 1:学院 2:专业 3:班级',
                                `upper_id` int DEFAULT NULL COMMENT '上一级 id',
                                `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                                `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='学校、学院、专业、班级';

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES ('1', '上海杉达学院', '0', null, '2020-01-25 21:23:30', '2020-01-25 21:23:30');
INSERT INTO `organization` VALUES ('2', '信息科学与技术学院', '1', '1', '2020-01-25 22:18:25', '2020-01-25 22:18:25');
INSERT INTO `organization` VALUES ('3', '复旦大学', '0', '0', '2020-01-25 22:36:05', '2020-01-25 23:21:24');
INSERT INTO `organization` VALUES ('4', '计算机科学与技术', '3', '2', '2020-01-25 22:54:29', '2020-01-25 22:54:29');
INSERT INTO `organization` VALUES ('5', '电子商务', '3', '2', '2020-01-25 22:56:11', '2020-01-25 22:56:11');

-- ----------------------------
-- Table structure for `subject`
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '科目名',
                           `orgId` int NOT NULL COMMENT '对应organization表id',
                           `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                           `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='科目表';

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES ('1', '离散数学', '4', '2020-01-26 17:58:59', '2020-01-26 17:58:59');
INSERT INTO `subject` VALUES ('2', '计算机网络', '4', '2020-01-26 17:59:23', '2020-01-26 18:05:31');
INSERT INTO `subject` VALUES ('3', '软件工程', '4', '2020-01-26 18:05:22', '2020-01-26 18:05:22');

-- ----------------------------
-- Table structure for `sys_enum`
-- ----------------------------
DROP TABLE IF EXISTS `sys_enum`;
CREATE TABLE `sys_enum` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `catalog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模块名',
                            `type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '枚举类型',
                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '枚举名称',
                            `value` int NOT NULL COMMENT '整型值',
                            `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '说明',
                            `updatetime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `createtime` datetime DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='枚举表';

-- ----------------------------
-- Records of sys_enum
-- ----------------------------
INSERT INTO `sys_enum` VALUES ('1', 'COMMON', 'GENDER', '男', '0', null, '2020-01-16 01:46:16', '2020-01-16 01:46:16');
INSERT INTO `sys_enum` VALUES ('2', 'COMMON', 'GENDER', '女', '1', null, '2020-01-16 01:46:16', '2020-01-16 01:46:16');
INSERT INTO `sys_enum` VALUES ('3', 'TOPIC', 'DIFFICULT', '易', '0', null, '2020-01-16 17:50:44', '2020-01-16 17:50:44');
INSERT INTO `sys_enum` VALUES ('4', 'TOPIC', 'DIFFICULT', '中', '1', null, '2020-01-16 17:49:59', '2020-01-16 17:49:59');
INSERT INTO `sys_enum` VALUES ('5', 'TOPIC', 'DIFFICULT', '难', '2', null, '2020-01-16 17:51:04', '2020-01-16 17:51:04');
INSERT INTO `sys_enum` VALUES ('6', 'TOPIC', 'TYPE', '选择题', '0', null, '2020-01-16 17:51:41', '2020-01-16 17:51:41');
INSERT INTO `sys_enum` VALUES ('7', 'TOPIC', 'TYPE', '判断题', '1', null, '2020-01-16 17:51:56', '2020-01-16 17:51:56');
INSERT INTO `sys_enum` VALUES ('8', 'COMMON', 'DEFAULT_PASSWORD', '1', '1', '默认密码(name是密码)', '2020-01-16 17:56:24', '2020-01-16 17:52:12');
INSERT INTO `sys_enum` VALUES ('13', 'SCHEDULE', 'TYPE', '考试', '1', '日程类型为考试', '2020-03-06 20:05:22', '2020-03-06 20:05:22');
INSERT INTO `sys_enum` VALUES ('14', 'SCHEDULE', 'TYPE', '作业', '2', '日程类型为作业', '2020-03-06 20:05:22', '2020-03-06 20:05:22');

-- ----------------------------
-- Table structure for `topic`
-- ----------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
                         `id` int NOT NULL AUTO_INCREMENT COMMENT '题目id',
                         `file_id` int DEFAULT NULL COMMENT '文件id',
                         `type` int NOT NULL COMMENT '题目类型',
                         `difficult` int NOT NULL COMMENT '难度等级',
                         `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '题目描述',
                         `correctkey` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '正确答案',
                         `topicmark` double DEFAULT NULL COMMENT '分值',
                         `analysis` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '题目分析',
                         `subject_id` int NOT NULL COMMENT '学科号',
                         `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                         `flag` int DEFAULT '1' COMMENT '判断题目可不可用',
                         `user_id` int DEFAULT NULL COMMENT '上传用户的Id',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2268 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of topic
-- ----------------------------
INSERT INTO `topic` VALUES ('2225', '43', '0', '2', '管理员在FusionCompute中．为主机添加存储接口．实现主机与存储设备对接以下哪个存储类型需要添加存储接口？', 'B', '20', '', '2', '2020-01-16 19:42:59', '2020-01-16 19:42:59', '1', '57');
INSERT INTO `topic` VALUES ('2226', '43', '0', '2', '以下哪些是FusionSphere的特点？', 'ABCD', '20', '', '2', '2020-01-16 19:43:00', '2020-01-16 19:43:00', '1', '57');
INSERT INTO `topic` VALUES ('2227', '43', '1', '0', '在华为服务器虚拟化解决方案中，每台虚拟机的虚拟网卡连接在虚拟交换机的端口上，为了方便用户同时对多个端口进行配置和管理，将具有相同网络属性的端口划分到同一个端口组下？', 'A', '15', '', '2', '2020-01-16 19:43:02', '2020-01-16 19:43:02', '1', '57');
INSERT INTO `topic` VALUES ('2228', '43', '0', '2', 'FileInputStream与FileOutputStream类用读、写字节流', 'ABCD', '15', '', '2', '2020-01-16 19:43:02', '2020-01-16 19:43:02', '1', '57');
INSERT INTO `topic` VALUES ('2229', '43', '0', '2', '下列命题公式为永真蕴含式的是（ ）。', 'C', '15', '', '2', '2020-01-16 19:43:02', '2020-01-16 19:43:02', '1', '57');
INSERT INTO `topic` VALUES ('2230', '43', '0', '2', '整型数据类型中，需要内存空间最少的是（）。', 'D', '15', '', '2', '2020-01-16 19:43:02', '2020-01-16 19:43:02', '1', '57');
INSERT INTO `topic` VALUES ('2231', '43', '0', '2', 'Java类可以作为（）。', 'C', '15', '', '2', '2020-01-16 19:43:02', '2020-01-16 19:43:02', '1', '57');
INSERT INTO `topic` VALUES ('2232', '43', '0', '0', '在创建对象时必须（）。', 'B', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2233', '43', '0', '2', '在调用方法时，若要使方法改变实参的值，可以（）。', 'B', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2234', '43', '0', '2', 'Java中（）。', 'C', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2235', '43', '0', '2', 'Java语言具有许多优点和特点，那个反映了Java程序并行机制的特点？（）', 'B', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2236', '43', '0', '0', 'Character流与Byte流的区别是（）。', 'C', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2237', '43', '0', '2', '以下声明合法的是（）。', 'D', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2238', '43', '0', '2', '关于一下程序代码的说明正确的是（）。', 'D', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2239', '43', '0', '0', 'Java application中的主类需包含main方法，main方法的返回类型是什么？（）', 'D', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2240', '43', '0', '2', '以下哪个方法用于定义线程的执行体？（）', 'C', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2241', '43', '0', '2', '以下标识符中哪项是不合法的（）。', 'A', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2242', '43', '0', '2', '以下哪个关键字可以用来为对象加互斥锁？（）', 'D', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2243', '43', '0', '1', '若需要定义一个类域或类方法，应使用哪种修饰符？（）', 'C', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2244', '43', '0', '2', 'Java所定义的版本不包括：（）？', 'D', '15', '', '2', '2020-01-16 19:43:03', '2020-01-16 19:43:03', '1', '57');
INSERT INTO `topic` VALUES ('2245', '43', '0', '2', '为一个boolean类型变量赋值时，可以使用（）方式', 'B', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2246', '43', '0', '2', '以下（）不是合法的标识符', 'C', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2247', '43', '0', '2', '表达式（11+3*8）/4%3的值是（）', 'D', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2248', '43', '0', '2', '（）表达式不可以作为循环条件', 'A', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2249', '43', '0', '0', '下列属于JSP中注释的有（）', 'AD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2250', '43', '0', '0', '下列是JSP作用域的通信对象的有（）', 'ABC', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2251', '43', '0', '2', '下面哪个不是java的简单类型数据类型？', 'BC', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2252', '43', '0', '2', '下列说法错误的有（）', 'BCD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2253', '43', '0', '2', '不能用来修饰interface的有（）', 'ACD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2254', '43', '0', '0', '下列说法错误的有（）', 'ACD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2255', '43', '0', '0', '下列标识符不合法的有（）', 'ACD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2256', '43', '0', '2', '在接口中一下哪条定义是正确的？', 'AB', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2257', '43', '0', '0', '以下哪四个能使用throw抛出？', 'ABCD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2258', '43', '0', '2', '下面哪几种描述是正确的？', 'ABD', '15', '', '2', '2020-01-16 19:43:04', '2020-01-16 19:43:04', '1', '57');
INSERT INTO `topic` VALUES ('2259', '43', '0', '2', '哪两种声明防止方法覆盖？', 'AD', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '1', '57');
INSERT INTO `topic` VALUES ('2260', '43', '0', '0', '哪个是将一个十六进制值赋值给一个long型变量？（）', 'D', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2261', '43', '0', '2', '在Java applet程序中，用户自定义的Apllet子类常常覆盖父类的（）方法来完成applet界面的初始化工作。', 'D', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2262', '43', '0', '0', '执行完以下代码int[] x=new ine[10];后，以下哪项说明是正确的（）', 'A', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2263', '43', '0', '2', '下述概念中不属于面向对象方法的是（）', 'D', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2264', '43', '0', '0', '下列数中最小的数是（）', 'A', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2265', '43', '0', '2', '若二进制数为0101111.101，则该数的十进制表示为（）。', 'B', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2266', '43', '0', '2', '11000110为二进制补码，该数的真值为（）。', 'D', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');
INSERT INTO `topic` VALUES ('2267', '43', '0', '2', 'MIPS用来描述计算机的运算速度，含义是（）', 'C', '15', '', '2', '2020-01-16 19:43:05', '2020-01-16 19:43:05', '0', '57');

-- ----------------------------
-- Table structure for `upload_file`
-- ----------------------------
DROP TABLE IF EXISTS `upload_file`;
CREATE TABLE `upload_file` (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT '文件id',
                               `fileName` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '文件名',
                               `filePath` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '上传文件路径',
                               `user_id` int NOT NULL COMMENT '上传用户id',
                               `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of upload_file
-- ----------------------------
INSERT INTO `upload_file` VALUES ('17', '考试题目测试数据 20200105 223155.xlsx', 'files\\考试题目测试数据 20200105 223155.xlsx', '41', '2020-01-05 22:32:17', '2020-01-05 22:32:17');
INSERT INTO `upload_file` VALUES ('18', '考试题目上传 模板 20200114 132008.xlsx', 'files\\考试题目上传 模板 20200114 132008.xlsx', '41', '2020-01-14 13:20:07', '2020-01-14 13:20:07');
INSERT INTO `upload_file` VALUES ('19', '考试题目测试数据 20200114 132029.xlsx', 'files\\考试题目测试数据 20200114 132029.xlsx', '41', '2020-01-14 13:20:28', '2020-01-14 13:20:28');
INSERT INTO `upload_file` VALUES ('20', '考试题目测试数据 20200114 154932.xlsx', 'files\\考试题目测试数据 20200114 154932.xlsx', '50', '2020-01-14 15:49:46', '2020-01-14 15:49:46');
INSERT INTO `upload_file` VALUES ('21', '考试题目测试数据 20200114 160704.xlsx', 'files\\考试题目测试数据 20200114 160704.xlsx', '50', '2020-01-14 16:07:04', '2020-01-14 16:07:04');
INSERT INTO `upload_file` VALUES ('28', '考试题目测试数据 20200114 165101.xlsx', 'files\\考试题目测试数据 20200114 165101.xlsx', '50', '2020-01-14 16:51:02', '2020-01-14 16:51:02');
INSERT INTO `upload_file` VALUES ('29', '考试题目测试数据 20200114 165336.xlsx', 'files\\考试题目测试数据 20200114 165336.xlsx', '50', '2020-01-14 16:53:37', '2020-01-14 16:53:37');
INSERT INTO `upload_file` VALUES ('30', '考试题目测试数据 20200114 165728.xlsx', 'files\\考试题目测试数据 20200114 165728.xlsx', '50', '2020-01-14 16:57:27', '2020-01-14 16:57:27');
INSERT INTO `upload_file` VALUES ('31', '考试题目测试数据 20200114 170039.xlsx', 'files\\考试题目测试数据 20200114 170039.xlsx', '50', '2020-01-14 17:00:40', '2020-01-14 17:00:40');
INSERT INTO `upload_file` VALUES ('32', '考试题目测试数据 20200114 170428.xlsx', 'files\\考试题目测试数据 20200114 170428.xlsx', '50', '2020-01-14 17:04:29', '2020-01-14 17:04:29');
INSERT INTO `upload_file` VALUES ('33', '考试题目测试数据 20200114 170852.xlsx', 'files\\考试题目测试数据 20200114 170852.xlsx', '50', '2020-01-14 17:08:51', '2020-01-14 17:08:51');
INSERT INTO `upload_file` VALUES ('34', '考试题目测试数据 20200114 170901.xlsx', 'files\\考试题目测试数据 20200114 170901.xlsx', '50', '2020-01-14 17:09:00', '2020-01-14 17:09:00');
INSERT INTO `upload_file` VALUES ('35', '考试题目测试数据 20200114 170919.xlsx', 'files\\考试题目测试数据 20200114 170919.xlsx', '50', '2020-01-14 17:09:18', '2020-01-14 17:09:18');
INSERT INTO `upload_file` VALUES ('36', '考试题目测试数据 20200114 170956.xlsx', 'files\\考试题目测试数据 20200114 170956.xlsx', '50', '2020-01-14 17:09:55', '2020-01-14 17:09:55');
INSERT INTO `upload_file` VALUES ('37', '考试题目测试数据 20200114 193906.xlsx', 'files\\考试题目测试数据 20200114 193906.xlsx', '50', '2020-01-14 19:39:07', '2020-01-14 19:39:07');
INSERT INTO `upload_file` VALUES ('38', '考试题目测试数据 20200114 204512.xlsx', 'files\\考试题目测试数据 20200114 204512.xlsx', '50', '2020-01-14 20:45:11', '2020-01-14 20:45:11');
INSERT INTO `upload_file` VALUES ('39', '考试题目测试数据 20200114 204629.xlsx', 'files\\考试题目测试数据 20200114 204629.xlsx', '50', '2020-01-14 20:46:29', '2020-01-14 20:46:29');
INSERT INTO `upload_file` VALUES ('40', '考试题目测试数据 20200114 205533.xlsx', 'files\\考试题目测试数据 20200114 205533.xlsx', '50', '2020-01-14 20:55:33', '2020-01-14 20:55:33');
INSERT INTO `upload_file` VALUES ('41', '考试题目测试数据 20200115 214533.xlsx', 'files\\考试题目测试数据 20200115 214533.xlsx', '50', '2020-01-15 21:45:31', '2020-01-15 21:45:31');
INSERT INTO `upload_file` VALUES ('42', '考试题目上传 模板 20200116 193920.xlsx', 'files\\考试题目上传 模板 20200116 193920.xlsx', '57', '2020-01-16 19:39:19', '2020-01-16 19:39:19');
INSERT INTO `upload_file` VALUES ('43', '考试题目测试数据 20200116 194237.xlsx', 'files\\考试题目测试数据 20200116 194237.xlsx', '57', '2020-01-16 19:42:36', '2020-01-16 19:42:36');

-- ----------------------------
-- Table structure for `worry_topic`
-- ----------------------------
DROP TABLE IF EXISTS `worry_topic`;
CREATE TABLE `worry_topic` (
                               `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
                               `user_id` int DEFAULT NULL COMMENT '用户id',
                               `record_id` int DEFAULT NULL COMMENT '做题记录表id',
                               `exam_id` int DEFAULT NULL COMMENT '试卷id',
                               `topic_id` int DEFAULT NULL COMMENT '题目id',
                               `worryanswer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '错误答案',
                               `correctanswer` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '正确答案',
                               `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updatetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of worry_topic
-- ----------------------------
INSERT INTO `worry_topic` VALUES ('49', '62', '3', '15', '2242', 'A', 'D', '2020-01-30 19:20:26', '2020-01-30 19:20:26');
INSERT INTO `worry_topic` VALUES ('50', '62', '3', '15', '2243', 'D', 'C', '2020-01-30 19:20:26', '2020-01-30 19:20:26');
INSERT INTO `worry_topic` VALUES ('51', '62', '3', '15', '2244', 'A', 'D', '2020-01-30 19:20:26', '2020-01-30 19:20:26');
INSERT INTO `worry_topic` VALUES ('52', '62', '3', '15', '2226', 'D', 'ABCD', '2020-01-30 19:20:26', '2020-01-30 19:20:26');
INSERT INTO `worry_topic` VALUES ('53', '62', '3', '15', '2227', 'D', 'A', '2020-01-30 19:20:26', '2020-01-30 19:20:26');
INSERT INTO `worry_topic` VALUES ('54', '62', '3', '15', '2228', 'D', 'ABCD', '2020-01-30 19:20:27', '2020-01-30 19:20:27');
INSERT INTO `worry_topic` VALUES ('55', '62', '4', '15', '2226', 'D', 'ABCD', '2020-01-31 00:16:04', '2020-01-31 00:16:04');
INSERT INTO `worry_topic` VALUES ('56', '62', '5', '232', '24', 'test查询', null, '2020-01-31 00:16:22', '2020-01-31 00:16:22');
