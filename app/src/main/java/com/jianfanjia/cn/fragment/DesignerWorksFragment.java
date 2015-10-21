package com.jianfanjia.cn.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.activity.DesignerCaseInfoActivity;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.DesignerWorksAdapter;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.DesignerWorksInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ActivityToFragmentInterface;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengliang
 * @ClassName: DesignerWorksFragment
 * @Description: 设计师作品
 * @date 2015-8-26 下午1:07:52
 */
public class DesignerWorksFragment extends BaseFragment implements OnItemClickListener, ApiUiUpdateListener, ActivityToFragmentInterface {
    private static final String TAG = DesignerWorksFragment.class.getName();
    private ListView designer_works_listview = null;
    private DesignerWorksAdapter adapter = null;
    private List<DesignerWorksInfo> designerWorksList = new ArrayList<DesignerWorksInfo>();

    @Override
    public void initView(View view) {
        designer_works_listview = (ListView) view.findViewById(R.id.designer_works_listview);
        designer_works_listview.setFocusable(false);
        initDesignerWorksList();
    }

    private void initDesignerWorksList() {
//        for (int i = 0; i < 5; i++) {
//            DesignerWorksInfo info = new DesignerWorksInfo();
//            info.setXiaoquName("小区名称" + 1);
//            info.setProduce("100平米,三室二厅,现代简约");
//            designerWorksList.add(info);
//        }
//        adapter = new DesignerWorksAdapter(getActivity(), designerWorksList);
//        designer_works_listview.setAdapter(adapter);

//        getDesignerProduct("55ebfc02d6e8f37706e4f1b7", 0, 5);
    }

    private void getDesignerProduct(String designerid, int from, int limit) {
        JianFanJiaClient.getDesignerProduct(getActivity(), designerid, from, limit, this, this);
    }

    @Override
    public void toTransmit(DesignerInfo designerInfo) {
        String designerid = designerInfo.get_id();
        getDesignerProduct(designerid, 0, 5);
    }

    @Override
    public void setListener() {
        designer_works_listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
        makeTextLong("案例");
        startActivity(DesignerCaseInfoActivity.class);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        DesignerWorksInfo worksInfo = JsonParser.jsonToBean(data.toString(), DesignerWorksInfo.class);
        LogTool.d(TAG, "worksInfo :" + worksInfo);
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_designer_works;
    }

}
