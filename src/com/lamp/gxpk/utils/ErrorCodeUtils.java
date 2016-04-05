package com.lamp.gxpk.utils;

/**显示访问错误信息的类
 * @author lgx
 *
 */
public class ErrorCodeUtils {
	
	/**获得错误信息
	 * @param code 错误码
	 * @return 错误信息
	 */
	public static String getErrorMessage(int code){
		String msg = null;
		switch (code) {
		case 9016:
			msg = "无网络连接，请检查您的手机网络";
			break;
		case 9010:
			msg = "网络超时";
			break;
		case 101:
			msg = "用户名或密码不正确,请重新输入";
			break;
		case 202:
			msg = "用户名已经存在";
			break;
		case 209:
			msg = "该手机号码已经存在";
			break;
		case 207:
			msg = "验证码错误，请重新输入";
			break;
		case 9018:
			msg = "选项不能为空";
			break;
		case 9019:
			msg = "存在不正确的格式选项，请查证！";
			break;
		case 210:
			msg = "旧密码不正确,请重新输入！";
			break;
		}
		return msg;
	}
}
