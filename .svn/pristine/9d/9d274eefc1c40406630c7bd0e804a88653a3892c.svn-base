package com.lamp.gxpk.utils;

import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.lamp.gxpk.R;
import com.lamp.gxpk.application.MyApplication;

/**
 * 加载图片的工具类
 * @author Administrator
 *
 */
public class ImageLoaderUtils {
	
	private static ImageLoader loader = new ImageLoader(MyApplication.queue, new MyCache());
	
	private static ImageListener listener;
	/**
	 * 加载小图片
	 * @param img 要显示的控件
	 * @param url 图片url
	 */
	public static void loadSmallImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.drawer_person,R.drawable.drawer_person);
		loader.get(url, listener, 100, 100);
	}
	/**
	 * 加载大图片
	 * @param img 要显示的控件
	 * @param url 图片url
	 */
	public static void loadBigImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.imgloading,R.drawable.imgloading);
		loader.get(url, listener, 400, 300);
	}
}
