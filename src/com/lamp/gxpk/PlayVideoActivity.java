package com.lamp.gxpk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lamp.gxpk.adapter.CommentlvAdapter;
import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.base.BaseActivity;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.CommentBean;
import com.lamp.gxpk.bean.UserBean;
import com.lamp.gxpk.bean.VideoBean;
import com.lamp.gxpk.myview.MyImageView;
import com.lamp.gxpk.utils.GetVideoTimeUtils;
import com.lamp.gxpk.utils.ImageLoaderUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PlayVideoActivity extends BaseActivity implements BaseInterface {

	@ViewInject(R.id.act_playvideo_video)
	private VideoView video;// 视频播放
	@ViewInject(R.id.act_playvideo_usericon)
	private MyImageView iconimg;// 视频发布者头像
	@ViewInject(R.id.act_playvideo_username)
	private TextView tvuname;// 用户昵称
	@ViewInject(R.id.act_playvideo_speak)
	private TextView tvspeak;// 用户发布视频时发表的言论
	@ViewInject(R.id.act_playvideo_lovenum)
	private TextView tvlovenum;// 收藏该视频的人数
	@ViewInject(R.id.act_playvideo_commentnum)
	private TextView tvCommentnum;// 评论该视频的人数
	@ViewInject(R.id.act_playvideo_content)
	private EditText etcontent;// 评论的内容
	@ViewInject(R.id.act_playvideo_lv)
	private ListView commentlv;// 评论列表
	@ViewInject(R.id.act_playvideo_progressbar)
	private ProgressBar progressBar;// 显示当前缓存进度
	@ViewInject(R.id.act_playvideo_time)
	private TextView tvtime;// 视频总时长
	@ViewInject(R.id.act_playvideo_love)
	private ImageView imglove;// 收藏
	@ViewInject(R.id.act_playvideo_frame)
	private FrameLayout frame;
	@ViewInject(R.id.act_playvideo_frame1)
	private FrameLayout frame1;
	
	private UserBean ub;// 当前登录用户
	private MediaController mController;
	private VideoBean vbean;//要播放的视频对象

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_playvideo);
		ViewUtils.inject(this);
		
	}

	@Override
	public void initDatas() {

		vbean = (VideoBean) MyApplication.getData("video", true);

		ub = BmobUser.getCurrentUser(this, UserBean.class);

		// 查询该视频所有评论
		getVideoComment();

	}

	@Override
	public void initOper() {
		// 显示视频发布者的昵称
		tvuname.setText(vbean.getNickname()+":");
		// 显示视频发布者发布的言论
		tvspeak.setText(vbean.getSpeak());
		// 显示视频发布者头像
		ImageLoaderUtils.loadSmallImg(iconimg, vbean.getUiconUrl());
		// 显示视频收藏人数
		tvlovenum.setText(vbean.getLoveduserIds().size() + " 喜欢");
		// 显示心型的状态（是否被改用户收藏）
		List<String> ids = vbean.getLoveduserIds();
		if (ub == null || !ids.contains(ub.getObjectId())) {
			imglove.setImageResource(R.drawable.heartoff);
		} else {
			imglove.setImageResource(R.drawable.hearton);
		}
		// 播放视频
		playVideo(vbean.getVideo().getFileUrl(this));
	}

	private void playVideo(String url) {
		video.setBackgroundColor(Color.parseColor("#e2e2e2"));
		mController = new MediaController(this);
		//加载指定视频
		video.setVideoPath(url);
		//设置video与mController建立关联
		video.setMediaController(mController);
		//设置mController与video建立关联
		mController.setMediaPlayer(video);
		//让video获得焦点
		video.requestFocus();
		//设置监听，准备好后显示视频时长
		video.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				frame.setVisibility(View.GONE);
				// 获得视频总时长
				long time = video.getDuration();
				String time_str = GetVideoTimeUtils.getvideotime(time);
				// 显示时长
				tvtime.setText(time_str);
				video.setBackgroundColor(0);
			}
		});
		//播放视频
		video.start();
	}

	// 下载视频的操作
	@OnClick(R.id.act_playvideo_load)
	public void loadOnClick(View v) {
		if (ub == null) {
			startActivity(new Intent(getAct(), LogRegActivity.class));
			return;
		}

		HttpUtils hu = new HttpUtils();
		// 下载下来的视频保存路径
		final String target = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gxpk/download/"
				+ System.currentTimeMillis() + ".mp4";
		// 下载视频
		hu.download(vbean.getVideo().getFileUrl(this), target, true, true, new RequestCallBack<File>() {
			// 开始下载
			@Override
			public void onStart() {
				super.onStart();
				progressBar.setProgress(0);
				toastShort("开始下载");
			}

			// 下载中
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				// 显示下载进度
				int i = (int) (current / (total / 100));
				progressBar.setProgress(i);
			}

			// 下载成功
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				toastShort("下载成功，请去我的下载查看");
			}

			// 下载失败
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShort("下载失败:"+arg1);
			}
		});

	}

	// 该方法用于查询当前视频所有的评论及设置评论的人数
	private void getVideoComment() {

		BmobQuery<CommentBean> query = new BmobQuery<CommentBean>();
		// 设置查询ID
		query.addWhereEqualTo("videoId", vbean.getObjectId());
		// 倒序查询
		query.order("-createdAt");
		query.findObjects(getAct(), new FindListener<CommentBean>() {

			@Override
			public void onSuccess(List<CommentBean> arg0) {
				// 查询成功以后设置评论的总人数
				if (arg0.size() == 0) {
					tvCommentnum.setText("0 评论");
				} else {
					tvCommentnum.setText(arg0.size() + " 评论");
				}

				// 设置数据源
				commentlv.setAdapter(new CommentlvAdapter(getAct(), arg0));
			}

			@Override
			public void onError(int arg0, String arg1) {
				logE("查询失败:" + arg1);
			}
		});

	}

	// 发表对该视频评论的操作
	@OnClick(R.id.act_playvideo_fabu)
	public void fabuOnClick(View v) {
		// 如果没有用户登陆，发表评论跳登陆界面
		if (ub == null) {
			startActivity(new Intent(getAct(), LogRegActivity.class));
			return;
		}

		// 判断评论内容是否为空
		String content = etcontent.getText().toString().trim();
		if (content.equals("")) {
			toastShort("请输入您评论的内容");
			return;
		}
		// 上传服务器
		CommentBean cbean = new CommentBean();
		cbean.setContent(content);
		cbean.setNickname(ub.getNickname());
		logE(ub.getUsericon().getFileUrl(this));
		cbean.setIconUrl(ub.getUsericon().getFileUrl(this));
		cbean.setVideoId(vbean.getObjectId());
		cbean.save(getAct(), new SaveListener() {

			@Override
			public void onSuccess() {
				toastShort("发表成功");
				etcontent.setText("");
				// 刷新评论
				getVideoComment();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				logE("评论失败" + arg1);
			}
		});
	}

	// 收藏
	@OnClick(R.id.act_playvideo_love)
	public void loveOnClick(View v) {

		if (ub == null) {
			toastShort("请去登陆后收藏");
			startActivity(new Intent(getAct(), LogRegActivity.class));
			return;
		}

		List<String> ids = vbean.getLoveduserIds();
		if (!ids.contains(ub.getObjectId())) {// 点赞操作
			VideoBean newvBean = new VideoBean();
			// 服务器增加
			newvBean.add("loveduserIds", ub.getObjectId());
			// 本地增加
			vbean.getLoveduserIds().add(ub.getObjectId());
			// 更新
			newvBean.update(getAct(), vbean.getObjectId(), new UpdateListener() {

				@Override
				public void onSuccess() {

				}

				@Override
				public void onFailure(int arg0, String arg1) {

				}
			});

			UserBean newub = new UserBean();
			// 服务器加一
			newub.add("lovevideoIds", vbean.getObjectId());
			// 本地增加视频ID
			ub.getLovevideoIds().add(vbean.getObjectId());
			// 更新
			newub.update(getAct(), ub.getObjectId(), new UpdateListener() {

				@Override
				public void onSuccess() {
					toastShort("收藏成功");
					imglove.setImageResource(R.drawable.hearton);
					tvlovenum.setText(vbean.getLoveduserIds().size() + " 喜欢");
				}

				@Override
				public void onFailure(int arg0, String arg1) {

				}
			});
		} else {// 取消点赞

			VideoBean newvideo = new VideoBean();
			List<String> values = new ArrayList<String>();
			// 本地移除用户ID
			vbean.getLoveduserIds().remove(ub.getObjectId());
			// 服务器移除用户ID
			values.add(ub.getObjectId());
			newvideo.removeAll("loveduserIds", values);
			// 更新
			newvideo.update(getAct(), vbean.getObjectId(), new UpdateListener() {

				@Override
				public void onSuccess() {

				}

				@Override
				public void onFailure(int arg0, String arg1) {

				}
			});
			UserBean uu = new UserBean();
			// 用户收藏活动ID的集合本地移除该活动ID
			ub.getLovevideoIds().remove(vbean.getObjectId());
			List<String> values2 = new ArrayList<String>();
			values2.add(vbean.getObjectId());
			// 用户收藏活动ID的集合服务器移除该活动ID
			uu.removeAll("lovevideoIds", values2);
			uu.update(getAct(), ub.getObjectId(), new UpdateListener() {

				@Override
				public void onSuccess() {
					toastShort("取消收藏");
					imglove.setImageResource(R.drawable.heartoff);
					tvlovenum.setText(vbean.getLoveduserIds().size() + " 喜欢");
				}

				@Override
				public void onFailure(int arg0, String arg1) {

				}
			});
		}
	}

	// 查看他人资料
	@OnClick(R.id.act_playvideo_usericon)
	public void msgOnClick(View v) {

		if (ub == null) {
			toastShort("请登陆后查看他人资料");
			return;
		}

		Intent intent = new Intent(getAct(), UserInfoActivity.class);
		logE(vbean.getUserId());
		intent.putExtra("uId", vbean.getUserId());
		startActivity(intent);
	}

	// 回显
	@Override
	protected void onRestart() {
		super.onRestart();
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	// 返回
	@OnClick(R.id.act_playvideo_back)
	public void backOnClick(View v) {
		finish();
	}
}
