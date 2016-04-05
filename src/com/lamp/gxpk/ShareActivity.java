package com.lamp.gxpk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.UserBean;
import com.lamp.gxpk.bean.VideoBean;
import com.lamp.gxpk.utils.Util;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ShareActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.act_share_back)
	private ImageView back;// ����
	@ViewInject(R.id.act_share_share)
	private ImageView share;// ����
	@ViewInject(R.id.act_share_video_img)
	private ImageView videoImg;// ��Ƶ����ͼƬ
	@ViewInject(R.id.act_share_info)
	private EditText videoInfo;// ��Ƶ����
	@ViewInject(R.id.act_share_lin_pyq)
	private LinearLayout pyq;// ����������Ȧ
	@ViewInject(R.id.act_share_lin_qq)
	private LinearLayout qq;// ������QQ
	@ViewInject(R.id.act_share_lin_wx)
	private LinearLayout wx;// ������΢��
	@ViewInject(R.id.act_share_lin_sina)
	private LinearLayout sina;// ����������΢��
	private String path;// ��Ƶ�ļ���·��
	private Bitmap bit;// ��Ƶ����ͼƬ
	private UserBean ub;// ��ǰ��¼����
	private BmobFile bmobFile;// ��Ƶ�ļ�
	private File file1;// ��Ƶ�ļ�
	private ProgressDialog uploadloadDialog;
	private BmobFile bmobFile1;// ��Ƶ��Ƭ�ļ�

	private static final String APPID = "wx1ef613099f3668be";
	private IWXAPI api;
	private VideoBean vBean;
	// private WXTextObject wxText;

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
		setContentView(R.layout.act_share);
		ViewUtils.inject(getAct());
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		api = WXAPIFactory.createWXAPI(getAct(), APPID, true);
		api.registerApp(APPID);

	}

	@Override
	public void initDatas() {
		Intent intent = getIntent();
		path = intent.getStringExtra("video");
		logE(path + "------");
		bit = ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);// ��ƵͼƬ
		videoImg.setImageBitmap(bit);

	}

	@Override
	public void initOper() {
		upLoadImg();// �ϴ���ƵͼƬ

	}

	/**
	 * �ϴ���ƵͼƬ
	 */
	private void upLoadImg() {
		File file = new File(getCacheDir(), System.currentTimeMillis() + ".png");
		try {
			bit.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bmobFile1 = new BmobFile(file);
		bmobFile1.uploadblock(getAct(), new UploadFileListener() {

			@Override
			public void onSuccess() {
				toastShort("ͼƬ�ϴ��ɹ�");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastShort(arg0 + "ͼƬ�ϴ�ʧ��" + arg1);
				logE("��Ƭ�ϴ�ʧ��" + arg1);
			}
		});

	}

	/**
	 * �ϴ���Ƶ�ļ�
	 */
	private void upLoadVideo() {
		file1 = new File(path);
		bmobFile = new BmobFile(file1);

		bmobFile.uploadblock(getAct(), new UploadFileListener() {

			@Override
			public void onSuccess() {
				// toastShort("��Ƶ�ϴ��ɹ�~");
				vBean = new VideoBean();
				vBean.setUserId(ub.getObjectId());// �û�id
				vBean.setVideo(bmobFile);// ��Ƶ�ļ�
				vBean.setVideoicon(bmobFile1);// ��Ƶ��Ƭ
				vBean.setUiconUrl(ub.getUsericon().getFileUrl(getAct()));// �û�ͷ��
				logE("�û�ͷ��" + ub.getUsericon().getFileUrl(getAct()));
				vBean.setNickname(ub.getNickname());// �û��ǳ�
				String string = videoInfo.getText().toString().trim();
				vBean.setSpeak(string);// ��Ƶ����
				vBean.save(getAct(), new SaveListener() {

					@Override
					public void onSuccess() {
						toastShort("�ϴ��ɹ�");
						uploadloadDialog.dismiss();
						// finish();
						pyq.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								WXVideoObject video = new WXVideoObject();
								video.videoUrl = bmobFile.getFileUrl(getAct());
								WXMediaMessage msg = new WXMediaMessage(video);
								msg.title = "��ЦƬ��";
								msg.description = videoInfo.getText().toString().trim();
								Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.fenxinag);
								msg.thumbData = Util.bmpToByteArray(thumb, true);
								SendMessageToWX.Req req = new SendMessageToWX.Req();
								req.transaction = getType("video");
								req.message = msg;
								req.scene = SendMessageToWX.Req.WXSceneTimeline;
								api.sendReq(req);
							}
						});
						wx.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								WXVideoObject video = new WXVideoObject();
								video.videoUrl = bmobFile.getFileUrl(getAct());
								WXMediaMessage msg = new WXMediaMessage(video);
								msg.title = "��ЦƬ��";
								msg.description = videoInfo.getText().toString().trim();
								Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.fenxinag);
								msg.thumbData = Util.bmpToByteArray(thumb, true);
								SendMessageToWX.Req req = new SendMessageToWX.Req();
								req.transaction = getType("video");
								req.message = msg;
								req.scene = SendMessageToWX.Req.WXSceneSession;
								api.sendReq(req);
							}
						});
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						logE(arg0 + "����ԭ��" + arg1);
					}
				});
			}

			@Override
			public void onProgress(Integer value) {
				// TODO Auto-generated method stub
				super.onProgress(value);
				uploadloadDialog.setProgress(value);
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}

	@OnClick(R.id.act_share_back)
	public void onClick1(View v) {// ����
		finish();
	}

	@OnClick(R.id.act_share_share)
	public void onClick2(View v) {// ����Ƶ������������
		uploadloadDialog = new ProgressDialog(getAct());
		// downloadDialog.setIcon(R.drawable.ic_launcher);
		uploadloadDialog.setTitle("�ϴ�����ȴ�...");
		uploadloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		uploadloadDialog.setCanceledOnTouchOutside(false);
		// uploadloadDialog.setProgressNumberFormat("0/100");
		uploadloadDialog.show();
		upLoadVideo();// �ϴ���Ƶ�ļ�
	}

	// @OnClick(R.id.act_share_lin_pyq)
	// public void onClick3(View v) {// ������΢������Ȧ
	//
	// WXVideoObject video = new WXVideoObject();
	// video.videoUrl = bmobFile.getFileUrl(getAct());
	// logE("1" + bmobFile.getFileUrl(getAct()));
	// WXMediaMessage msg = new WXMediaMessage(video);
	// msg.title = "��ЦƬ��";
	// msg.description = videoInfo.getText().toString().trim();
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.fenxinag);
	// msg.thumbData = Util.bmpToByteArray(thumb, true);
	// logE("2");
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = getType("video");
	// logE("3");
	// req.message = msg;
	// req.scene = SendMessageToWX.Req.WXSceneTimeline;
	// logE("4");
	// api.sendReq(req);
	// logE("5");
	// }

	// @OnClick(R.id.act_share_lin_wx)
	// public void onClick4(View v) {
	// WXVideoObject video = new WXVideoObject();
	// video.videoUrl = bmobFile.getUrl();
	// WXMediaMessage msg = new WXMediaMessage(video);
	// msg.title = "��ЦƬ��";
	// msg.description = videoInfo.getText().toString().trim();
	// Bitmap thumb = BitmapFactory.decodeResource(getResources(),
	// R.drawable.fenxinag);
	// msg.thumbData = Util.bmpToByteArray(thumb, true);
	// SendMessageToWX.Req req = new SendMessageToWX.Req();
	// req.transaction = getType("video");
	// req.message = msg;
	// req.scene = SendMessageToWX.Req.WXSceneSession;
	//
	// api.sendReq(req);
	// wxText = new WXTextObject();
	// wxText.text = "��������";
	// WXMediaMessage message = new WXMediaMessage();
	// message.mediaObject = wxText;
	// message.description = "��������";
	// SendMessageToWX.Req req = new Req();
	// req.transaction = getType("text");
	// req.message = message;
	// req.scene = SendMessageToWX.Req.WXSceneSession;
	// api.sendReq(req);
	// }

	public String getType(String type) {
		return System.currentTimeMillis() + type;
	}
}