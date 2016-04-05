package com.lamp.gxpk;

import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePassActivity extends BaseActivity implements BaseInterface {

	// 返回
	@ViewInject(R.id.updapass_back)
	private ImageView updapass_back;
	// 旧密码
	@ViewInject(R.id.upda_old)
	private EditText upda_old;
	// 新密码
	@ViewInject(R.id.upda_new)
	private EditText upda_new;
	// 确定修改的按钮
	@ViewInject(R.id.but_upda)
	private Button but_upda;

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
		// 加载布局
		setContentView(R.layout.act_updapass);
		ViewUtils.inject(getAct());

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

	}

	// 修改密码的操作
	@OnClick(R.id.but_upda)
	public void updaOnClick(View view) {

		// 获取数据 旧密码 和新密码
		String old_p = upda_old.getText().toString().trim();
		String new_p = upda_new.getText().toString().trim();

		// 调用Bmob云的 修改密码
		BmobUser.updateCurrentUserPassword(getAct(), old_p, new_p, new UpdateListener() {

			@Override
			public void onSuccess() {
				toastShort("密码修改成功，可以用新密码进行登录啦");
				Log.i("smile", "密码修改成功，可以用新密码进行登录啦");
				Intent intent = new Intent(getAct(), LogRegActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				toastShort("密码修改失败");
				Log.i("smile", "密码修改失败：" + msg + "(" + code + ")");
			}
		});

	}

	// 返回 的操作
	@OnClick(R.id.updapass_back)
	public void backOnClick(View view) {
		finish();
	}

}
