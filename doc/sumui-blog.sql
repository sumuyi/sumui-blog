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

create table sys_oper_log (
    id                bigint(20)      not null                   comment '日志主键',
    title             varchar(50)     default ''                 comment '模块标题',
    business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
    method            varchar(100)    default ''                 comment '方法名称',
    request_method    varchar(10)     default ''                 comment '请求方式',
    operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name         varchar(50)     default ''                 comment '操作人员',
    dept_name         varchar(50)     default ''                 comment '部门名称',
    oper_url          varchar(255)    default ''                 comment '请求URL',
    oper_ip           varchar(128)    default ''                 comment '主机地址',
    oper_location     varchar(255)    default ''                 comment '操作地点',
    oper_param        varchar(2000)   default ''                 comment '请求参数',
    json_result       varchar(2000)   default ''                 comment '返回参数',
    status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
    error_msg         varchar(2000)   default ''                 comment '错误消息',
    oper_time         datetime                                   comment '操作时间',
    cost_time         bigint(20)      default 0                  comment '消耗时间',
    primary key (id),
    key idx_sys_oper_log_bt (business_type),
    key idx_sys_oper_log_s  (status),
    key idx_sys_oper_log_ot (oper_time)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci comment = '操作日志记录' ROW_FORMAT = Dynamic;