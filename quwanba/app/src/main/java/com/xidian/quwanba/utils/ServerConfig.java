package com.xidian.quwanba.utils;

public class ServerConfig {
    public static final String SERVER_URL="http://120.76.173.177:8080";

    // +++获取短信验证码
    public static final String REG_GET_SMS_CODE = SERVER_URL + "/Travel/api/guestUser/sendSms";
    // +++注册
    public static final String REG_REGISTER = SERVER_URL + "/Travel/api/guestUser/regist";

    // +++获取路线列表
    public static final String GET_ROUTELIST = SERVER_URL + "/Travel/api/routeManage/list";

    // +++获取路线详情
    public static final String GET_ROUTEDETAIL = SERVER_URL + "/Travel/api/routeManage/routeDetail";

    // +++登录
    public static final String LOGIN = SERVER_URL + "/Travel/api/guestUser/login";

    // +++上传用户信息
    public static final String UOLOAD_USERINDFO = SERVER_URL + "/Travel/api/guestUser/uploadGuestInfo";

    // +++二维码跳转
    public static final String USER_CODE = SERVER_URL + "/Travel/app/register.html";

    // +++我的详情
    public static final String USER_INFO = SERVER_URL + "/Travel/api/guestUser/guestUserInfoById";

    // +++校验码
    public static final String GET_CHECK_CODE = SERVER_URL + "/Travel/api/guestUser/getNumber";

    // +++忘记密码
    public static final String GET_PASSWORD = SERVER_URL + "/Travel/api/guestUser/forgetpwd";


}
