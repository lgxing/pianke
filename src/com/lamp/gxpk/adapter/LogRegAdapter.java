package com.lamp.gxpk.adapter;

import java.util.List;

import com.lamp.gxpk.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class LogRegAdapter extends FragmentPagerAdapter {

	private List<BaseFragment> frags;

	public LogRegAdapter(FragmentManager fm, List<BaseFragment> frags) {
		super(fm);
		this.frags = frags;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return frags.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
