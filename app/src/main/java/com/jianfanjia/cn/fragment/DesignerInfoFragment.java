package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ApiUiUpdateListener {
    private static final String TAG = DesignerInfoFragment.class.getName();
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
            LogTool.d(TAG, "designerInfo:" + designerInfo);
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
            StringBuffer decTypeStr = new StringBuffer();
            for (String str : decTypes) {
                LogTool.d(TAG, "str:" + str);
                if (str.equals("0")) {
                    decTypeStr.append("家装 ");
                } else if (str.equals("1")) {
                    decTypeStr.append("商装 ");
                } else if (str.equals("2")) {
                    decTypeStr.append("软装");
                }
            }
            jiandanType.setText(decTypeStr.toString());
            List<String> decHouseTypes = designerInfo.getDec_house_types();
            StringBuffer decHouseType = new StringBuffer();
            for (String str : decHouseTypes) {
                LogTool.d(TAG, "str:" + str);
                if (str.equals("0")) {
                    decHouseType.append("一居 ");
                } else if (str.equals("1")) {
                    decHouseType.append("二居 ");
                } else if (str.equals("2")) {
                    decHouseType.append("三居 ");
                } else if (str.equals("3")) {
                    decHouseType.append("四居 ");
                } else if (str.equals("4")) {
                    decHouseType.append("复式 ");
                } else if (str.equals("5")) {
                    decHouseType.append("别墅");
                }
            }
            jiandanHouseType.setText(decHouseType.toString());
            jiandanDistrict.setText(designerInfo.getDistrict());
            List<String> decStyles = designerInfo.getDec_styles();
            StringBuffer decStyleType = new StringBuffer();
            for (String str : decStyles) {
                if (str.equals("0")) {
                    decStyleType.append("欧式 ");
                } else if (str.equals("1")) {
                    decStyleType.append("中式 ");
                } else if (str.equals("2")) {
                    decStyleType.append("现代 ");
                } else if (str.equals("3")) {
                    decStyleType.append("地中海 ");
                } else if (str.equals("4")) {
                    decStyleType.append("美式 ");
                } else if (str.equals("5")) {
                    decStyleType.append("东南亚");
                }
            }
            designStyle.setText(decStyleType.toString());
            designIdea.setText(designerInfo.getPhilosophy());
            designAchievement.setText(designerInfo.getAchievement());
            company.setText(designerInfo.getCompany());
            teamCount.setText(designerInfo.getTeam_count() + "");
            String designFeeRange = designerInfo.getDesign_fee_range();
            String designFeeStr = null;
            if (designFeeRange.equals("0")) {
                designFeeStr = "50-100";
            } else if (designFeeRange.equals("1")) {
                designFeeStr = "100-200";
            } else if (designFeeRange.equals("2")) {
                designFeeStr = "200-300";
            } else if (designFeeRange.equals("3")) {
                designFeeStr = "300以上";
            }
            designFee.setText(designFeeStr);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_info;
    }

}
