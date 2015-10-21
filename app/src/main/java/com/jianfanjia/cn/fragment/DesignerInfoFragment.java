package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ActivityToFragmentInterface;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

/**
 * @author fengliang
 * @ClassName: DesignerInfoFragment
 * @Description: 设计师资料
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerInfoFragment extends BaseFragment implements ApiUiUpdateListener, ActivityToFragmentInterface {
    private static final String TAG = DesignerInfoFragment.class.getName();
    private TextView jiandanType = null;
    private TextView jiandanHouseType = null;
    private TextView jiandanDistrict = null;
    private TextView designStyle = null;
    private TextView designIdea = null;
    private TextView designAchievement = null;
    private TextView company = null;
    private TextView teamCount = null;
    private TextView designFee = null;

    @Override
    public void initView(View view) {
        jiandanType = (TextView) view.findViewById(R.id.jiandanType);
        jiandanHouseType = (TextView) view.findViewById(R.id.jiandanHouseType);
        jiandanDistrict = (TextView) view.findViewById(R.id.jiandanDistrict);
        designStyle = (TextView) view.findViewById(R.id.designStyle);
        designIdea = (TextView) view.findViewById(R.id.designIdea);
        designAchievement = (TextView) view.findViewById(R.id.designAchievement);
        company = (TextView) view.findViewById(R.id.company);
        teamCount = (TextView) view.findViewById(R.id.teamCount);
        designFee = (TextView) view.findViewById(R.id.designFee);
    }

    @Override
    public void setListener() {

    }

    private void getDesignerPageInfo(String designerid) {
        JianFanJiaClient.getDesignerHomePage(getActivity(), designerid, this, this);
    }

    @Override
    public void toTransmit(DesignerInfo designerInfo) {
        LogTool.d(TAG, "designerInfo----------------------------------------------" + designerInfo);
        jiandanType.setText("接单类型:" + designerInfo.getAchievement());
        jiandanHouseType.setText("接单户型:" + designerInfo.getAchievement());
        jiandanDistrict.setText("接单区域:" + designerInfo.getProvince() + designerInfo.getCity() + designerInfo.getDistrict());
        designStyle.setText("设计风格:" + designerInfo.getAchievement());
        designIdea.setText("设计理念:" + designerInfo.getPhilosophy());
        designAchievement.setText("设计成就:" + designerInfo.getAchievement());
        company.setText("曾就职公司:" + designerInfo.getCompany());
        teamCount.setText("施工团队:" + designerInfo.getTeam_count() + "个");
        designFee.setText("设计费:" + designerInfo.getAchievement());
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        DesignerInfo designerInfo = JsonParser.jsonToBean(data.toString(), DesignerInfo.class);
        LogTool.d(TAG, "designerInfo:" + designerInfo);
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
