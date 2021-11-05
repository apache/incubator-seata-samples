/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.seata.common.constant;

public interface CommonConstant {

    public static final String FORMAT_DATETIME_WITH_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    /**
     * 验证码原始字符
     */
    public static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;

    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_YZS_NO_AUTHZ = 510;

    /**
     * 没找到资源 404
     */
    public static final Integer SC_YZS_NOT_FOUND = 404;

    /**
     * 登录用户Token令牌缓存KEY前缀
     */
    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";
    /**
     * Token缓存时间：3600秒即一小时
     */
    public static final int TOKEN_EXPIRE_TIME = 10 * 3600;
    /**
     * 状态(0无效1有效)
     */
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";

    public final static String X_ACCESS_TOKEN = "X-Access-Token";

    /**
     * 多租户 请求头
     */
    public final static String TENANT_ID = "tenant_id";

    /**
     * 请求头 用户id
     */
    public final static String USER_ID = "user_id";

    /**
     * 请求头用户名
     */
    public final static String USERNAME = "username";

    /**
     * 微服务读取配置文件属性 服务地址
     */
    public final static String CLOUD_SERVER_KEY = "spring.cloud.nacos.discovery.server-addr";

    /**
     * 验证码缓存到期时间
     */
    public static final Long VERIFY_CODE_EXPIRED = 180L;

    /**
     * 用户账号已锁定
     */
    public static final String USER_ACCOUNT_STATUS_LOCKED = "1";

    /**
     * 用户账号正常
     */
    public static final String USER_ACCOUNT_STATUS_NORMAL = "0";

    /**
     * 租户状态正常
     */
    public static final String TENANT_STATUS_NORMAL = "1";
    /**
     * 租户状态禁用
     */
    public static final String TENANT_STATUS_DISABLE = "0";

    /**
     * 系统id
     */
    public static final String SYS_ID = "sys_id";
    /**
     * 终端id
     */
    public static final String TERM_ID = "term_id";

    /**
     * 账号最大会话数，为1表明一个账号同时只允许一个人登陆
     */
    public static final Integer ACCOUNT_MAX_SESSION = 1;

    /**
     * 发送短信topic
     */
    public static final String KAFKA_TOP_SEND_SMS_CODE = "SEND_SMS_CODE_TOPIC";

    /**
     * 日志处理topic
     */
    public static final String KAFKA_TOP_LOG_DEAL_TOPIC = "LOG_DEAL_TOPIC";

    /**
     * redis手机验证码缓存
     */
    public static final String PHONE_CODE_CACHE = "phone::code::cache::";

    /**
     * 账号类型-职工
     */
    public static final String ACCOUNT_TYPE_WORKER = "2";
    /**
     * 账号类型-用户
     */
    public static final String ACCOUNT_TYPE_USER = "1";

    /**
     * md5密码盐
     */
    public static final String PWD_SALT = "qwertyuiopasdfghjkl;dsy./123456798";
}
