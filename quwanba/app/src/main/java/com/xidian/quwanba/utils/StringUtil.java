package com.xidian.quwanba.utils;

import android.os.Environment;


import java.io.File;
import java.util.Random;

/**
 * 
 * @author WuMeng
 * @version 1.0
 * @date 2012-05-02
 * @Description 摘自 org.apache.commons.lang.StringUtil
 * 
 */
public class StringUtil {



	/**
	 * 判断字符串是否为空 Determines if a string is empty (<code>null</code> or
	 * zero-length).
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return ((string == null) || (string.length() == 0));
	}

	/**
	 * 判断字符串是否不为空 Determines if a string is not empty.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNotEmpty(String string) {
		return string != null && string != "null" && string.length() > 0;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str.matches("\\d*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 随机获取一个Int数组中的数字
	 * 
	 * @param limits
	 * @return
	 */
	public static int getRandomNumFromArray(int[] limits) {
		Random random = new Random();
		return limits[random.nextInt(limits.length)];
	}

	/**
	 * 获取整形数组中一个对象的索引
	 * 
	 * @param array
	 * @param item
	 * @return
	 */
	public static int getIndexOfIntArray(int[] array, int item) {
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == item) {
				return i;
			}
		}
		return index;
	}

	
}
