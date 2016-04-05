package com.lamp.gxpk.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lamp.gxpk.R;
import com.lamp.gxpk.base.MyBaseAdapter;

/**
 * 本地视频展示的Adapters
 * 
 * @author Administrator
 * 
 */
public class LocalVideoGvAdapter extends MyBaseAdapter {

	/**
	 * 要展示的数据源
	 */
	private List<Bitmap> datas;

	/**
	 * 上下文
	 */
	private Context context;

	public LocalVideoGvAdapter(List<Bitmap> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

	// 获得数据的数量
	@Override
	public int getCount() {
		return datas.size();
	}

	// 获得position下标的item
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	// 获得Item下标的View
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder vh;
		if (v == null) {
			vh = new ViewHolder();
			v = LayoutInflater.from(context).inflate(R.layout.item_localvideo,
					null);
			vh.img = (ImageView) v.findViewById(R.id.item_localvideo_img);
			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}
		vh.img.setImageBitmap(datas.get(position));
		return v;
	}

	class ViewHolder {
		ImageView img;
	}
}
