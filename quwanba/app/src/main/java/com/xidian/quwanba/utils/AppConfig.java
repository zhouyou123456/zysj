package com.xidian.quwanba.utils;

import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.SparseArray;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author WuMeng
 * @version 1.0
 * @date 2012-10-30
 * @Description 程序参数配置
 * 
 */
public class AppConfig {

	/** 屏幕宽度（像素） */
	public static int phoneWidth = 0;

	/** 屏幕高度（像素） */
	public static int phoneHeight = 0;

	/** 屏幕密度（0.75 / 1.0 / 1.5） */
	public static float phoneDensity = 0;

	/** 屏幕密度DPI（120 / 160 / 240） */
	public static int phoneDPI = 0;

	/** 头像的保存路径 */
	public static final String headImgSavePath = Environment.getExternalStorageDirectory()
			+ File.separator + "HeadImage.jpg";


}
