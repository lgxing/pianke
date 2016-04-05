package com.lamp.gxpk;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.lamp.gxpk.adapter.HomeVpAdapter;
import com.lamp.gxpk.adapter.LogRegAdapter;
import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseFragment;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.fragment.LoginFragment;
import com.lamp.gxpk.fragment.RegFragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LogRegActivity extends BaseActivity implements BaseInterface, OnClickListener {

	private ViewPager log_reg_vp;// 登录注册的ViewPager
	// 布局id
	private LinearLayout[] lr_lines = new LinearLayout[2];
	private int lr_linIds[] = { R.id.lin_log, R.id.lin_reg };
	// imageView id
	private ImageView log_img, reg_img;
	// 数据源 登录注册 fragment界面
	private List<BaseFragment> frags;
	// adapter
	private LogRegAdapter adapter;

	private ImageView qx;

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
		setContentView(R.layout.act_log_reg);

		log_img = findImg(R.id.log_img);
		reg_img = findImg(R.id.reg_img);
		qx = findImg(R.id.qx);

		// 初始化视图
		for (int i = 0; i < 2; i++) {
			lr_lines[i] = findLin(lr_linIds[i]);
			lr_lines[i].setOnClickListener(this);
		}

	}

	@Override
	public void initDatas() {

		// 加载 登录注册的fragment
		frags = new ArrayList<BaseFragment>();
		frags.add(new LoginFragment());// 登录
		frags.add(new RegFragment());// 注册
	}

	@Override
	public void initOper() {

		log_reg_vp = (ViewPager) findViewById(R.id.log_reg_vp);
		// 加载 adapter 数据源
		adapter = new LogRegAdapter(getSupportFragmentManager(), frags);
		log_reg_vp.setAdapter(adapter);
		log_reg_vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				updataView(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// 取消的操作
		qx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// 根据id 改变图片
		switch (v.getId()) {
		case R.id.lin_log:
			// 登录时 登录文字下边的三角显示 注册文字下边的三角隐藏

			updataView(0);

			log_reg_vp.setCurrentItem(0);
			break;
		case R.id.lin_reg:
			// 注册时 注册文字下边的三角显示 登录文字下边的三角隐藏
			updataView(1);

			log_reg_vp.setCurrentItem(1);
			break;

		default:
			break;
		}
	}

	private void updataView(int i) {

		if (i == 0) {
			log_img.setVisibility(View.VISIBLE);
			reg_img.setVisibility(View.INVISIBLE);
		} else if (i == 1) {
			reg_img.setVisibility(View.VISIBLE);
			log_img.setVisibility(View.INVISIBLE);
		}

	}

}
