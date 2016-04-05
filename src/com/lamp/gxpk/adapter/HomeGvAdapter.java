package com.lamp.gxpk.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lamp.gxpk.R;
import com.lamp.gxpk.base.MyBaseAdapter;
import com.lamp.gxpk.bean.VideoBean;
import com.lamp.gxpk.utils.ImageLoaderUtils;

/**
 * 主页面显示视频的Adapter
 * @author Administrator
 *
 */
public class HomeGvAdapter extends MyBaseAdapter{

	private List<VideoBean> datas;
	private Context context;
	
	/**
	 * 
	 * @param datas 加载的数据源
	 * @param context 上下文
	 */
	public HomeGvAdapter(List<VideoBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

	//返回总数量
	@Override
	public int getCount() {
		return datas.size();
	}

	//返回arg0下标的数据
	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	//返回arg0下标的View
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh;
		if(v==null){
			vh = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.item_home_gv, null);
			vh.icon = (ImageView) v.findViewById(R.id.item_home_gv_icon);
			vh.videoImg = (ImageView) v.findViewById(R.id.item_home_gv_videoimg);
			vh.num = (TextView) v.findViewById(R.id.item_home_gv_num);
			v.setTag(vh);
		}else{
			vh = (ViewHolder) v.getTag();
		}
		//加载用户头像
		ImageLoaderUtils.loadSmallImg(vh.icon, datas.get(position).getUiconUrl());
		//加载视频截图
		ImageLoaderUtils.loadBigImg(vh.videoImg, datas.get(position).getVideoicon().getFileUrl(context));
		//点赞的数量
		vh.num.setText(datas.get(position).getLoveduserIds().size()+"");
		return v;
	}
	
	/**
	 * 下拉刷新的操作
	 * @param data 替换的数据源
	 */
	public void setData(List<VideoBean> data){
		this.datas = data;
		notifyDataSetChanged();
	}
	
	/**
	 * 上拉加载的操作
	 * @param data 加载的数据源
	 */
	public void addData(List<VideoBean> data){
		this.datas.addAll(data);
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		ImageView icon,videoImg;
		TextView num;
	}
}


