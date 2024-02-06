/*
 Navicat Premium Data Transfer

 Source Server         : 天翼贵州3
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : localhost:3306
 Source Schema         : sumui_blog

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 12/22/2023 10:28:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
    `id` varchar(20) NOT NULL COMMENT '主键ID',
    `nickname` varchar(10) NOT NULL COMMENT '昵称',
    `username` varchar(20) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `salt` varchar(500) NOT NULL COMMENT '加密盐',
    `mobile` varchar(20) DEFAULT NULL COMMENT '电话',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '账号状态 1：停用 0:正常',
    `is_super` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是超级管理员 1：是 0:否',
    `dept_id` varchar(20) DEFAULT NULL COMMENT '部门ID',
    `login_ip` varchar(128) DEFAULT NULL COMMENT '登录IP',
    `login_time` datetime DEFAULT NULL COMMENT '登录时间',
    `last_active_time` datetime DEFAULT NULL COMMENT '最后一次活跃时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
    `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uni_username` (`username`) USING BTREE COMMENT '登录名唯一'
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;