package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.fragment.XuQiuFragment;
import com.jianfanjia.cn.interf.ClickCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-15 13:19
 */
public class RequirementAdapter extends BaseListAdapter<RequirementInfo> {

    private ClickCallBack clickCallBack;

    public RequirementAdapter(Context context, List<RequirementInfo> list,ClickCallBack clickCallBack) {
        super(context, list);
        this.clickCallBack = clickCallBack;
    }

    @Override
    public View initView(final int position, View convertView) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.list_item_req,null,false);
            viewHolder = new ViewHolder();
            viewHolder.ltm_req_cell = (TextView)convertView.findViewById(R.id.ltm_req_cell);
            viewHolder.ltm_req_edit = (ImageButton)convertView.findViewById(R.id.ltm_req_edit);
            viewHolder.ltm_req_publish_time = (TextView)convertView.findViewById(R.id.ltm_req_starttime_cont);
            viewHolder.ltm_req_update_time = (TextView)convertView.findViewById(R.id.ltm_req_updatetime_cont);
            viewHolder.ltm_req_owner_head = (ImageView)convertView.findViewById(R.id.ltm_req_owner_head);
            viewHolder.ltm_req_gotopro = (TextView)convertView.findViewById(R.id.ltm_req_gotopro);
            for(int i = 0 ;i< 3; i++) {
                DesignerItem designerItem = new DesignerItem();
                designerItem.ltm_req_designer_head = (ImageView) convertView.findViewById(context.getResources().getIdentifier("ltm_req_designer_head" + i, "id", context.getPackageName()));
                designerItem.ltm_req_designer_name = (TextView) convertView.findViewById(context.getResources().getIdentifier("ltm_req_designer_name" + i, "id", context.getPackageName()));
                designerItem.ltm_req_designer_status = (TextView) convertView.findViewById(context.getResources().getIdentifier("ltm_req_designer_status" + i, "id", context.getPackageName()));
                viewHolder.ltm_req_designerItems.add(designerItem);
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ltm_req_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.click(position, XuQiuFragment.ITEM_EDIT);
            }
        });
        return convertView;
    }

    class ViewHolder{

        public ViewHolder(){
            ltm_req_designerItems.clear();
        }
        protected TextView ltm_req_cell;
        protected TextView ltm_req_publish_time;
        protected TextView ltm_req_update_time;
        protected ImageButton ltm_req_edit;
        protected ImageView ltm_req_owner_head;
        protected TextView ltm_req_gotopro;
        protected List<DesignerItem> ltm_req_designerItems = new ArrayList<DesignerItem>();
        protected ImageView ltm_req_designer_head0;
        protected TextView ltm_req_designer_name0;
        protected TextView ltm_req_designer_status0;
        protected ImageView ltm_req_designer_head1;
        protected TextView ltm_req_designer_name1;
        protected TextView ltm_req_designer_status1;
        protected ImageView ltm_req_designer_head2;
        protected TextView ltm_req_designer_name2;
        protected TextView ltm_req_designer_status2;
    }

    class DesignerItem{
        protected ImageView ltm_req_designer_head;
        protected TextView ltm_req_designer_name;
        protected TextView ltm_req_designer_status;
    }
}
