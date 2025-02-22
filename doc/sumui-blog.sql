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
    `open_id` varchar(50) NOT NULL COMMENT '微信用户appId',
    `nickname` varchar(10) DEFAULT NULL COMMENT '昵称',
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

create table sys_opt_log (
    id                bigint(20)      not null                   comment '日志主键',
    title             varchar(50)     default ''                 comment '模块标题',
    business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
    method            varchar(100)    default ''                 comment '方法名称',
    request_method    varchar(10)     default ''                 comment '请求方式',
    operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
    opt_name          varchar(50)     default ''                 comment '操作人员',
    dept_name         varchar(50)     default ''                 comment '部门名称',
    opt_url           varchar(255)    default ''                 comment '请求URL',
    opt_ip            varchar(128)    default ''                 comment '主机地址',
    opt_location      varchar(255)    default ''                 comment '操作地点',
    opt_param         varchar(2000)   default ''                 comment '请求参数',
    json_result       varchar(2000)   default ''                 comment '返回参数',
    status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
    error_msg         varchar(2000)   default ''                 comment '错误消息',
    opt_time          datetime                                   comment '操作时间',
    cost_time         bigint(20)      default 0                  comment '耗时间',
    primary key (id),
    key idx_business_type (business_type),
    key idx_status  (status),
    key idx_opt_time (opt_time)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci comment = '操作日志记录' ROW_FORMAT = Dynamic;

-- 任务分类表
CREATE TABLE task_categories (
    id varchar(20) NOT NULL COMMENT '分类ID',
    user_id varchar(20) NOT NULL COMMENT '用户ID',
    name varchar(50) NOT NULL COMMENT '分类名称',
    icon varchar(50) COMMENT '图标',
    color varchar(20) COMMENT '颜色',
    sort_order int DEFAULT 0 COMMENT '排序',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务分类表';

-- 任务表
CREATE TABLE task_todo (
    id varchar(20) NOT NULL COMMENT '任务ID',
    user_id varchar(20) NOT NULL COMMENT '用户ID',
    category_id varchar(20) COMMENT '分类ID',
    title varchar(100) NOT NULL COMMENT '任务标题',
    description text COMMENT '任务描述',
    icon varchar(50) COMMENT '图标',
    color varchar(20) COMMENT '颜色',
    start_time datetime COMMENT '开始时间',
    end_time datetime COMMENT '结束时间',
    task_date date NOT NULL COMMENT '任务日期',
    repeat_type ENUM('none', 'daily', 'weekly', 'monthly', 'yearly') DEFAULT 'none' COMMENT '重复类型',
    repeat_value JSON COMMENT '重复详细设置',
    priority int DEFAULT 0 COMMENT '优先级 0-低 1-中 2-高',
    status ENUM('pending', 'processing', 'completed', 'deleted') DEFAULT 'pending' COMMENT '状态',
    progress int DEFAULT 0 COMMENT '进度 0-100',
    completed_at datetime COMMENT '完成时间',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (category_id) REFERENCES task_categories(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务表';

-- 任务提醒表
CREATE TABLE task_reminders (
    id varchar(20) NOT NULL COMMENT '提醒ID',
    task_id varchar(20) NOT NULL COMMENT '任务ID',
    remind_time datetime NOT NULL COMMENT '提醒时间',
    remind_type ENUM('notification', 'email', 'sms') COMMENT '提醒方式',
    status ENUM('pending', 'sent', 'failed') DEFAULT 'pending' COMMENT '发送状态',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (task_id) REFERENCES task_todo(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务提醒表';

-- 新增任务评论表
CREATE TABLE task_comments (
    id varchar(20) NOT NULL COMMENT '评论ID',
    task_id varchar(20) NOT NULL COMMENT '任务ID',
    user_id varchar(20) NOT NULL COMMENT '用户ID',
    content text NOT NULL COMMENT '评论内容',
    parent_id varchar(20) COMMENT '父评论ID',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (task_id) REFERENCES task_todo(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务评论表';

-- 新增任务附件表
CREATE TABLE task_attachments (
    id varchar(20) NOT NULL COMMENT '附件ID',
    task_id varchar(20) NOT NULL COMMENT '任务ID',
    file_name varchar(255) NOT NULL COMMENT '文件名',
    file_path varchar(500) NOT NULL COMMENT '文件路径',
    file_size bigint COMMENT '文件大小(字节)',
    file_type varchar(50) COMMENT '文件类型',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (task_id) REFERENCES task_todo(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务附件表';

-- 任务标签表
CREATE TABLE task_tags (
    id varchar(20) NOT NULL COMMENT '标签ID',
    user_id varchar(20) NOT NULL COMMENT '用户ID',
    name varchar(50) NOT NULL COMMENT '标签名称',
    color varchar(20) COMMENT '标签颜色',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    update_by varchar(20) DEFAULT NULL COMMENT '更新人',
    del_flag tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
    PRIMARY KEY (id) USING BTREE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务标签表';

-- 任务-标签关联表
CREATE TABLE task_tag_relations (
    task_id varchar(20) NOT NULL COMMENT '任务ID',
    tag_id varchar(20) NOT NULL COMMENT '标签ID',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by varchar(20) DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (task_id, tag_id) USING BTREE,
    FOREIGN KEY (task_id) REFERENCES task_todo(id),
    FOREIGN KEY (tag_id) REFERENCES task_tags(id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT '任务-标签关联表';