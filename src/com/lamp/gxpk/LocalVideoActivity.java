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
 * �鿴������Ʒ��Activity
 * 
 * @author Administrator
 * 
 */
public class LocalVideoActivity extends BaseActivity implements BaseInterface {

	// ��ʾ������Ʒ����ͼ��GridView
	@ViewInject(R.id.act_localvideo_grid)
	private GridView gv;
	// ѡ��TextView
	@ViewInject(R.id.act_localvideo_select)
	private TextView select;
	// ȡ����TextView
	@ViewInject(R.id.act_localvideo_cancel)
	private TextView cancel;
	//Activity��ʾ�ı���
	@ViewInject(R.id.act_localvideo_title)
	private TextView title;
	//ɾ����ť
	@ViewInject(R.id.act_localvideo_delete)
	private Button delete;
	// ��ʾ������Դ
	private List<Bitmap> imgs;
	// ��ʾ����Ƶ�ļ�
	private File[] files;
	// ��ʾ��Ƶ�ļ�����ͼ��Adapter
	private LocalVideoGvAdapter adapter;
	//�жϵ�ǰ״̬�Ƿ�Ϊѡ��״̬
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

	// ��ʼ��View
	@Override
	public void initViews() {
		setContentView(R.layout.act_localvideo);
		ViewUtils.inject(getAct());
	}

	// ��ʼ������
	@Override
	public void initDatas() {
		positions = new ArrayList<Integer>();
		
		int code = getIntent().getIntExtra("code", -1);
		File file = null;
		if (code == 1) {
			// ������Ʒ
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gxpk/local");
			title.setText("������Ʒ");
		} else if (code == 2) {
			// �ҵ�����
			file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/gxpk/download");
			title.setText("�ҵ�����");
		}
		imgs = new ArrayList<Bitmap>();
		files = file.listFiles();
		if (files == null) {
			toastShort("û����ƵŶ~");
			return;
		}
		//����Ĭ��ͼƬ��ʾ����
		for (int i = 0; i < files.length; i++) {
			Bitmap bit = BitmapFactory.decodeResource(getResources(),
					R.drawable.imgloading);
			imgs.add(bit);
		}
		adapter = new LocalVideoGvAdapter(imgs, getAct());
		gv.setAdapter(adapter);
		new Thread() {
			public void run() {
				//�����߳��л�ȡ��Ƶ�Ľ�ͼ������ʾ����
				for (int i = 0; i < files.length; i++) {
					Bitmap bit = ThumbnailUtils.createVideoThumbnail(
							files[i].getAbsolutePath(), Thumbnails.MINI_KIND);
					imgs.set(i, bit);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//�����߳��ϸ���UI
							adapter.notifyDataSetChanged();
						}
					});
				}
			};
		}.start();
	}

	// ��ʼ������
	@Override
	public void initOper() {
		//�����Ƶ���벥��
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(!state){
					//û��ѡ���״̬��ת��ϵͳ������
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.parse(files[(int) arg3].getAbsolutePath()),
							"video/mp4");
					startActivity(intent);
				}else{
					//����Ѿ�ѡ�����Ƴ�
					if(positions.contains((Integer)arg2)){
						View v = gv.getChildAt(arg2);
						//��С�Ժ���ʾ����
						v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.INVISIBLE);
						positions.remove((Integer)arg2);
					}else{
						//���û��ѡ��������
						View v = gv.getChildAt(arg2);
						//��С�Ժ�ȡ����ʾ
						v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.VISIBLE);
						positions.add(arg2);
					}
				}
			}
		});
		// ѡ��ť�ļ����¼�
		select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				select.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.VISIBLE);
				delete.setVisibility(View.VISIBLE);
				state = true;
			}
		});
		// ȡ����ť�ļ����¼�
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

	// ���ؼ��ļ���
	@OnClick(R.id.act_localvideo_back)
	public void onBackClick(View v) {
		finish();
	}

	//ɾ����ť���¼� 
	@OnClick(R.id.act_localvideo_delete)
	public void onDeleteClick(View v) {
		List<Bitmap> del = new ArrayList<Bitmap>();
		for(int i=0;i<positions.size();i++){
			del.add(imgs.get(positions.get(i)));
			//������ɾ���ļ�
			files[positions.get(i)].delete();
		}
		//UI��ɾ��ͼƬ
		imgs.removeAll(del);
		//������ͼ
		adapter.notifyDataSetChanged();
		//����¼��ɾ���±��Ƴ�
		positions.clear();
		//�Ƴ�Сͼ��
		removeLogo();
		state = false;
		delete.setVisibility(View.INVISIBLE);
		select.setVisibility(View.VISIBLE);
		cancel.setVisibility(View.INVISIBLE);
		
	}
	
	/**
	 * ȡ�����е�С�Ժ�
	 */
	private void removeLogo(){
		for(int i=0;i<imgs.size();i++){
			View v = gv.getChildAt(i);
			v.findViewById(R.id.item_localvideo_xuanzhong).setVisibility(View.INVISIBLE);
		}
	}
}