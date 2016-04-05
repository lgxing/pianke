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
 * ������Ƶչʾ��Adapters
 * 
 * @author Administrator
 * 
 */
public class LocalVideoGvAdapter extends MyBaseAdapter {

	/**
	 * Ҫչʾ������Դ
	 */
	private List<Bitmap> datas;

	/**
	 * ������
	 */
	private Context context;

	public LocalVideoGvAdapter(List<Bitmap> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

	// ������ݵ�����
	@Override
	public int getCount() {
		return datas.size();
	}

	// ���position�±��item
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	// ���Item�±��View
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