package com.jianfanjia.cn.base;

import com.jianfanjia.cn.tools.LogTool;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @version 1.0
 * @Description Fragment»ùÀà
 * @author Administrator
 * @date 2015-8-19 16:02:18
 * 
 */
public abstract class BaseFragment extends Fragment implements OnClickListener{

	protected LayoutInflater inflater = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogTool.d(this.getClass().getName(),"onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogTool.d(this.getClass().getName(),"onCreateView");
		this.inflater = inflater;
		View view = inflateView(getLayoutId());
		initView(view);
		return view;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogTool.d(this.getClass().getName(),"onDestroy");
	}

	@Override
    public void onResume() {
        super.onResume();
		LogTool.d(this.getClass().getName(),"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
		LogTool.d(this.getClass().getName(),"onPause");
    }
    
    public Context getApplication() {
        return getActivity().getApplication();
    }
    
	public abstract int getLayoutId();

	public abstract void initView(View view);

	protected View inflateView(int resId) {
		return this.inflater.inflate(resId, null);
	}
	
	@Override
	public void onClick(View arg0) {
	}
	
	protected void makeTextShort(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	protected void makeTextLong(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}

}
