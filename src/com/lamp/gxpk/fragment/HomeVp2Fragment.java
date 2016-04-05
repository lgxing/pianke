package com.lamp.gxpk.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.lamp.gxpk.PlayVideoActivity;
import com.lamp.gxpk.R;
import com.lamp.gxpk.adapter.HomeGvAdapter;
import com.lamp.gxpk.application.MyApplication;
import com.lamp.gxpk.base.BaseFragment;
import com.lamp.gxpk.base.BaseInterface;
import com.lamp.gxpk.bean.UserBean;
import com.lamp.gxpk.bean.VideoBean;
import com.lamp.gxpk.pulltorefresh.MyListener;
import com.lamp.gxpk.pulltorefresh.PullToRefreshLayout;
import com.lamp.gxpk.pulltorefresh.pullableview.PullableGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ��ҳ����ʾ��Ƶ�б��Fragment
 * 
 * @author Administrator
 * 
 */
public class HomeVp2Fragment extends BaseFragment implements BaseInterface {
	// ��ʾ�б��GridView
	@ViewInject(R.id.frag_home_vp_gv)
	private PullableGridView gv;

	//����ˢ�µĿؼ�
	@ViewInject(R.id.frag_home_vp_layout)
	private PullToRefreshLayout layout;
	// Ҫ���ص�����Դ
	private List<VideoBean> datas;
	// GridView��Adapter
	private HomeGvAdapter adapter;
	//����ʾ���ݵĳ���
	private int count;
	//��ǰ��¼���û�
	private UserBean ub;
	
	/**
	 * ��������Դ
	 * @param datas ����Դ
	 */
	public void setDatas(List<VideoBean> datas){
		this.datas = datas;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initOper();
	}

	@Override
	public View getFragmentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.frag_home_vp, null);
		ViewUtils.inject(this, v);
		return v;
	}

	@Override
	public void initViews() {
		
	}

	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);
		if(ub==null){
			return;
		}
		List<BmobQuery<VideoBean>> queries = new ArrayList<BmobQuery<VideoBean>>();
		for(int i=0;i<ub.getGzUserids().size();i++){
			BmobQuery<VideoBean> query = new BmobQuery<VideoBean>();
			query.addWhereEqualTo("userId", ub.getGzUserids().get(i));
			queries.add(query);
		}
		BmobQuery<VideoBean> main = new BmobQuery<VideoBean>();
		main.or(queries);
		main.order("-createdAt");
		main.setLimit(6);
		main.findObjects(getActivity(), new FindListener<VideoBean>() {
			
			@Override
			public void onSuccess(List<VideoBean> arg0) {
				count = arg0.size();
				datas = arg0;
				adapter = new HomeGvAdapter(datas, getActivity());
				gv.setAdapter(adapter);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				LogE("��ѯʧ��"+arg1);
			}
		});
	}

	@Override
	public void initOper() {
		//GridView����¼�
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),PlayVideoActivity.class);
				MyApplication.putData("video", datas.get(arg2));
				startActivity(intent);
			}
		});
		layout.setOnRefreshListener(new MyListener(){

			//����ˢ�µĲ���
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				List<BmobQuery<VideoBean>> queries = new ArrayList<BmobQuery<VideoBean>>();
				for(int i=0;i<ub.getGzUserids().size();i++){
					BmobQuery<VideoBean> query = new BmobQuery<VideoBean>();
					query.addWhereEqualTo("userId", ub.getGzUserids().get(i));
					queries.add(query);
				}
				BmobQuery<VideoBean> main = new BmobQuery<VideoBean>();
				main.or(queries);//���ѯ��ѯ
				main.order("-createdAt");//���ѯ��ѯ
				main.setLimit(6);//���ٲ�ѯ6��
				main.findObjects(getActivity(), new FindListener<VideoBean>() {
					
					@Override
					public void onSuccess(List<VideoBean> arg0) {
						layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						count = arg0.size();
						datas = arg0;
						adapter.setData(arg0);
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						layout.loadmoreFinish(PullToRefreshLayout.FAIL);
						LogE("����ˢ��ʧ��"+arg1);
					}
				});
				
			}
			
			//�������صĲ���
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				List<BmobQuery<VideoBean>> queries = new ArrayList<BmobQuery<VideoBean>>();
				for(int i=0;i<ub.getGzUserids().size();i++){
					BmobQuery<VideoBean> query = new BmobQuery<VideoBean>();
					query.addWhereEqualTo("userId", ub.getGzUserids().get(i));
					queries.add(query);
				}
				BmobQuery<VideoBean> main = new BmobQuery<VideoBean>();
				main.or(queries);//���ѯ��ѯ����
				main.order("-createdAt");//��������
				main.setLimit(6);//���ټ���6��
				main.setSkip(count);//����������
				main.findObjects(getActivity(), new FindListener<VideoBean>() {
					
					@Override
					public void onSuccess(List<VideoBean> arg0) {
						layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						count += arg0.size();
						adapter.addData(arg0);
					}
					
					@Override
					public void onError(int arg0, String arg1) {
						layout.loadmoreFinish(PullToRefreshLayout.FAIL);
						LogE("��������ʧ��"+arg1);
					}
				});
			}
		});
	}

}
