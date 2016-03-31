package com.jianfanjia.cn.designer.business;

import java.util.List;

import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.ProcessSectionYs;
import com.jianfanjia.api.model.ProcessSectionYsImage;
import com.jianfanjia.cn.designer.tools.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.business
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-31 11:29
 */
public class ProcessBusiness {

    public static ProcessSection getSectionInfoByName(com.jianfanjia.api.model.Process process, String sectionName) {
        LogTool.d("SectionInfo", sectionName);
        List<ProcessSection> sections = process.getSections();
        if (sections != null) {
            for (ProcessSection sectionInfo : sections) {
                if (sectionInfo.getName().equals(sectionName)) {
                    return sectionInfo;
                }
            }
        }
        return null;
    }

    public static void addImageToItem(Process process, String section, String item, String imageId) {
        ProcessSection processSection = getSectionInfoByName(process, section);
        for (ProcessSectionItem sectionItemInfo : processSection.getItems()) {
            if (sectionItemInfo.getName().equals(item)) {
                LogTool.d("SectionItem", item);
                addImageToItem(sectionItemInfo, imageId);
            }
        }
    }

    private static void addImageToItem(ProcessSectionItem processSectionItem, String imageId) {
        List<String> images = processSectionItem.getImages();
        if (images != null) {
            LogTool.d("addImage", imageId);
            images.add(imageId);
        }
    }

    public static void addImageToCheck(Process process, String section, String key, String imageId) {
        ProcessSection processSection = getSectionInfoByName(process, section);
        if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
            LogTool.d("SectionInfo", section + "--" + key + imageId);
            addImageToCheck(processSection.getYs(), key, imageId);
        }
    }

    private static void addImageToCheck(ProcessSectionYs processSectionYs, String key, String imageId) {
        ProcessSectionYsImage processSectionYsImage = new ProcessSectionYsImage();
        processSectionYsImage.setKey(key);
        processSectionYsImage.setImageid(imageId);
        List<ProcessSectionYsImage> processSectionYsImages = processSectionYs.getImages();
        if (processSectionYsImages != null) {
            processSectionYsImages.add(processSectionYsImage);
        }
    }

    public static boolean deleteCheckImage(Process process, String section, String key) {
        boolean flag = false;
        ProcessSection processSection = getSectionInfoByName(process, section);
        if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
            flag = deleteImageToCheck(processSection.getYs(), key);
        }
        return flag;
    }

    private static boolean deleteImageToCheck(ProcessSectionYs processSectionYs, String key) {
        List<ProcessSectionYsImage> processSectionYsImages = processSectionYs.getImages();
        if (processSectionYsImages != null) {
            for (ProcessSectionYsImage processSectionYsImage : processSectionYsImages) {
                if (processSectionYsImage.getKey().equals(key)) {
                    processSectionYsImages.remove(processSectionYsImage);
                    return true;
                }
            }
        }
        return false;
    }

    public static List<ProcessSectionYsImage> getImageidsByName(Process process, String section) {
        ProcessSection processSection = getSectionInfoByName(process, section);
        if (!section.equals("kai_gong") && !section.equals("chai_gai")) {
            return processSection.getYs().getImages();
        }
        return null;
    }
}
