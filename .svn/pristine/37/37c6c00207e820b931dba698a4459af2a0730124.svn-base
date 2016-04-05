package com.lamp.gxpk.utils;

public class GetVideoTimeUtils {

	public static String getvideotime(long time) {

		// 计算音乐的总时长 显示格式为 :
		int miao = (int) (time / 1000);
		if (miao % 60 > 9) {
			return miao / 60 + "分" + miao % 60 + "秒";
		} else {
			return miao / 60 + "分0" + miao % 60 + "秒";
		}
	}

}
