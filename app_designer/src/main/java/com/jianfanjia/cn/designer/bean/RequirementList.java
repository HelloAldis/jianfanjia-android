package com.jianfanjia.cn.designer.bean;

import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
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
    private List<Requirement> requirementInfoList;

    private List<Requirement> unHandleRequirementInfoList = new ArrayList<>();

    private List<Requirement> communicationRequirementInfoList = new ArrayList<>();

    private List<Requirement> overRequirementInfoLists = new ArrayList<>();

    public List<Requirement> getRequirementInfoList() {
        return requirementInfoList;
    }

    public void setRequirementInfoList(List<Requirement> requirementInfoList) {
        this.requirementInfoList = requirementInfoList;
        sortRequirementInfoList();
    }

    public RequirementList(List<Requirement> requirementInfoList) {
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
            for (Requirement requirementInfo : requirementInfoList) {
                String requiremnetStatus = requirementInfo.getStatus();
                if (requiremnetStatus.equals(Global.REQUIREMENT_STATUS5) || requiremnetStatus.equals(Global
                        .REQUIREMENT_STATUS8)) {
                    continue;
                }
                Plan plan = requirementInfo.getPlan();
                if (plan != null) {
                    String status = plan.getStatus();
                    switch (status) {
                        case Global.PLAN_STATUS0:
                            unHandleRequirementInfoList.add(requirementInfo);
                            break;
                        case Global.PLAN_STATUS1:
                        case Global.PLAN_STATUS4:
                        case Global.PLAN_STATUS8:
                        case Global.PLAN_STATUS9:
                            overRequirementInfoLists.add(requirementInfo);
                            break;
                        case Global.PLAN_STATUS7:
                            break;
                        default:
                            communicationRequirementInfoList.add(requirementInfo);
                            break;
                    }
                }
            }
        }
    }

    public List<Requirement> getUnHandleRequirementInfoList() {
        return unHandleRequirementInfoList;
    }

    public List<Requirement> getCommunicationRequirementInfoList() {
        return communicationRequirementInfoList;
    }

    public List<Requirement> getOverRequirementInfoLists() {
        return overRequirementInfoLists;
    }

}
