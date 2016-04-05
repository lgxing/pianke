package com.lamp.gxpk.pulltorefresh;


import com.lamp.gxpk.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import android.os.Handler;
import android.os.Message;

public class MyListener implements OnRefreshListener
{

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout)
	{
		
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		
		/*	// 下拉刷新操作
		new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// 千万别忘了告诉控件刷新完毕了哦！
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 5000);*/
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout)
	{
	
		
//		// 加载操作
//		new Handler()
//		{
//			@Override
//			public void handleMessage(Message ms1g)
//			{
//				// 千万别忘了告诉控件加载完毕了哦！
//				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//				
//				Message msg = new Message();
//				msg.what = MainActivity.UPDATE_GRIDVIEW_LOAD_MORE;
//				MainActivity.handler.sendMessage(msg);
//			}
//		}.sendEmptyMessageDelayed(0, 5000);
		
	}

}
