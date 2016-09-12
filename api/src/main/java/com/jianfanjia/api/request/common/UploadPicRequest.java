package com.jianfanjia.api.request.common;


import com.jianfanjia.api.request.BaseRequest;

public class UploadPicRequest extends BaseRequest {
	private byte[] bytes;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
