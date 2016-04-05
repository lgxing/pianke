package com.lamp.gxpk;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.lamp.gxpk.adapter.HomeVpAdapter;
import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.application.MyApplication.OnFinishListener;
import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseFragment;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.UserBean;
import com.lamp.gxpk.bean.VideoBean;
import com.lamp.gxpk.fragment.HomeVp2Fragment;
import com.lamp.gxpk.fragment.HomeVpFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * 首页Activity
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends BaseActivity implements BaseInterface {

	//用户登录的界面
	@ViewInject(R.id.act_home_lin_yes)
	private RelativeLayout loginYes;
	//用户未登录的界面
	@ViewInject(R.id.act_home_lin_no)
	private RelativeLayout loginNo;
	
	// 主页面的ViewPager
	@ViewInject(R.id.act_home_vp)
	private ViewPager vp;
	// 发现的文字
	@ViewInject(R.id.act_home_find)
	private TextView find;
	// 关注的文字
	@ViewInject(R.id.act_home_guanzhu)
	private TextView guanzhu;
	// ViewPager的ItemFragment
	private List<BaseFragment> frags;
	// ViewPager的Adapter
	private FragmentPagerAdapter adapter;
	// Fragment管理类
	private FragmentManager fm;
	// 可滑动的菜单
	private ResideMenu menu;
	// 设置标记判断用户是否要退出
	private boolean state = false;
	//发现的数据源
	private List<VideoBean> datas;
	//当前登录的用户
	private UserBean ub;

	// 菜单选项
	private ResideMenuItem item1;
	private ResideMenuItem item2;
	private ResideMenuItem item3;
	private ResideMenuItem item4;
	private ResideMenuItem item5;
	private ResideMenuItem item6;
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == item1) {
				// 个人中心
				Intent intent = new Intent(getAct(), UserInfoActivity.class);
				intent.putExtra("uId", ub.getObjectId());
				startActivity(intent);
			} else if (v == item2) {
				// 修改密码
				Intent intent = new Intent(getAct(), UpdatePassActivity.class);
				startActivity(intent);
			} else if (v == item3) {
				// 我的下载
				Intent intent = new Intent(getAct(), LocalVideoActivity.class);
				intent.putExtra("code", 2);
				startActivity(intent);
			} else if (v == item4) {
				// 本地作品
				Intent intent = new Intent(getAct(), LocalVideoActivity.class);
				intent.putExtra("code", 1);
				startActivity(intent);
			} else if (v == item5) {
				// 清除缓存
				
			} else {
				// 退出登录
				BmobUser.logOut(getAct());
				finish();
			}
			// 点击完毕关闭菜单
			menu.closeMenu();
		}
	};

	// 返回menu供子布局操作
	public ResideMenu getMenu() {
		return menu;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initDatas();
		initOper();
	}

	// 初始化控件
	@Override
	public void initViews() {
		setContentView(R.layout.act_home);
		ViewUtils.inject(getAct());
	}

	// 初始化数据
	@SuppressWarnings("unchecked")
	@Override
	public void initDatas() {
		//设置ViewPager的数据源
		frags = new ArrayList<BaseFragment>();
		datas = (List<VideoBean>) MyApplication.getData("initData", true);
		if(datas!=null){
			HomeVpFragment frag1 = new HomeVpFragment();
			frag1.setDatas(datas);
			frags.add(frag1);
			HomeVp2Fragment frag2 = new HomeVp2Fragment();
			frags.add(frag2);
			fm = getSupportFragmentManager();
			adapter = new HomeVpAdapter(fm, frags);
			vp.setAdapter(adapter);
		}
		//设置Application加载数据的监听
		((MyApplication)(getApplication())).setFinishListener(new OnFinishListener() {
			
			@Override
			public void onFinish(List<VideoBean> arg0) {
				datas = arg0;
				HomeVpFragment frag1 = new HomeVpFragment();
				frag1.setDatas(arg0);
				frags.add(frag1);
				HomeVp2Fragment frag2 = new HomeVp2Fragment();
				frags.add(frag2);
				fm = getSupportFragmentManager();
				adapter = new HomeVpAdapter(fm, frags);
				vp.setAdapter(adapter);
			}
		});
		//获取本地用户
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		if(ub==null){
			//如果没有登录则显示未登录界面
			loginYes.setVisibility(View.INVISIBLE);
			loginNo.setVisibility(View.VISIBLE);
		}else{
			//如果已经登录则显示登录界面
			loginYes.setVisibility(View.VISIBLE);
			loginNo.setVisibility(View.INVISIBLE);
		}
	}

	// 初始化操作
	@Override
	public void initOper() {
		// 初始化菜单
		menu = new ResideMenu(this);
		menu.setUse3D(true);// 使用3D效果
		menu.setBackground(R.drawable.homeback);//设置黑色背景
		menu.attachToActivity(this);// 将菜单绑定到Activity
		menu.setScaleValue(0.7f);// 设置缩放程度
		menu.addIgnoredView(vp);//添加忽略区域(滑动此处不会弹出菜单)

		// 创建菜单项
		item1 = new ResideMenuItem(this, R.drawable.gerenzhongxin, "个人中心");
		item2 = new ResideMenuItem(this, R.drawable.xiugaimima, "修改密码");
		item3 = new ResideMenuItem(this, R.drawable.wodexiazai, "我的下载");
		item4 = new ResideMenuItem(this, R.drawable.bendizuopin, "本地作品");
		item5 = new ResideMenuItem(this, R.drawable.qingchuhuancun, "清除缓存");
		item6 = new ResideMenuItem(this, R.drawable.tuichudenglu, "退出登录");

		// 设置菜单项的监听事件
		item1.setOnClickListener(listener);
		item2.setOnClickListener(listener);
		item3.setOnClickListener(listener);
		item4.setOnClickListener(listener);
		item5.setOnClickListener(listener);
		item6.setOnClickListener(listener);

		// 将菜单项添加进菜单
		menu.addMenuItem(item1, ResideMenu.DIRECTION_LEFT);
		menu.addMenuItem(item2, ResideMenu.DIRECTION_LEFT);
		menu.addMenuItem(item3, ResideMenu.DIRECTION_LEFT);
		menu.addMenuItem(item4, ResideMenu.DIRECTION_LEFT);
		menu.addMenuItem(item5, ResideMenu.DIRECTION_LEFT);
		menu.addMenuItem(item6, ResideMenu.DIRECTION_LEFT);
		// 设置右边为不可划出
		menu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		// ViewPager的监听事件
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					//修改顶部标题的颜色
					find.setTextColor(Color.parseColor("#ffffff"));
					guanzhu.setTextColor(Color.parseColor("#e2e2e2"));
				} else {
					guanzhu.setTextColor(Color.parseColor("#ffffff"));
					find.setTextColor(Color.parseColor("#e2e2e2"));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		//获取本地用户
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		if(ub==null){
			loginYes.setVisibility(View.INVISIBLE);
			loginNo.setVisibility(View.VISIBLE);
		}else{
			loginYes.setVisibility(View.VISIBLE);
			loginNo.setVisibility(View.INVISIBLE);
		}
		//初始化数据
		initDatas();
	}

	// 点击菜单键弹出菜单
	@OnClick(R.id.act_home_menu)
	public void onMenuClick(View v) {
		menu.openMenu(ResideMenu.DIRECTION_LEFT);
	}

	// 点击录制键调到录制界面
	@OnClick(R.id.act_home_record)
	public void onRecordClick(View v) {
		Intent intent = new Intent(getAct(), RecordVideoActivity.class);
		startActivity(intent);
	}
	
	//登录按钮跳转到登录界面
	@OnClick(R.id.act_home_login)
	public void onLoginClick(View v){
		Intent intent = new Intent(getAct(), LogRegActivity.class);
		startActivity(intent);
	}

	// 返回键的监听
	@Override
	public void onBackPressed() {
		if (!state) {
			toastShort("再次按返回键退出");
			state = true;
			//计时三秒如果没有退出就恢复状态
			new CountDownTimer(3000, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
				}

				@Override
				public void onFinish() {
					state = false;
				}
			}.start();
		} else {
			super.onBackPressed();
		}

	}

}
