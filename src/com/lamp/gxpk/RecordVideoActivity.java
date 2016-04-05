package com.lamp.gxpk;

import java.io.File;

import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.core.BitmapDecoder;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * ¼����Ƶҳ��
 * 
 * @author Administrator
 *
 */
public class RecordVideoActivity extends BaseActivity implements BaseInterface, OnClickListener {
	@ViewInject(R.id.act_rview_recode)
	private ImageButton record;// ��ʼ¼��
	@ViewInject(R.id.act_rView)
	private SurfaceView sView;// ��ʾ��ƵԤ����SurfaceView
	// ϵͳ����Ƶ�ļ�
	File videoFile;
	MediaRecorder mRecorder;
	// ��¼�Ƿ����ڽ���¼��
	private boolean isRecording = true;
	private Camera camera;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		initDatas();
		initOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_recordvideo);
		ViewUtils.inject(getAct());
		// ����������ʾ
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// ����ȫ��
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ѡ��֧�ְ�͸��ģʽ,����surfaceview��activity��ʹ�á�
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// Ϊ������ť�ĵ����¼��󶨼�����
		record.setOnClickListener(this);
		// ���÷ֱ���
		sView.getHolder().setFixedSize(1280, 720);
		// ���ø��������Ļ�����Զ��ر�
		sView.getHolder().setKeepScreenOn(true);
		// ����MediaPlayer����
		mRecorder = new MediaRecorder();

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

	}

	// ��ʼ¼�ƣ�ֹͣ¼�Ƶļ����¼�
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (isRecording) {
			record.setImageResource(R.drawable.luxiang2);
			isRecording = false;

			if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				Toast.makeText(this, "SD�������ڣ������SD����", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				File file = new File(Environment.getExternalStorageDirectory() + "/gxpk/local");
				if (!file.exists()) {
					file.mkdir();
				}
				// ��������¼����Ƶ����Ƶ�ļ�
				videoFile = new File(file + "/video_" + System.currentTimeMillis() + ".mp4");

				// ����¼����ƵԴΪCamera(���)
				camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
				if (camera != null) {
					camera.setDisplayOrientation(90);// ����ͼ��ת90��
					camera.unlock();
					mRecorder.setCamera(camera);
				}
				mRecorder.reset();
				// ���ô���˷�ɼ�����(������¼���������AudioSource.CAMCORDER)
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// ���ô�����ͷ�ɼ�ͼ��
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// ������Ƶ�ļ��������ʽ
				// �������������������ʽ��ͼ������ʽ֮ǰ����
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				// ������������ĸ�ʽ
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// ����ͼ�����ĸ�ʽ
				mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
				mRecorder.setVideoSize(1280, 720);
				mRecorder.setVideoEncodingBitRate(1 * 1024 * 512);
//				// ������Ƶ���ʱ��
//				mRecorder.setMaxDuration(2 * 60 * 1000);
//
//				// �����ļ����ֵ(10M)
//				mRecorder.setMaxFileSize(10 * 1024 * 1024);
				// ��Ƶ��ת90
				mRecorder.setOrientationHint(90);
				// ÿ�� 4֡
				mRecorder.setVideoFrameRate(30);
				mRecorder.setOutputFile(videoFile.getAbsolutePath());
				// ָ��ʹ��SurfaceView��Ԥ����Ƶ
				mRecorder.setPreviewDisplay(sView.getHolder().getSurface()); // ��
				mRecorder.prepare();
				// ��ʼ¼��
				mRecorder.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			isRecording = true;
			record.setImageResource(R.drawable.luxiang);
			// ������ڽ���¼��
			if (isRecording) {
				// ֹͣ¼��
				mRecorder.stop();
				//�ͷ���Դ
				camera.release();
				// �ͷ���Դ
				mRecorder.release();
				mRecorder = null;
				// �˴�����ת(��ת������ҳ��)
				Intent intent = new Intent(getAct(), ShareActivity.class);
				String path = videoFile.getAbsolutePath();
				intent.putExtra("video", path);
				startActivity(intent);
				finish();
			}
		}
	}

}