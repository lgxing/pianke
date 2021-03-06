package com.lamp.gxpk;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.lamp.gxpk.adapter.LocalVideoGvAdapter;
import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 查看本地作品的Activity
 * 
 * @author Administrator
 * 
 */
public class LocalVideoActivity extends BaseActivity implements BaseInterface {

	// 显示本地作品缩略图的GridView
	@ViewInject(R.id.act_localvideo_grid)
	private GridView gv;
	// 选择TextView
	@ViewInject(R.id.act_localvideo_select)
	private TextView select;
	// 取消的TextView
	@ViewInject(R.id.act_localvideo_cancel)
	private TextView cancel;
	//Activity显示的标题
	@ViewInject(R.id.act_localvideo_title)
	private TextView title;
	//删除按钮
	@ViewInject(R.id.act_localvideo_delete)
	private Button delete;
	// 显示的数据源
	private List<Bitmap> imgs;
	// 显示的视频文件
	private File[] files;
	// 显示视频文件缩略图的Adapter
	private LocalVideoGvAdapter adapter;
	//判断当前状态是否为选择状态
	private boolean state = false;
	
	private List<Integer> positions;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		initDatas();
		initOper();
	}

	// 初始化View
	@Override
	public void initViews() {
		setContentView(R.layout.act_localvideo);
		ViewUtils.inject(getAct());
	}

	// 初始化数据
	@Override
	public void initDatas() {
		positions = new ArrayList<Integer>();
		
		int code = getIntent().getIntExtra("code", -1);
		File file = null;
		if (code == 1) {
			// 本地作品
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gxpk/local");
			title.setText("本地作品");
		} else if (code == 2) {
			// 我的下载
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gxpk/download");
			title.setText("我的下载");
		}
		imgs = new ArrayList<Bitmap>();
		files = file.listFiles();
		if (files == null) {
			toastShort("没有视频哦~");
			return;
		}
		//添加默认图片显示出来
		for (int i = 0; i < files.length; i++) {
			Bitmap bit = BitmapFactory.decodeResource(getResources(),
					R.drawable.imgloading);
			imgs.add(bit);
		}
		adapter = new LocalVideoGvAdapter(imgs, getAct());
		gv.setAdapter(adapter);
		new Thread() {
			public void run() {
				//在子线程中获取视频的截图慢慢显示出来
				for (int i = 0; i < files.length; i++) {
					Bitmap bit = ThumbnailUtils.createVideoThumbnail(
							files[i].getAbsolutePath(), Thumbnails.MINI_KIND);
					imgs.set(i, bit);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//在主线程上更新UI
							adapter.notifyDataSetChanged();
						}
					});
				}
			};
		}.start();
	}

	// 初始化操作
	@Override
	public void initOper() {
		//点击视频进入播放
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(!state){
					//没有选择的状态跳转到系统播放器
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.parse(files[(int) arg3].getAbsolutePath()),
							"video/mp4");
					startActivity(intent);
				}else{
					//如果已经选中则移除
					if(positions.contains((Integer)arg2)){
						View v = gv.getChildAt(arg2);
						//将小对号显示出来
						v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.INVISIBLE);
						positions.remove((Integer)arg2);
					}else{
						//如果没有选中则添加
						View v = gv.getChildAt(arg2);
						//将小对号取消显示
						v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.VISIBLE);
						positions.add(arg2);
					}
				}
			}
		});
		// 选择按钮的监听事件
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
				state = true;
			}
		});
		// 取消按钮的监听事件
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.INVISIBLE);
				delete.setVisibility(View.INVISIBLE);
				removeLogo();
				positions.clear();
				state = false;
			}
		});
	}

	// 返回键的监听
	@OnClick(R.id.act_localvideo_back)
	public void onBackClick(View v) {
		finish();
	}

	//删除按钮的事件 
	@OnClick(R.id.act_localvideo_delete)
	public void onDeleteClick(View v) {
		List<Bitmap> del = new ArrayList<Bitmap>();
		for(int i=0;i<positions.size();i++){
			del.add(imgs.get(positions.get(i)));
			//磁盘上删除文件
			files[positions.get(i)].delete();
		}
		//UI上删除图片
		imgs.removeAll(del);
		//更新视图
		adapter.notifyDataSetChanged();
		//将记录的删除下标移除
		positions.clear();
		//移除小图标
		removeLogo();
		state = false;
		delete.setVisibility(View.INVISIBLE);
		select.setVisibility(View.VISIBLE);
		cancel.setVisibility(View.INVISIBLE);
		
	}
	
	/**
	 * 取消所有的小对号
	 */
	private void removeLogo(){
		for(int i=0;i<imgs.size();i++){
			View v = gv.getChildAt(i);
			v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.INVISIBLE);
		}
	}
}
