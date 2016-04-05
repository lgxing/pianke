package com.lamp.gxpk.fragment;

import com.lamp.gxpk.R;
import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.base.BaseFragment;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.UserBean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;

public class RegFragment extends BaseFragment implements BaseInterface {

	private EditText reg_phone, reg_codeet, reg_pass;// 手机号 、 验证码 、 密码
	private LinearLayout reg_code;// 获取验证码
	private TextView code;// 验证码倒计时
	private Button but_reg;// 注册按钮

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initOper();
	}

	@Override
	public void initViews() {

		reg_phone = findEt(R.id.reg_phone);// 注册 手机号
		reg_pass = findEt(R.id.reg_pass);// 注册 密码
		reg_codeet = findEt(R.id.reg_codeet);// 验证码
		reg_code = findLin(R.id.reg_code);// 获取验证码
		code = findTv(R.id.code);// 验证码文字
		but_reg = findBut(R.id.but_reg);// 注册按钮

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

		/**
		 * 手机验证码获取
		 */
		reg_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone_et = reg_phone.getText().toString().trim();

				// 手机号格式验证
				if (!phone_et.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastShort("手机号码格式有错误哦！");
					return;
				}

				// 验证码冷却中
				reg_code.setClickable(false);// 设置不可点击
				reg_code.setBackgroundResource(R.drawable.shape_code);// 设置
																		// 不可点击的背景
				// 倒计时
				CountDownTimer count = new CountDownTimer(60 * 1000, 1000) {
					// 倒计时
					@Override
					public void onTick(long millisUntilFinished) {
						code.setText(millisUntilFinished / 1000 + "s");
					}

					// 倒计时结束
					@Override
					public void onFinish() {
						// 改变验证码框
						reg_code.setClickable(true);// 设置 可点击
						reg_code.setBackgroundResource(R.drawable.selector_code);// 设置可点击的背景
						code.setText("获取验证码");// 改变文字
						code.setTextColor(Color.parseColor("#ffffff"));// 改变文字颜色
					}
				};
				count.start();// 开始倒计时

				BmobSMS.requestSMSCode(getActivity(), phone_et, "搞笑片客验证码", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// 验证码发送成功
							toastShort("正在努力的获取验证码~~");
						}
					}
				});
			}
		});

		/**
		 * 注册的操作
		 */
		but_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取 用户填写的 信息数据
				final String phone_reg = reg_phone.getText().toString().trim();// 手机号
				final String password_reg = reg_pass.getText().toString().trim();// 密码
				String code_reg = reg_codeet.getText().toString().trim();// 验证码

				// DialogUtils.showDialog(getAct(), null, null, false);

				/**
				 * 验证验证码
				 */
				BmobSMS.verifySmsCode(getActivity(), phone_reg, code_reg, new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {// 短信验证码已验证成功
							// 把数据存入Bmop服务器
							UserBean user = new UserBean();

							user.setUsername(phone_reg);// 用户昵称 默认手机号
							user.setMobilePhoneNumber(phone_reg); // 手机号
							user.setPassword(password_reg);// 密码
							user.setMobilePhoneNumberVerified(true);// 绑定手机号
							user.signUp(getActivity(), new SaveListener() {// 注册用户

								@Override
								public void onSuccess() {
									toastShort("注册成功~快去看看有哪些搞笑视频吧~~");
									// 把 注册用户 缓存到 本地 用于返回
									MyApplication.putData("reg_phone", phone_reg);
									MyApplication.putData("reg_password", password_reg);
								}

								@Override
								public void onFailure(int arg0, String arg1) {
									toastShort("您没有注册成功哦~再检查检查~" +arg0  +"...."+arg1);
									
									
									
								}
							});

						} else {
							toastShort("验证码不正确");
						}
					}
				});
			}
		});

	}

	@Override
	public View getFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_reg, null);
	}

}
