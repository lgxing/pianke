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

	private EditText find_phone, find_codeet, find_pass;// �ֻ��� �� ��֤�� �� ����
	private LinearLayout find_code;// ��ȡ��֤��
	private TextView codetv;// ��֤�뵹��ʱ
	private Button but_findpass;// �޸�ȷ����ť
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

		setContentView(R.layout.act_findpass);// ���ز���
		find_phone = findEt(R.id.find_phone);// ע�� �ֻ���
		find_pass = findEt(R.id.find_pass);// ע�� ����
		find_codeet = findEt(R.id.find_codeet);// ��֤��
		find_code = findLin(R.id.find_code);// ��ȡ��֤��
		codetv = findTv(R.id.codetv);// ��֤������
		but_findpass = findBut(R.id.but_findpass);// �޸�ȷ����ť
		findpass_back = findImg(R.id.findpass_back);// ����

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

		/**
		 * �ֻ���֤���ȡ
		 */
		find_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone_et = find_phone.getText().toString().trim();

				// �ֻ��Ÿ�ʽ��֤
				if (!phone_et.matches("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastShort("�ֻ������ʽ�д���Ŷ��");
					return;
				}

				// ��֤����ȴ��
				find_code.setClickable(false);// ���ò��ɵ��
				find_code.setBackgroundResource(R.drawable.shape_code);// ���� ���ɵ���ı���
				// ����ʱ
				CountDownTimer count = new CountDownTimer(60 * 1000, 1000) {
					// ����ʱ
					@Override
					public void onTick(long millisUntilFinished) {
						codetv.setText(millisUntilFinished / 1000 + "s");
					}

					// ����ʱ����
					@Override
					public void onFinish() {
						// �ı���֤���
						find_code.setClickable(true);// ���� �ɵ��
						find_code.setBackgroundResource(R.drawable.selector_code);// ����
																					// �ɵ���ı���
						codetv.setText("��ȡ��֤��");// �ı�����
						codetv.setTextColor(Color.parseColor("#ffffff"));// �ı�������ɫ
					}
				};
				count.start();// ��ʼ����ʱ
				// ��ȡ��֤��
				BmobSMS.requestSMSCode(getAct(), phone_et, "��ЦƬ��", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						// TODO Auto-generated method stub
						if (ex == null) {// ��֤�뷢�ͳɹ�
							Log.i("smile", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
						}
					}
				});
			}
		});

		/**
		 * �һ�����Ĳ���
		 */
		but_findpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ��ȡ �û���д�� ��Ϣ����
				// String phone_find = find_phone.getText().toString().trim();
				String password_find = find_pass.getText().toString().trim();// ������
				final String code_find = find_codeet.getText().toString().trim();// ��֤��

				BmobUser.resetPasswordBySMSCode(getAct(), code_find, password_find, new ResetPasswordByCodeListener() {

					@Override
					public void done(cn.bmob.v3.exception.BmobException ex) {

						if (ex == null) {
							Log.i("smile", "�������óɹ�");
							toastShort("�������óɹ�,�������������¼��");
							finish();
						} else {
							Log.i("smile", "����ʧ�ܣ�code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
							toastShort("��������ʧ��" + code_find);
						}
					}
				});

			}
		});

		// ���صĲ���
		findpass_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}