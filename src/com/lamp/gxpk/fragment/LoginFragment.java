package com.lamp.gxpk.fragment;

import com.lamp.gxpk.ForgetPassActivity;
import com.lamp.gxpk.HomeActivity;
import com.lamp.gxpk.R;
import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.base.BaseFragment;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.UserBean;
import com.lamp.gxpk.utils.ErrorCodeUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginFragment extends BaseFragment implements BaseInterface {

	private EditText log_phone, log_pass;// �û���������
	private Button but_log;// ��¼��ť
	private TextView forget_pass;// ��������

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

		log_phone = findEt(R.id.log_phone);// �ֻ���
		log_pass = findEt(R.id.log_pass);// ����
		but_log = findBut(R.id.but_log);// ��¼��ť
		forget_pass = findTv(R.id.forget_pass);// ��������
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

		/**
		 * ��¼����
		 */

		but_log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ��ȡ����
				String phone_et = log_phone.getText().toString().trim(); // �ֻ���
				String pass_et = log_pass.getText().toString().trim(); // ����

				// DialogUtils.showDialog(getAct(), null, null, false);
				BmobUser.loginByAccount(getActivity(), phone_et, pass_et, new LogInListener<UserBean>() {

					@Override
					public void done(UserBean user, BmobException e) {
						// TODO Auto-generated method stub
						if (user != null) {
							toastShort("��¼�ɹ�");
							// �����¼���û�
							getActivity().finish();
						} else {

							toastShort(ErrorCodeUtils.getErrorMessage(e.getErrorCode()));
							
						}
					}
				});

			}
		});

		/**
		 * ��������
		 */
		forget_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ת�� �һ��������
				Intent intent = new Intent(getActivity(), ForgetPassActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// ���ڻ��� ע���û����˺ź�����
		String phone_reg = (String) MyApplication.getData("reg_phone", true);
		String password_reg = (String) MyApplication.getData("reg_password", true);

		if (phone_reg != null && password_reg != null) {
			log_phone.setText(phone_reg);
			log_pass.setText(password_reg);
		}

	}

	@Override
	public View getFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_login, null);
	}

}