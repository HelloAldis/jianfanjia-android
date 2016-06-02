package com.jianfanjia.cn.activity.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.Reschedule;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-25 10:03
 */
public class ProcessDetailHeadStateView extends LinearLayout {

    private static final String TAG = ProcessDetailHeadStateView.class.getName();

    @Bind(R.id.site_list_head_checkbutton_layout)
    LinearLayout site_list_head_checkbutton_layout;

    @Bind(R.id.site_list_head_delay_layout)
    LinearLayout site_list_head_delay_layout;

    @Bind(R.id.site_list_head_delay)
    TextView openDelay;

    public ProcessDetailHeadStateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custom_processdetail_stateview, null);
        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this);

        setOrientation(VERTICAL);


    }

    public void resetCheckLayout(ProcessSection processSection) {
        String section_status = processSection.getStatus();
        LogTool.d(TAG, "section_status=" + section_status);
        switch (section_status) {
            case Constant.FINISHED:
                site_list_head_delay_layout.setVisibility(View.GONE);
                site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                openDelay.setEnabled(false);
                openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                openDelay.setText(getResources().getText(R.string
                        .site_example_node_delay_no));
                break;
            case Constant.NO_START:
                site_list_head_delay_layout.setVisibility(View.GONE);
                site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                openDelay.setEnabled(false);
                openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                openDelay.setText(getResources().getText(R.string.site_example_node_delay));
                break;
            case Constant.YANQI_AGREE:
            case Constant.YANQI_REFUSE:
            case Constant.DOING:
                site_list_head_delay_layout.setVisibility(View.GONE);
                site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                openDelay.setEnabled(true);
                openDelay.setTextColor(getResources().getColor(R.color.orange_color));
                openDelay.setText(getResources().getText(R.string.site_example_node_delay));
                break;
            case Constant.YANQI_BE_DOING:
                LogTool.d(TAG, "this section is yanqi_doing");
                Reschedule reschedule = processSection.getReschedule();
                if (null != reschedule) {
                    String role = reschedule.getRequest_role();
                    if (role.equals(Constant.IDENTITY_OWNER)) {
                        site_list_head_delay_layout.setVisibility(View.GONE);
                        site_list_head_checkbutton_layout.setVisibility(View.VISIBLE);
                        openDelay.setTextColor(getResources().getColor(R.color.grey_color));
                        openDelay.setText(getResources().getText(R.string
                                .site_example_node_delay_doing));
                        openDelay.setEnabled(false);
                    } else {
                        site_list_head_delay_layout.setVisibility(View.VISIBLE);
                        site_list_head_checkbutton_layout.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showHead() {
        LogTool.d(TAG, "show head");
//        openCheckLayout();
//        if (site_list_head_check_layout.getVisibility() == GONE) {
//            LogTool.d(TAG, "check height =" + site_list_head_check_layout.getHeight());
//            site_list_head_check_layout.animate().yBy(site_list_head_check_layout.getHeight()).alpha(1).setDuration
//                    (300).setListener(new Animator
//                    .AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    site_list_head_check_layout.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            }).start();

//        }
    }



    public void changeCheckLayoutState(ProcessSection processSection, boolean isReset) {
//        if (processSection.getName().equals("kai_gong")
//                || processSection.getName().equals("chai_gai")) {
//            LogTool.d(TAG, "dismiss head");
//            if (site_list_head_check_layout.getVisibility() == VISIBLE) {
//                indicatorImage.setVisibility(INVISIBLE);
//                LogTool.d(TAG, "close check height =" + site_list_head_check_layout.getHeight());
//                post(new Runnable() {
//                    @Override
//                    public void run() {
//                        site_list_head_check_layout.animate().yBy(-site_list_head_check_layout.getHeight()).alpha(0)
//                                .setDuration(300).setListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                site_list_head_check_layout.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animation) {
//
//                            }
//                        }).start();
//                    }
//                });
//            }
//            return;
//        }
        resetCheckLayout(processSection);
        if (isReset) {
            showHead();
        }

    }


}
