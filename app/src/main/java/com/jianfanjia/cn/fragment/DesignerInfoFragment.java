package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScrollableHelper;

import butterknife.Bind;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ApiUiUpdateListener, ScrollableHelper
        .ScrollableContainer {
    private static final String TAG = DesignerInfoFragment.class.getName();

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.jiandanTypeLayout)
    LinearLayout jiandanTypeLayout;

    @Bind(R.id.jiandanHouseTypeLayout)
    LinearLayout jiandanHouseTypeLayout;

    @Bind(R.id.jiandanDistrictLayout)
    LinearLayout jiandanDistrictLayout;

    @Bind(R.id.designStyleLayout)
    LinearLayout designStyleLayout;

    @Bind(R.id.designIdeaLayout)
    LinearLayout designIdeaLayout;

    @Bind(R.id.designAchievementLayout)
    LinearLayout designAchievementLayout;

    @Bind(R.id.companyLayout)
    LinearLayout companyLayout;

    @Bind(R.id.teamCountLayout)
    LinearLayout teamCountLayout;

    @Bind(R.id.designFeeLayout)
    LinearLayout designFeeLayout;

    @Bind(R.id.jiandanType)
    TextView jiandanType;

    @Bind(R.id.jiandanHouseType)
    TextView jiandanHouseType;

    @Bind(R.id.jiandanDistrict)
    TextView jiandanDistrict;

    @Bind(R.id.designStyle)
    TextView designStyle;

    @Bind(R.id.designIdea)
    TextView designIdea;

    @Bind(R.id.designAchievement)
    TextView designAchievement;

    @Bind(R.id.company)
    TextView company;

    @Bind(R.id.teamCount)
    TextView teamCount;

    @Bind(R.id.designFee)
    TextView designFee;

    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;

    private String designerid = null;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public static DesignerInfoFragment newInstance(String info) {
        Bundle args = new Bundle();
        DesignerInfoFragment infoFragment = new DesignerInfoFragment();
        args.putString(Global.DESIGNER_ID, info);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        designerid = bundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid=" + designerid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isPrepared = true;
        load();
        return view;
    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getDesignerPageInfo(designerid);
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_info;
    }
}
