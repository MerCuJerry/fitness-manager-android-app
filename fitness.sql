/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : fitness_mysql

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 16/12/2023 11:33:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_name` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `coach_id` int NOT NULL,
  `course_describe` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `coach_id`(`coach_id` ASC) USING BTREE,
  CONSTRAINT `coach_id` FOREIGN KEY (`coach_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, '高温瑜伽', 17, 'BikramYoga。强调在温度高达摄氏四十度上下的教室里练习体位法，以大量流汗为乐，偶有人因身体不佳受不了而产生呕吐虚脱等症状。因为BikramYoga有专利问题，高温瑜珈练法有的称为HotYoga（热瑜珈）以规避，名称不同内容一样，但是业者会以更好的温湿度调控设备作区隔。');
INSERT INTO `course` VALUES (2, '拉丁舞', 17, '拉丁舞是以运动肩部、腹部、腰部、臀部为主的一种舞蹈艺术。参加运动的包括腹直肌、腹内斜肌、腹外斜肌、竖脊肌、背阔肌等上百块肌肉。从上世纪60年代至今，许多科研人员对体育舞蹈的生理和心理作用做过研究，平均每跳一曲拉丁舞，腰部的扭转有160—180次。');
INSERT INTO `course` VALUES (3, '羽毛球', 17, '无论是进行有规则的羽毛球比赛还是作为一般性的健身活动，都要在场地上不停地进行脚步移动、跳跃、转体、挥拍，合理地运用各种击球技术和步法将球在场上往返对击，从而增大了上肢、下肢和腰部肌肉的力量，加快了锻炼者全身血液循环，增强了心血管系统和呼吸系统的功能。');
INSERT INTO `course` VALUES (4, '哑铃', 17, '可以锻炼上肢肌肉及腰、腹部肌肉。如做仰卧起坐的时候在颈后部双手紧握哑铃，可以增加腹肌练习的负荷；手握哑铃做体侧屈或转体运动，可以锻炼腹内、外斜肌；手握哑铃的直臂前举、侧平举等可以锻炼肩部和胸部肌肉.');
INSERT INTO `course` VALUES (5, '室内自行车', 16, '自行车是克服心脏功能毛病的最佳工具之一。世界上有半数以上的人是死于心脏病的。骑单车不只能藉腿部的运动压缩血液流动，以及把血液从血管末梢抽回心脏，事实上却同时强化了微血管组织，这叫“附带循环”。强化血管可以使你不受年龄的威胁，青春永驻。');
INSERT INTO `course` VALUES (6, '搏击', 17, '对于脂肪堆积过多的中青年人，特别是长时间久坐、致使脂肪堆积在腰腹部的办公一族而言，有氧搏击堪称效果十足的“瘦身”运动。因为它对人体体能的消耗特别大，对心肺功能的要求比较高。因为训练需要大量供血供氧，而在这一过程中，人体的脂肪细胞不断进行活化和分解代谢。');
INSERT INTO `course` VALUES (7, '跑步', 18, '跑步是一项有氧运动（跑步速度会影响心率，但一般而言认为跑步的心率应控制在有氧心率区间内），通过跑步，我们能提高肌力，令肌肉量适当地恢复正常的水平，同时提高体内的基础代谢水平，加速脂肪的燃烧，养成易瘦体质。');
INSERT INTO `course` VALUES (8, '篮球', 18, '篮球活动涵盖了跑、跳、投等多种身体运动形式，且运动强度较大，因此，它能全面、有效、综合地促进身体素质和人体机能的全面发展，保持和提高人的生命活力，为人的一切活动打下坚实的身体（物质）基础，从而提高生活的质量。');

-- ----------------------------
-- Table structure for depend
-- ----------------------------
DROP TABLE IF EXISTS `depend`;
CREATE TABLE `depend`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `course_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `course_id` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of depend
-- ----------------------------
INSERT INTO `depend` VALUES (1, 1, 5);
INSERT INTO `depend` VALUES (3, 4, 1);
INSERT INTO `depend` VALUES (6, 6, 1);
INSERT INTO `depend` VALUES (7, 1, 1);
INSERT INTO `depend` VALUES (8, 2, 1);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `notice_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '由于热水厂区域6月1日停汽，热水厂无法提供热水，特此通知：本俱乐部于6月1日全天洗浴用水暂停使用，由此给您带来的不便敬请谅解');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `phone_num` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `permission` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'user',
  `self_sign` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13212341234', 'user', '坚持健身');
INSERT INTO `user` VALUES (2, 'a1', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13112283210', 'user', '坚持健身');
INSERT INTO `user` VALUES (3, 'qw1', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13918567432', 'user', '坚持健身');
INSERT INTO `user` VALUES (4, 'admin', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13293710820', 'admin', '坚持健身');
INSERT INTO `user` VALUES (5, '12345', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13497976135', 'user', '坚持健身');
INSERT INTO `user` VALUES (6, '2', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13451330841', 'user', '坚持健身');
INSERT INTO `user` VALUES (7, '3', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13516321509', 'user', '坚持健身');
INSERT INTO `user` VALUES (8, 'd', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13900310141', 'user', '坚持健身');
INSERT INTO `user` VALUES (10, '2015', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '15235169715', 'admin', '坚持健身');
INSERT INTO `user` VALUES (11, '134', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13731250991', 'coach', '坚持健身');
INSERT INTO `user` VALUES (12, '333', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '15731344631', 'coach', '坚持健身');
INSERT INTO `user` VALUES (14, '151', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '15183151312', 'user', '坚持健身');
INSERT INTO `user` VALUES (15, '1234567', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13215928731', 'coach', '坚持健身');
INSERT INTO `user` VALUES (16, '1647', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '15123876453', 'coach', '坚持健身');
INSERT INTO `user` VALUES (17, '197', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13412415197', 'coach', '坚持健身');
INSERT INTO `user` VALUES (18, '111', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13832156312', 'coach', '坚持健身');
INSERT INTO `user` VALUES (19, 'aaa', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '13512358123', 'user', '努力');
INSERT INTO `user` VALUES (20, '14', 'pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', '15672332156', 'user', '坚持健身');

SET FOREIGN_KEY_CHECKS = 1;
