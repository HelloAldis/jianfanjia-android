package com.jianfanjia.cn.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.request.guest.DesignerHomePageRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.ScrollableHelper;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ScrollableHelper
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
        args.putString(IntentConstant.DESIGNER_ID, info);
        infoFragment.setArguments(args);
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        designerid = bundle.getString(IntentConstant.DESIGNER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "onCreateView()");
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
        if(TextUtils.isEmpty(designerid)) return;
        DesignerHomePageRequest request = new DesignerHomePageRequest();
        request.set_id(designerid);
        Api.getDesignerHomePage(request, new ApiCallback<ApiResponse<Designer>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<Designer> apiResponse) {
                mHasLoadedOnce = true;
                Designer designerInfo = apiResponse.getData();
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
                    jiandanType.setText(BusinessCovertUtil.getDecTypeStr(designerInfo.getDec_types()));
                    jiandanHouseType.setText(BusinessCovertUtil.getHouseTypeStr(designerInfo.getDec_house_types()));
                    jiandanDistrict.setText(BusinessCovertUtil.getDecDistrictStr(designerInfo.getDec_districts()));
                    designStyle.setText(BusinessCovertUtil.getDecStyleStr(designerInfo.getDec_styles()));
                    designIdea.setText(designerInfo.getPhilosophy());
                    designAchievement.setText(designerInfo.getAchievement());
                    company.setText(designerInfo.getCompany());
                    teamCount.setText(designerInfo.getTeam_count() + "");
                    String designFeeRange = designerInfo.getDesign_fee_range();
                    designFee.setText(BusinessCovertUtil.convertDesignFeeToShow(designFeeRange));
                }
            }

            @Override
            public void onFailed(ApiResponse<Designer> apiResponse) {
            }

            @Override
            public void onNetworkError(int code) {
            }
        });

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
