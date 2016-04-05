package com.lamp.gxpk.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lamp.gxpk.base.BaseFragment;

/**
 * 主页面ViewPager的Adapter
 * @author Administrator
 *
 */
public class HomeVpAdapter extends FragmentPagerAdapter {

	
	//ViewPager的数据源
	private List<BaseFragment> frags;
	
	public HomeVpAdapter(FragmentManager fm,List<BaseFragment> frags) {
		super(fm);
		this.frags = frags;
	}

	@Override
	public Fragment getItem(int arg0) {
		return frags.get(arg0);
	}

	@Override
	public int getCount() {
		return frags.size();
	}

	

}
