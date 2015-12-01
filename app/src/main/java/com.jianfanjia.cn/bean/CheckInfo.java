package com.jianfanjia.cn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @class CheckInfo.class
 * @author zhanghao
 * @Decription 此类是验收信息实体类
 * @date 2015-8-31 上午11:55
 */
public class CheckInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private long date;

	private ArrayList<Imageid> images;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public ArrayList<Imageid> getImages() {
		return images;
	}

	public void setImages(ArrayList<Imageid> images) {
		this.images = images;
	}

	public void addImageId(Imageid imageid) {
		if (images != null) {
			images.add(imageid);
		}
	}

	public boolean deleteImageIdBykey(String key) {
		boolean flag = false;
		if (images != null) {
			for (Imageid imageid : images) {
				if (key.equals(imageid.getKey())) {
					images.remove(imageid);
					return true;
				}
			}
		}
		return flag;
	}

	public static class Imageid implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String key;
		private String imageid;

		public Imageid(String key, String imageid) {
			this.key = key;
			this.imageid = imageid;
		}

		public Imageid() {
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getImageid() {
			return imageid;
		}

		public void setImageid(String imageid) {
			this.imageid = imageid;
		}

	}

}
