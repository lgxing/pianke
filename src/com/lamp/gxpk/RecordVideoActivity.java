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
 * 录制视频页面
 * 
 * @author Administrator
 *
 */
public class RecordVideoActivity extends BaseActivity implements BaseInterface, OnClickListener {
	@ViewInject(R.id.act_rview_recode)
	private ImageButton record;// 开始录制
	@ViewInject(R.id.act_rView)
	private SurfaceView sView;// 显示视频预览的SurfaceView
	// 系统的视频文件
	File videoFile;
	MediaRecorder mRecorder;
	// 记录是否正在进行录制
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
		// 设置竖屏显示
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 选择支持半透明模式,在有surfaceview的activity中使用。
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// 为两个按钮的单击事件绑定监听器
		record.setOnClickListener(this);
		// 设置分辨率
		sView.getHolder().setFixedSize(1280, 720);
		// 设置该组件让屏幕不会自动关闭
		sView.getHolder().setKeepScreenOn(true);
		// 创建MediaPlayer对象
		mRecorder = new MediaRecorder();

	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initOper() {

	}

	// 开始录制，停止录制的监听事件
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (isRecording) {
			record.setImageResource(R.drawable.luxiang2);
			isRecording = false;

			if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				Toast.makeText(this, "SD卡不存在，请插入SD卡！", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				File file = new File(Environment.getExternalStorageDirectory() + "/gxpk/local");
				if (!file.exists()) {
					file.mkdir();
				}
				// 创建保存录制视频的视频文件
				videoFile = new File(file + "/video_" + System.currentTimeMillis() + ".mp4");

				// 设置录制视频源为Camera(相机)
				camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
				if (camera != null) {
					camera.setDisplayOrientation(90);// 摄像图旋转90度
					camera.unlock();
					mRecorder.setCamera(camera);
				}
				mRecorder.reset();
				// 设置从麦克风采集声音(或来自录像机的声音AudioSource.CAMCORDER)
				mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置从摄像头采集图像
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				// 设置视频文件的输出格式
				// 必须在设置声音编码格式、图像编码格式之前设置
				mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
				// 设置声音编码的格式
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				// 设置图像编码的格式
				mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
				mRecorder.setVideoSize(1280, 720);
				mRecorder.setVideoEncodingBitRate(1 * 1024 * 512);
//				// 设置视频最大时长
//				mRecorder.setMaxDuration(2 * 60 * 1000);
//
//				// 设置文件最大值(10M)
//				mRecorder.setMaxFileSize(10 * 1024 * 1024);
				// 视频旋转90
				mRecorder.setOrientationHint(90);
				// 每秒 4帧
				mRecorder.setVideoFrameRate(30);
				mRecorder.setOutputFile(videoFile.getAbsolutePath());
				// 指定使用SurfaceView来预览视频
				mRecorder.setPreviewDisplay(sView.getHolder().getSurface()); // ①
				mRecorder.prepare();
				// 开始录制
				mRecorder.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			isRecording = true;
			record.setImageResource(R.drawable.luxiang);
			// 如果正在进行录制
			if (isRecording) {
				// 停止录制
				mRecorder.stop();
				//释放资源
				camera.release();
				// 释放资源
				mRecorder.release();
				mRecorder = null;
				// 此处有跳转(跳转至分享页面)
				Intent intent = new Intent(getAct(), ShareActivity.class);
				String path = videoFile.getAbsolutePath();
				intent.putExtra("video", path);
				startActivity(intent);
				finish();
			}
		}
	}

}
