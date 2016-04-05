package com.lamp.gxpk;

import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

public class ForgetPassActivity extends BaseActivity implements BaseInterface {

	private EditText find_phone, find_codeet, find_pass;// 手机号 、 验证码 、 密码
	private LinearLayout find_code;// 获取验证码
	private TextView codetv;// 验证码倒计时
	private Button but_findpass;// 修改确定按钮
	private ImageView findpass_back;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		initDatas();
		initOper();

	}

	@Override
	public void initViews() {

		setContentView(R.layout.act_findpass);// 加载布局
		find_phone = findEt(R.id.find_phone);// 注册 手机号
		find_pass = findEt(R.id.find_pass);// 注册 密码
		find_codeet = findEt(R.id.find_codeet);// 验证码
		find_code = findLin(R.id.find_code);// 获取验证码
		codetv = findTv(R.id.codetv);// 验证码文字
		but_findpass = findBut(R.id.but_findpass);// 修改确定按钮
		findpass_back = findImg(R.id.findpass_back);// 返回

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

		/**
		 * 手机验证码获取
		 */
		find_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone_et = find_phone.getText().toString().trim();

				// 手机号格式验证
				if (!phone_et.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastShort("手机号码格式有错误哦！");
					return;
				}

				// 验证码冷却中
				find_code.setClickable(false);// 设置不可点击
				find_code.setBackgroundResource(R.drawable.shape_code);// 设置 不可点击的背景
				// 倒计时
				CountDownTimer count = new CountDownTimer(60 * 1000, 1000) {
					// 倒计时
					@Override
					public void onTick(long millisUntilFinished) {
						codetv.setText(millisUntilFinished / 1000 + "s");
					}

					// 倒计时结束
					@Override
					public void onFinish() {
						// 改变验证码框
						find_code.setClickable(true);// 设置 可点击
						find_code.setBackgroundResource(R.drawable.selector_code);// 设置
																					// 可点击的背景
						codetv.setText("获取验证码");// 改变文字
						codetv.setTextColor(Color.parseColor("#ffffff"));// 改变文字颜色
					}
				};
				count.start();// 开始倒计时
				// 获取验证码
				BmobSMS.requestSMSCode(getAct(), phone_et, "搞笑片刻", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						// TODO Auto-generated method stub
						if (ex == null) {// 验证码发送成功
							Log.i("smile", "短信id：" + smsId);// 用于查询本次短信发送详情
						}
					}
				});
			}
		});

		/**
		 * 找回密码的操作
		 */
		but_findpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 获取 用户填写的 信息数据
				// String phone_find = find_phone.getText().toString().trim();
				String password_find = find_pass.getText().toString().trim();// 新密码
				final String code_find = find_codeet.getText().toString().trim();// 验证码

				BmobUser.resetPasswordBySMSCode(getAct(), code_find, password_find, new ResetPasswordByCodeListener() {

					@Override
					public void done(cn.bmob.v3.exception.BmobException ex) {

						if (ex == null) {
							Log.i("smile", "密码重置成功");
							toastShort("密码重置成功,可以用新密码登录了");
							finish();
						} else {
							Log.i("smile", "重置失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
							toastShort("密码重置失败" + code_find);
						}
					}
				});

			}
		});

		// 返回的操作
		findpass_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
