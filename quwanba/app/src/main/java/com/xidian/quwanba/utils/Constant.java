package com.xidian.quwanba.utils;

/**
 * 
 * @author WuMeng
 * @version 1.0
 * @date 2012-10-30
 * @Description 静态常量类
 * 
 */
public class Constant {

	public static final String RIGHT_TEXT_COLOR = "#03C3DA";
	
	/** 百度地图的KEY */
	public static final String BAIDU_MAP_KEY = "1E32C61B24D75BFBB631E1BF07609AB6039F860C";
	//public static final String BAIDU_MAP_KEY = "UdGG1HqRfyzZ6NdiNucbc1ev";
	

	/** DEBUG时模拟网络延迟的时间 */
	public static final int DEBUG_NET_THREAD_SLEEP_TIME = 3000;

	/** 地图控制布局点击后消失的按钮时间间隔 */
	public static final int HIDE_MAP_CONTROL_LAYOUT_TIME = 3000;

	/** 用户头像限制 - X/Y比例 - X */
	public static final int REG_HEAD_IMG_ASPECT_X = 1;

	/** 用户头像限制 - X/Y比例 - Y */
	public static final int REG_HEAD_IMG_ASPECT_Y = 1;

	/** 用户头像限制 - 宽高 - X */
	public static final int REG_HEAD_IMG_OUTPUT_X = 400;

	/** 用户头像限制 - 宽高 - Y */
	public static final int REG_HEAD_IMG_OUTPUT_Y = 400;

	/** 用户头像限制 - 宽高 - X */
	public static final int REG_PHOTO_IMG_OUTPUT_X = 1000;

	/** 用户头像限制 - 宽高 - Y */
	public static final int REG_PHOTO_IMG_OUTPUT_Y = 1000;

	/** 同兴趣的人列表默认每次获取数据条数 */
	public static final int ALIKE_NEED_GET_DATE_SIZE = 15;

	/** 联系人列表默认每次获取数据条数 */
	public static final int CONTACTS_GET_DATE_SIZE = 15;

	/** 搜索用户默认每次获取数据条数 */
	public static final int SEARCH_USER_GET_DATE_SIZE = 10;

	/** 查看位置默认每次获取数据条数 */
	public static final int TEAM_LIST_GET_DATE_SIZE = 10;

	/** 行程列表默认每次获取数据条数 */
	public static final int DESTINATION_GET_DATE_SIZE = 10;

	/** 响应列表默认每次获取数据条数 */
	public static final int RESPONSE_GET_DATE_SIZE = 10;

	/** 意向列表默认每次获取数据条数 */
	public static final int JOINED_PERSON_GET_DATE_SIZE = 10;

	/** 选择用户地列表默认每次获取数据条数 */
	public static final int CHOOSE_USER_LIST_GET_DATE_SIZE = 10;

	/** 随便看看列表默认每次获取数据条数 */
	public static final int LOOK_AROUND_GET_DATE_SIZE = 10;

	/** 系统消息列表默认每次获取数据条数 */
	public static final int MESSAGE_GET_DATE_SIZE = 10;

	/** 行程列表默认每次获取数据条数 */
	public static final int ROUTE_GET_DATE_SIZE = 10;

	/** 邀请聊天中用户列表默认每次获取数据条数 */
	public static final int INVITE_MEMBNER_GET_DATE_SIZE = 20;

	/** 文本编辑框中编辑的内容的最大字数 */
	public static final int COMMON_PUBLISH_DESCRIPTION_MAX_LENGTH = 120;

	/** 每隔多少秒更新自己的位置- 毫秒 */
	public static final long UPDATE_USER_LOCATION_INTERVAL_TIME = 60000;

	/** 发布行程时最大地名数 */
	public static final int PUBLISH_ROUTE_MAX_PLACE = 5;

	// 登陆的返回码
	/** 登陆成功 */
	///---public static final int LOGIN_BACK_SUCCEED = 1;
	public static final int LOGIN_BACK_SUCCEED = 0;     ///0表示成功
	/** 老用户登陆成功 */
	public static final int SERVER_BACK_CODE_SUCC_BY_OLD = 10000;     ///0表示成功
	/** 用户登录信息为空 */
	public static final int LOGIN_BACK_INFO_NULL = 2;
	/** 网络错误 */
	public static final int LOGIN_BACK_NET_WORK_ERROR = 3;
	/** XMPP服务器错误 */
	public static final int LOGIN_BACK_XMPP_ERROR = 4;
	/** 系统错误 */
	public static final int LOGIN_BACK_SYSTEM_ERROR = 5;
	/** 服务器无响应 */
	public static final int LOGIN_BACK_SERVER_RESPONSE = 6;
	/** XMPP服务器登录成功 */
	public static final int LOGIN_BACK_XMPP_SERVER_SUCC = 7;
	
	public static final int LOCATION_CITY_CHANGED = 8;    ///城市改变
	
	public static final int BIND_BAIDU_PUSH_SUCC = 8;   ///绑定百度推送
	public static final int BIND_BAIDU_PUSH_ERR  = 9;   ///绑定百度推送
	public static final int LOGIN_OFFLINE        = 10;  ///执行离线登录
	
	public static final int NET_AVAILABLE        = 11;   ///网络可用
	public static final int NET_UNAVAILABLE      = 12;   ///网络不可用
	
	public static final int LOCATE_SUCC          = 13;   ///定位成功
	public static final int LOCATE_ERROR         = 14;   ///定位失败
	
	public static final int REVERSE_GEO_SUCC     = 15;   ///反查地址成功
	public static final int REVERSE_GEO_ERROR    = 16;   ///反查地址失败
	

	// 省市等级
	/** 表示省市等级 - 省级 */
	public static final int PROVINCE_CITY_MODEL_LEVEL_PROVINCE = 1;
	/** 表示省市等级 - 市级 */
	public static final int PROVINCE_CITY_MODEL_LEVEL_CITY = 2;
	/** 邀请好友短信内容 */
	public static final String SMS_CONTENT = "Hi，我最近下载安装了搭伴玩android版，很方便出去玩的时候找朋友一起哦，推荐你试试。http://www.dabanwan.com/data/dabanwan.apk";

	/** 初次绑定腾讯微博文字内容 */
	public static final String TX_WEIBO_FIRST_BIND = "我刚刚安装了结伴旅游神器「搭伴玩」，以后出去玩就不愁找不到人一起了。朋友们也安装一个一起玩啊，装好记的加我。http://www.dabanwan.com/app.html";
	
	/** 手势——向左 **/
	public static final String GESTURE_FINGER_TO_LEFT = "finger_left";
	
	/** 手势——向右 **/
	public static final String GESTURE_FINGER_TO_RIGHT = "finger_right";
	
	///+++ 用于图像查看yd 2014-11-20
	public static final String IMAGES = "Images";
	public static final String IMAGE_IDS = "Image_IDs";
	public static final String IMAGE_POSITION = "ImagePosition";
	public static final String IS_LOCAL_IMAGE = "isLocalImage";
	public static final String CAN_DEL_PHOTO = "canDelPhoto";   //支持删除照片
	
	public static final int SCREEN_PADDING = 13;   ///屏幕边宽像素
	
	///友盟描述符
	public static final String DESCRIPTOR = "com.umeng.share";
}
