package com.lamp.gxpk.utils;

public class GetVideoTimeUtils {

	public static String getvideotime(long time) {

		// �������ֵ���ʱ�� ��ʾ��ʽΪ :
		int miao = (int) (time / 1000);
		if (miao % 60 > 9) {
			return miao / 60 + "��" + miao % 60 + "��";
		} else {
			return miao / 60 + "��0" + miao % 60 + "��";
		}
	}

}
