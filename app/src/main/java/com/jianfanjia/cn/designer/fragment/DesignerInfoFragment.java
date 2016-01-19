package com.jianfanjia.cn.designer.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.bean.DesignerInfo;
import com.jianfanjia.cn.designer.cache.BusinessManager;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.ScrollableHelper;

import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ApiUiUpdateListener, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerInfoFragment.class.getName();
    private ScrollView scrollView = null;
    private LinearLayout jiandanTypeLayout = null;
    private LinearLayout jiandanHouseTypeLayout = null;
    private LinearLayout jiandanDistrictLayout = null;
    private LinearLayout designStyleLayout = null;
    private LinearLayout designIdeaLayout = null;
    private LinearLayout designAchievementLayout = null;
    private LinearLayout companyLayout = null;
    private LinearLayout teamCountLayout = null;
    private LinearLayout designFeeLayout = null;
    private TextView jiandanType = null;
    private TextView jiandanHouseType = null;
    private TextView jiandanDistrict = null;
    private TextView designStyle = null;
    private TextView designIdea = null;
    private TextView designAchievement = null;
    private TextView company = null;
    private TextView teamCount = null;
    private TextView designFee = null;
    private String designerid = null;

    public static DesignerInfoFragment newInstance(String info) {
        Bundle args = new Bundle();
        DesignerInfoFragment infoFragment = new DesignerInfoFragment();
        args.putString(Global.DESIGNER_ID, info);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void initView(View view) {
        Bundle bundle = getArguments();
        designerid = bundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid=" + designerid);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        jiandanTypeLayout = (LinearLayout) view.findViewById(R.id.jiandanTypeLayout);
        jiandanHouseTypeLayout = (LinearLayout) view.findViewById(R.id.jiandanHouseTypeLayout);
        jiandanDistrictLayout = (LinearLayout) view.findViewById(R.id.jiandanDistrictLayout);
        designStyleLayout = (LinearLayout) view.findViewById(R.id.designStyleLayout);
        designIdeaLayout = (LinearLayout) view.findViewById(R.id.designIdeaLayout);
        designAchievementLayout = (LinearLayout) view.findViewById(R.id.designAchievementLayout);
        companyLayout = (LinearLayout) view.findViewById(R.id.companyLayout);
        teamCountLayout = (LinearLayout) view.findViewById(R.id.teamCountLayout);
        designFeeLayout = (LinearLayout) view.findViewById(R.id.designFeeLayout);
        jiandanType = (TextView) view.findViewById(R.id.jiandanType);
        jiandanHouseType = (TextView) view.findViewById(R.id.jiandanHouseType);
        jiandanDistrict = (TextView) view.findViewById(R.id.jiandanDistrict);
        designStyle = (TextView) view.findViewById(R.id.designStyle);
        designIdea = (TextView) view.findViewById(R.id.designIdea);
        designAchievement = (TextView) view.findViewById(R.id.designAchievement);
        company = (TextView) view.findViewById(R.id.company);
        teamCount = (TextView) view.findViewById(R.id.teamCount);
        designFee = (TextView) view.findViewById(R.id.designFee);
        getDesignerPageInfo(designerid);
    }

    @Override
    public void setListener() {

    }

    private void getDesignerPageInfo(String designerid) {
        JianFanJiaClient.getDesignerHomePage(getActivity(), designerid, this, this);
    }


    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        DesignerInfo designerInfo = JsonParser.jsonToBean(data.toString(), DesignerInfo.class);
        LogTool.d(TAG, "designerInfo:" + designerInfo);
        if (null != designerInfo) {
            jiandanTypeLayout.setVisibility(View.VISIBLE);
            jiandanHouseTypeLayout.setVisibility(View.VISIBLE);
            jiandanDistrictLayout.setVisibility(View.VISIBLE);
            designStyleLayout.setVisibility(View.VISIBLE);
            designIdeaLayout.setVisibility(View.VISIBLE);
            designAchievementLayout.setVisibility(View.VISIBLE);
            companyLayout.setVisibility(View.VISIBLE);
            teamCountLayout.setVisibility(View.VISIBLE);
            designFeeLayout.setVisibility(View.VISIBLE);
            List<String> decTypes = designerInfo.getDec_types();
            StringBuilder decTypeStr = new StringBuilder();
            for (String str : decTypes) {
                LogTool.d(TAG, "str:" + str);
                decTypeStr.append(BusinessManager.convertDectypeToShow(str) + "  ");
            }
            jiandanType.setText(decTypeStr.toString());
            List<String> decHouseTypes = designerInfo.getDec_house_types();
            StringBuilder decHouseType = new StringBuilder();
            for (String str : decHouseTypes) {
                decHouseType.append(BusinessManager.convertHouseTypeToShow(str) + "  ");
            }
            jiandanHouseType.setText(decHouseType.toString());
            jiandanDistrict.setText(designerInfo.getDistrict());
            List<String> decStyles = designerInfo.getDec_styles();
            StringBuilder decStyleType = new StringBuilder();
            for (String str : decStyles) {
                decStyleType.append(BusinessManager.convertDecStyleToShow(str) + "  ");
            }
            designStyle.setText(decStyleType.toString());
            designIdea.setText(designerInfo.getPhilosophy());
            designAchievement.setText(designerInfo.getAchievement());
            company.setText(designerInfo.getCompany());
            teamCount.setText(designerInfo.getTeam_count() + "");
            String designFeeRange = designerInfo.getDesign_fee_range();
            designFee.setText(BusinessManager.convertDesignFeeToShow(designFeeRange));
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_info;
    }

}