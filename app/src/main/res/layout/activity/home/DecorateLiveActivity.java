package layout.activity.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.adapter.DecorateLiveFragmentPagerAdapter;
import com.jianfanjia.cn.view.MainHeadView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Description: com.jianfanjia.cn.activity.home
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-08 17:49
 */
@EActivity(R.layout.activity_decorate_live)
public class DecorateLiveActivity extends SwipeBackActivity {

    private static final String TAG = DecorateLiveActivity.class.getName();
    @ViewById(R.id.decorate_live_head_layout)
    protected MainHeadView mainHeadView = null;
    @ViewById(R.id.tablayout)
    protected TabLayout tabLayout = null;
    @ViewById(R.id.viewpager)
    protected ViewPager viewPager = null;

    @AfterViews
    protected void initAnnotationView(){

        mainHeadView.setMianTitle(getString(R.string.decoration_live));
        initViewPagerAndTab();
    }

    protected void initViewPagerAndTab() {
        viewPager.setAdapter(new DecorateLiveFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Click({R.id.head_back_layout})
    protected void click(View view){
        switch (view.getId()){
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }


}
