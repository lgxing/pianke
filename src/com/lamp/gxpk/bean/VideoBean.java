package com.lamp.gxpk.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class VideoBean extends BmobObject {
	
	private String userId;//������Ƶ�û��ĵ�ID;
	private String videotime;//��Ƶ��ʱ��
	private BmobFile videoicon;//��Ƶ��ҳչʾ��ͼƬ�ļ�
	private BmobFile video;//��Ƶ�ļ�
	private String uiconUrl;//�ֲ���Ƶ�û���ͷ��Url
	private String speak;//�û�������Ƶ�Ƿ���������
	private String nickname;//������Ƶ�û����ǳ�
	private List<String> loveduserIds;//�ղظ���Ƶ���û�Id�ļ���

	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSpeak() {
		return speak;
	}

	public void setSpeak(String speak) {
		this.speak = speak;
	}
	
	
	public String getUiconUrl() {
		return uiconUrl;
	}

	public void setUiconUrl(String uiconUrl) {
		this.uiconUrl = uiconUrl;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVideotime() {
		return videotime;
	}

	public void setVideotime(String videotime) {
		this.videotime = videotime;
	}

	public BmobFile getVideoicon() {
		return videoicon;
	}

	public void setVideoicon(BmobFile videoicon) {
		this.videoicon = videoicon;
	}

	public BmobFile getVideo() {
		return video;
	}

	public void setVideo(BmobFile video) {
		this.video = video;
	}

	public List<String> getLoveduserIds() {
		if (loveduserIds == null) {
			loveduserIds = new ArrayList<String>();
		}
		return loveduserIds;
	}

	public void setLoveduserIds(List<String> loveduserIds) {
		this.loveduserIds = loveduserIds;
	}
	
	
	
}