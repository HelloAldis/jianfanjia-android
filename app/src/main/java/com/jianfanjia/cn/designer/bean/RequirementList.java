package com.jianfanjia.cn.designer.bean;

import com.jianfanjia.cn.designer.config.Global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.designer.bean
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-20 10:05
 */
public class RequirementList implements Serializable {

    private static final long serialVersionUID = 9000498747003756579L;
    private List<RequirementInfo> requirementInfoList;

    private List<RequirementInfo> unHandleRequirementInfoList = new ArrayList<>();

    private List<RequirementInfo> communicationRequirementInfoList = new ArrayList<>();

    private List<RequirementInfo> overRequirementInfoLists = new ArrayList<>();

    public List<RequirementInfo> getRequirementInfoList() {
        return requirementInfoList;
    }

    public void setRequirementInfoList(List<RequirementInfo> requirementInfoList) {
        this.requirementInfoList = requirementInfoList;
        sortRequirementInfoList();
    }

    public RequirementList(List<RequirementInfo> requirementInfoList){
        setRequirementInfoList(requirementInfoList);
    }

    /**
     * 对需求进行分类，按照首页显示的tab类型进行份
     */
    public void sortRequirementInfoList() {
        if (requirementInfoList != null) {
            unHandleRequirementInfoList.clear();
            communicationRequirementInfoList.clear();
            overRequirementInfoLists.clear();
            for (RequirementInfo requirementInfo : requirementInfoList) {
                PlanInfo planInfo = requirementInfo.getPlan();
                if (planInfo != null) {
                        String status = planInfo.getStatus();
                        switch (status){
                            case Global.PLAN_STATUS0:
                                unHandleRequirementInfoList.add(requirementInfo);
                                break;
                            case Global.PLAN_STATUS1:
                            case Global.PLAN_STATUS4:
                            case Global.PLAN_STATUS7:
                            case Global.PLAN_STATUS8:
                                overRequirementInfoLists.add(requirementInfo);
                                break;
                            default:
                                communicationRequirementInfoList.add(requirementInfo);
                                break;
                        }
                }
            }
        }
    }

    public List<RequirementInfo> getUnHandleRequirementInfoList() {
        return unHandleRequirementInfoList;
    }

    public List<RequirementInfo> getCommunicationRequirementInfoList() {
        return communicationRequirementInfoList;
    }

    public List<RequirementInfo> getOverRequirementInfoLists() {
        return overRequirementInfoLists;
    }

}
