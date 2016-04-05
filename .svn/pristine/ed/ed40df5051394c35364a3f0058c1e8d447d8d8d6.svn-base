package com.lamp.gxpk.adapter;

import java.util.List;

import com.lamp.gxpk.R;
import com.lamp.gxpk.base.MyBaseAdapter;
import com.lamp.gxpk.bean.CommentBean;
import com.lamp.gxpk.myview.MyImageView;
import com.lamp.gxpk.utils.ImageLoaderUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.vov.vitamio.utils.Log;

public class CommentlvAdapter extends MyBaseAdapter {
	
	private Context context;
	private List<CommentBean> datas;
	private LayoutInflater inflater;
	
	public CommentlvAdapter(Context context, List<CommentBean> datas) {
		super();
		this.context = context;
		this.datas = datas;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.item_comment, null);
			vh = new ViewHolder();
			vh.tvnickname = (TextView) v.findViewById(R.id.item_comment_nickname);
			vh.tvtime = (TextView) v.findViewById(R.id.item_comment_time);
			vh.tvcontent = (TextView) v.findViewById(R.id.item_comment_content);
			vh.usericon = (MyImageView) v.findViewById(R.id.item_comment_icon);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		
		//显示评论的内容
		vh.tvcontent.setText(datas.get(position).getContent());
		//显示评论者昵称
		vh.tvnickname.setText(datas.get(position).getNickname());
		//显示评论时间
		vh.tvtime.setText(datas.get(position).getCreatedAt());
		Log.e("gxpk", datas.get(position).getIconUrl());
		//显示评论者的头像
		ImageLoaderUtils.loadSmallImg(vh.usericon, datas.get(position).getIconUrl());
		
		return v;
	}
	
	class ViewHolder{
		TextView tvnickname,tvcontent,tvtime;
		MyImageView usericon;
	}
}
