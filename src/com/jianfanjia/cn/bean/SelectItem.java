package com.jianfanjia.cn.bean;

import java.io.Serializable;
import android.support.v4.app.Fragment;

public class SelectItem implements Serializable {
	private static final long serialVersionUID = 2658084937694399733L;
	private Fragment fragment;
	private String desc;

	public SelectItem(Fragment fragment, String desc) {
		this.fragment = fragment;
		this.desc = desc;
	}

	/**
	 * @return the fragment
	 */
	public Fragment getFragment() {
		return fragment;
	}

	/**
	 * @param fragment
	 *            the fragment to set
	 */
	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
