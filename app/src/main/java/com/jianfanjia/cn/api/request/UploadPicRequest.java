package com.jianfanjia.cn.api.request;


public class UploadPicRequest extends BaseRequest {
	private byte[] bytes;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
