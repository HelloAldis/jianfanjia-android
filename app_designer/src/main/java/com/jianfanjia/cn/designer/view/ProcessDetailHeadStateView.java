package com.jianfanjia.cn.designer.view;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.Reschedule;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.designer.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-04-25 10:03
 */
public class ProcessDetailHeadStateView extends FrameLayout {

    private static final String TAG = ProcessDetailHeadStateView.class.getName();

    @Bind(R.id.site_list_head_checkbutton_layout)
    LinearLayout site_list_head_checkbutton_layout;

    @Bind(R.id.check_head_layout)
    LinearLayout site_list_head_check_layout;

    @Bind(R.id.site_list_head_content_layout)
    LinearLayout site_list_head_content_layout;

    @Bind(R.id.site_list_head_delay_layout)
    LinearLayout site_list_head_delay_layout;

    @Bind(R.id.site_list_head_delay)
    TextView openDelay;

    @Bind(R.id.indicator_image)
    ImageView indicatorImage;

    @Bind(R.id.rowBtnUp)
    ImageView rowBtnUp;

    @Bind(R.id.rowBtnUpLayout)
    RelativeLayout rowBtnUpLayout;

    public ProcessDetailHeadStateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.custom_processdetail_stateview, null);
        addView(view);
        ButterKnife.bind(this);

        rowBtnUpLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.d(TAG, "rowBtnUp animate");
                openOrCloseAnimate();
            }
        });
    }

    private void openOrCloseAnimate() {
        rowBtnUp.animate().rotationBy(180).setDuration(100).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ViewPropertyAnimator layoutAni;
                if (site_list_head_content_layout.getVisibility() == GONE) {
                    layoutAni = site_list_head_content_layout.animate().yBy
                            (site_list_head_content_layout.getHeight()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            site_list_head_content_layout.setVisibility(VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).setDuration(200);
                } else {
                    layoutAni = site_list_head_content_layout.animate().yBy
                            (-site_list_head_content_layout.getHeight()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            site_list_head_content_layout.setVisibility(GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).setDuration(200);
                }
                layoutAni.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    public void resetCheckLayout(ProcessSection processSection) {
        String section_status = processSection.getStatus();
        LogTool.d(TAG, "section_status=" + section_status);
        showHead();
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
                    if (role.equals(Constant.IDENTITY_DESIGNER)) {
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
        if (site_list_head_check_layout.getVisibility() == GONE) {
            site_list_head_check_layout.animate().yBy(site_list_head_check_layout.getHeight()).alpha(1).setDuration
                    (300).setListener(new Animator
                    .AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    site_list_head_check_layout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();

        }
    }

    public void changeCheckLayoutState(ProcessSection processSection, boolean isReset) {
        if (processSection.getName().equals("kai_gong")
                || processSection.getName().equals("chai_gai")) {
            LogTool.d(TAG, "dismiss head");
            if (site_list_head_check_layout.getVisibility() == VISIBLE) {
                site_list_head_check_layout.animate().yBy(-site_list_head_check_layout.getHeight()).alpha(0)
                        .setDuration(300).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        site_list_head_check_layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
            return;
        }

        if (isReset) {
            resetCheckLayout(processSection);
        } else {
            int finishCount = 0;
            for (ProcessSectionItem processSectionItem : processSection.getItems()) {
                if (processSectionItem.getStatus().equals(Constant.FINISHED)) {
                    finishCount++;
                }
            }
            if (finishCount == processSection.getItems().size() - 1) {
                resetCheckLayout(processSection);
            }
        }


    }


}
