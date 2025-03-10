package com.sumui.common.constants;
import lombok.Getter;

/**
 * 异常码规范：
 * xxx - xxx - xxx
 * 业务 - 状态 - code
 * <p>
 * 业务取值
 * - 100 全局
 * - 200 文章相关
 * - 300 评论相关
 * - 400 用户相关
 * <p>
 * 状态：基于http status的含义
 * - 4xx 调用方使用姿势问题
 * - 5xx 服务内部问题
 * <p>
 * code: 具体的业务code
 *
 * @Description
 * @Author @Sunl
 * @Date 2023/12/5 19:10
 */
@Getter
public enum StatusEnum {
    SUCCESS(200, "OK"),
    FAIL(501, "fail"),

    // -------------------------------- 通用

    // 全局传参异常
    ILLEGAL_ARGUMENTS(100_400_001, "参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(100_400_002, "参数异常"),

    // 全局权限相关
    FORBID_ERROR(100_403_001, "无权限"),

    FORBID_ERROR_MIXED(100_403_002, "无权限"),
    FORBID_NOTLOGIN(100_403_003, "未登录"),

    // 全局，数据不存在
    RECORDS_NOT_EXISTS(100_404_001, "记录不存在"),

    // 系统异常
    UNEXPECT_ERROR(100_500_001, "非预期异常"),

    // 图片相关异常类型
    UPLOAD_PIC_FAILED(100_500_002, "图片上传失败！"),

    // --------------------------------

    // 文章相关异常类型，前缀为200
    ARTICLE_NOT_EXISTS(200_404_001, "文章不存在"),
    COLUMN_NOT_EXISTS(200_404_002, "教程不存在"),
    COLUMN_QUERY_ERROR(200_500_003, "教程查询异常"),
    // 教程文章已存在
    COLUMN_ARTICLE_EXISTS(200_500_004, "专栏教程已存在"),
    ARTICLE_RELATION_TUTORIAL(200_500_006, "文章已被添加为教程"),

    // --------------------------------

    // 评论相关异常类型
    COMMENT_NOT_EXISTS(300_404_001, "评论不存在"),


    // --------------------------------

    // 用户相关异常
    LOGIN_FAILED_MIXED(400_403_001, "登录失败"),
    USER_NOT_EXISTS(400_404_001, "用户不存在"),
    USER_EXISTS(400_404_002, "用户已存在"),
    // 用户登录名重复
    USER_LOGIN_NAME_REPEAT(400_404_003, "用户登录名重复"),

    USER_DISABLE(400_405_001, "用户已停用"),
    USER_DELETE(400_405_002, "用户已删除"),

    // 待审核
    USER_NOT_AUDIT(400_500_001, "用户未审核"),
    USER_PWD_ERROR(400_500_002, "用户名密码错误"),
    USER_OR_PWD_EMPTY(400_500_003, "用户名or密码不能为空"),

    // 账单相关错误码 (3000-3999)
    BILL_AMOUNT_INVALID(3000, "账单金额无效"),
    BILL_DATE_INVALID(3001, "账单日期无效"),
    BILL_CATEGORY_EMPTY(3002, "账单类别不能为空"),
    BILL_TYPE_INVALID(3003, "账单类型无效"),
    BILL_NOT_FOUND(3004, "账单记录不存在"),
    BILL_SAVE_ERROR(3005, "保存账单失败"),

    // 账本成员相关错误码 (4000-4999)
    BOOKS_MEMBERS_UNAUTHORIZED(4000, "您没有权限添加账本成员"),
    BOOKS_MEMBERS_NOT_FOUND(4001, "该用户不在该账本中"),
    BOOKS_MEMBERS_EXISTS(4002, "该用户已在该账本中"),
    BOOKS_MEMBERS_NOT_AUTHORIZED(4003, "您没有权限访问该用户"),

    // 账本相关错误码 (5000-5999)
    BOOKS_NOT_FOUND(5000, "账本不存在"),
    BOOKS_CREATE_FAILED(5001, "创建账本失败"),
    BOOKS_UPDATE_FAILED(5002, "更新账本失败"),
    BOOKS_DELETE_FAILED(5003, "删除账本失败"),
    BOOKS_NOT_OWNER(5004, "您不是该账本的所有者"),
    BOOKS_NOT_MEMBER(5005, "您不是该账本的成员"),
    BOOKS_NOT_AUTHORIZED(5006, "您没有权限访问该账本"),
    BOOKS_NOT_ENOUGH_MONEY(5007, "余额不足"),
    ;

    private final Integer code;

    private final String msg;

    StatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean is5xx(Integer code) {
        return code % 1000_000 / 1000 >= 500;
    }

    public static boolean is403(Integer code) {
        return code % 1000_000 / 1000 == 403;
    }

    public static boolean is4xx(Integer code) {
        return code % 1000_000 / 1000 < 500;
    }
}
