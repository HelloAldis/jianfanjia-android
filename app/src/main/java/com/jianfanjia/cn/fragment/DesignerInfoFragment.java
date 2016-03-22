package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends CommonFragment implements ApiUiUpdateListener, ScrollableHelper.ScrollableContainer {
    private static final String TAG = DesignerInfoFragment.class.getName();
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer_info, container, false);
        init(view);
        isPrepared = true;
        load();
        return view;
    }

    public void init(View view) {
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
    protected void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
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
        mHasLoadedOnce = true;
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
            jiandanType.setText(BusinessManager.getDecTypeStr(designerInfo.getDec_types()));
            jiandanHouseType.setText(BusinessManager.getHouseTypeStr(designerInfo.getDec_house_types()));
            jiandanDistrict.setText(BusinessManager.getDecDistrictStr(designerInfo.getDec_districts()));
            designStyle.setText(BusinessManager.getDecStyleStr(designerInfo.getDec_styles()));
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
        makeTextShort(error_msg);
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }

}
