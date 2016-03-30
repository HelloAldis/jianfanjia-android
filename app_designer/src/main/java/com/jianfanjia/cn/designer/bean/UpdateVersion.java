package com.jianfanjia.cn.designer.bean;

import java.io.Serializable;

public class UpdateVersion implements Serializable {

	private static final long serialVersionUID = 8703607686714247167L;

	private String version_name;

	private String version_code;

	private String download_url;

	private String updatetype;

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public String getDownload_url() {
		return download_url;
	}

	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}

	public String getUpdatetype() {
		return updatetype;
	}

	public void setUpdatetype(String updatetype) {
		this.updatetype = updatetype;
	}
}
