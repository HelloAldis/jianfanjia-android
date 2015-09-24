package com.jianfanjia.cn.bean;

import java.util.ArrayList;

import com.jianfanjia.cn.bean.CheckInfo.Imageid;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @class ProcessInfo
 * @Decription 此类是工地信息类
 * @author zhanghao
 * @date 2015-8-28 15:30
 * 
 */
public class ProcessInfo extends RequirementInfo {
	private static final long serialVersionUID = 1L;

	private String userid;

	private String going_on;

	private ArrayList<SectionInfo> sections;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGoing_on() {
		return going_on;
	}

	public void setGoing_on(String going_on) {
		this.going_on = going_on;
	}

	public ArrayList<SectionInfo> getSections() {
		return sections;
	}

	public void setSections(ArrayList<SectionInfo> sections) {
		this.sections = sections;
	}
	
	public SectionInfo getSectionInfoByName(String sectionName){
		LogTool.d("SectionInfo", sectionName);
		if(sections != null){
			for(SectionInfo sectionInfo : sections){
				if(sectionInfo.getName().equals(sectionName)){
					return sectionInfo;
				}
			}
		}
		return null;
	}
	
	public void addImageToItem(String section,String item,String imageId){
		if(sections != null){
			for(SectionInfo sectionInfo : sections){
				if(sectionInfo.getName().equals(section)){
					LogTool.d("SectionInfo", section);
					for(SectionItemInfo sectionItemInfo : sectionInfo.getItems()){
						if(sectionItemInfo.getName().equals(item)){
							LogTool.d("SectionItem", item);
							sectionItemInfo.addImageToItem(imageId);
						}
					}
				}
			}
		}
	}
	
	public void addImageToCheck(String section,String key,String imageId){
		if(sections != null){
			for(SectionInfo sectionInfo : sections){
				if(sectionInfo.getName().equals(section)){
					if(section.equals("kai_gong") && section.equals("chai_gai")){
						LogTool.d("SectionInfo", section + "--" + key + imageId);
						sectionInfo.getYs().addImageId(new Imageid(key,imageId));
					}
				}
			}
		}
	}
	
	public ArrayList<Imageid> getImageidsByName(String section){
		if(sections != null){
			for(SectionInfo sectionInfo : sections){
				if(sectionInfo.getName().equals(section)){
					LogTool.d("SectionInfo", section);
					if(!section.equals("kai_gong") && !section.equals("chai_gai")){
						return sectionInfo.getYs().getImages();
					}
				}
			}
		}
		return null;
	}
	
	

}
