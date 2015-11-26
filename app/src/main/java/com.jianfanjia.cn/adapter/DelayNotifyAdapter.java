package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.DelayInfoListener;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * @author zhanghao
 * @class DelayNotifyAdapter
 * @date 2015-8-26 15:57
 */
public class DelayNotifyAdapter extends BaseListAdapter<NotifyDelayInfo> {
    private DelayInfoListener listener;
    private String userType;

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList) {
        super(context, delayList);
    }

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList,
                              DelayInfoListener listener) {
        super(context, delayList);
        this.listener = listener;
        userType = dataManager.getUserType();
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        NotifyDelayInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_tip_delay,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemContentView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_content);
            viewHolder.itemNameView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_name);
            viewHolder.itemNodeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_node);
            viewHolder.itemPubTimeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_time);
            viewHolder.itemAgressView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_status);
            viewHolder.itemAgreeText = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_agree);
            viewHolder.itemRefuseText = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_refuse);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String requestRole = info.getRequest_role();
        String status = info.getStatus();
        if (status.equals(Constant.NO_START)) {
            viewHolder.itemAgressView.setText("未开工");
            viewHolder.itemAgreeText.setVisibility(View.GONE);
            viewHolder.itemRefuseText.setVisibility(View.GONE);
        } else if (status.equals(Constant.DOING)) {
            viewHolder.itemAgressView.setText("进行中");
            viewHolder.itemAgreeText.setVisibility(View.GONE);
            viewHolder.itemRefuseText.setVisibility(View.GONE);
        } else if (status.equals(Constant.FINISHED)) {
            viewHolder.itemAgressView.setText("已完成");
            viewHolder.itemAgreeText.setVisibility(View.GONE);
            viewHolder.itemRefuseText.setVisibility(View.GONE);
        } else if (status.equals(Constant.YANQI_BE_DOING)) {
            viewHolder.itemAgressView.setText("改期申请中");
            if (userType.equals(Constant.IDENTITY_DESIGNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您的业主已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.VISIBLE);
                    viewHolder.itemRefuseText.setVisibility(View.VISIBLE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                }
            } else if (userType.equals(Constant.IDENTITY_OWNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您的设计师已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.VISIBLE);
                    viewHolder.itemRefuseText.setVisibility(View.VISIBLE);
                }
            }
        } else if (status.equals(Constant.YANQI_AGREE)) {
            viewHolder.itemAgressView.setText("改期同意");
            if (userType.equals(Constant.IDENTITY_DESIGNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您的业主已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                }
            } else if (userType.equals(Constant.IDENTITY_OWNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您的设计师已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                }
            }
        } else if (status.equals(Constant.YANQI_REFUSE)) {
            viewHolder.itemAgressView.setText("改期拒绝");
            if (userType.equals(Constant.IDENTITY_DESIGNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您的业主已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                }
            } else if (userType.equals(Constant.IDENTITY_OWNER)) {
                if (requestRole.equals(Constant.IDENTITY_OWNER)) {
                    viewHolder.itemNameView.setText("您已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                } else if (requestRole.equals(Constant.IDENTITY_DESIGNER)) {
                    viewHolder.itemNameView.setText("您的设计师已申请改期验收至");
                    viewHolder.itemAgreeText.setVisibility(View.GONE);
                    viewHolder.itemRefuseText.setVisibility(View.GONE);
                }
            }
        }
        viewHolder.itemContentView.setText(DateFormatTool.longToString(info
                .getNew_date()));
        viewHolder.itemNodeView.setText(MyApplication.getInstance()
                .getStringById(info.getSection()) + "阶段");
        viewHolder.itemPubTimeView.setText(DateFormatTool
                .toLocalTimeString(info.getRequest_date()));
        viewHolder.itemAgreeText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener.onAgree();
            }

        });
        viewHolder.itemRefuseText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listener.onRefuse();
            }

        });
        return convertView;
    }

    private static class ViewHolder {
        TextView itemNameView;// 延迟标题
        TextView itemContentView;// 延迟内容
        TextView itemNodeView;// 延迟节点
        TextView itemPubTimeView;// 发布时间
        TextView itemAgressView;// 是否同意
        TextView itemAgreeText;// 同意
        TextView itemRefuseText;// 拒绝
    }

    @Override
    public void loadSuccess(Object data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadFailture(String errorMsg) {
        // TODO Auto-generated method stub

    }

}