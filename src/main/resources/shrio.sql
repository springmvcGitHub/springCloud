--Appuser表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appuser
-- ----------------------------
DROP TABLE IF EXISTS `appuser`;
CREATE TABLE `appuser`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inviteCode` bigint(255) NULL DEFAULT NULL,
  `nickName` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `userKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime NULL DEFAULT NULL,
  `userName` varchar(255) CHARACTER SET sjis COLLATE sjis_japanese_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET sjis COLLATE sjis_japanese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = sjis COLLATE = sjis_japanese_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;


--rule表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NULL DEFAULT NULL,
  `rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
