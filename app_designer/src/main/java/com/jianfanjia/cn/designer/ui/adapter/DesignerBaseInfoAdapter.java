package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DesignerAwardInfo;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info.BaseInfoAuthActicity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.product_info.CustomeUploadProdcutMenuLayout;
import com.jianfanjia.cn.designer.ui.interf.helper.ItemTouchHelperViewHolder;
import com.jianfanjia.cn.designer.view.custom_edittext.CustomEditText;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.designer.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-26 13:19
 */
public class DesignerBaseInfoAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BASE = 0;
    private static final int VIEW_TYPE_AWARD = 1;
    private static final int VIEW_TYPE_ADD = 2;

    List<DesignerAwardInfo> mDesignerAwardInfoList;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Designer mDesigner;
    private android.os.Handler mHandler = new android.os.Handler();
    private UploadAwardImgViewHolder.ItemClickAction mItemClickAction;
    private int changeAwardImagePosition;
    int mCurrentStatus;

    public DesignerBaseInfoAdapter(Context context, Designer designer,UploadAwardImgViewHolder.ItemClickAction itemClickAction) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDesigner = designer;
        this.mDesignerAwardInfoList = designer.getAward_details();
        this.mItemClickAction = itemClickAction;
    }

    public void changeShowStatus(int currentStatus){
        this.mCurrentStatus = currentStatus;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_BASE:
                view = mLayoutInflater.inflate(R.layout.list_item_designer_baseinfo, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new BaseInfoViewHolder(view);
            case VIEW_TYPE_ADD:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_product_plan_img_add, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadAddImageViewHolder(view);
            case VIEW_TYPE_AWARD:
                view = mLayoutInflater.inflate(R.layout.list_item_upload_award_img, null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new UploadAwardImgViewHolder(view, new EditTextTextWatcher());
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ADD:
                bindAddViewHolder((UploadAddImageViewHolder) holder);
                break;
            case VIEW_TYPE_AWARD:
                bingAwardViewHolder((UploadAwardImgViewHolder) holder, position);
                break;
            case VIEW_TYPE_BASE:
                bindBaseInfoViewHolder((BaseInfoViewHolder) holder);
                break;
        }
    }

    private void bindBaseInfoViewHolder(final BaseInfoViewHolder holder) {
        mItemClickAction.changeCommitStatus();//通知提交按钮显示

        if (!TextUtils.isEmpty(mDesigner.getImageid())) {
            ImageShow.getImageShow().displayImageHeadWidthThumnailImage(mContext, mDesigner.getImageid(), holder
                    .headView);
        } else {
            holder.headView.setImageResource(R.mipmap.icon_default_head);
        }

        holder.headLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickAction.uploadHead();
            }
        });

        holder.nameEditText.setText(mDesigner.getUsername());
        holder.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setUsername(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.sexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickAction.inputSex();
            }
        });
        String sexInfo = mDesigner.getSex();
        if (!TextUtils.isEmpty(sexInfo)) {
            if (sexInfo.equals(Constant.SEX_MAN)) {
                holder.sexTextView.setText(mContext.getString(R.string.man));
            } else if (sexInfo.equals(Constant.SEX_WOMEN)) {
                holder.sexTextView.setText(mContext.getString(R.string.women));
            }
        } else {
            holder.sexTextView.setText(null);
        }

        holder.addrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickAction.inputAddress();
            }
        });
        String city = mDesigner.getCity();
        if (TextUtils.isEmpty(city)) {
            holder.addrTextView
                    .setText(null);
        } else {
            String province = mDesigner.getProvince();
            String district = mDesigner.getDistrict();
            holder.addrTextView.setText(province + " " + city + " " + (TextUtils.isEmpty(district) ? ""
                    : district));
        }

        holder.emailEditText.setText(mDesigner.getAddress());
        holder.emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setAddress(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.designerConceptEditText.setText(mDesigner.getPhilosophy());
        holder.designerConceptEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setPhilosophy(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.universityEditText.setText(mDesigner.getUniversity());
        holder.universityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setUniversity(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.workCompanyEditext.setText(mDesigner.getCompany());
        holder.workCompanyEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setCompany(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (mDesigner.getWork_year() != 0) {
            holder.workYearEditext.setText(mDesigner.getWork_year() + "");
        }
        holder.workYearEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    mDesigner.setWork_year(Integer.parseInt(s.toString()));
                } else {
                    mDesigner.setWork_year(0);
                }
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.designAchievementEdittext.setText(mDesigner.getAchievement());
        holder.designAchievementEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDesigner.setAchievement(s.toString());
                mItemClickAction.changeCommitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String diplomaImageid = mDesigner.getDiploma_imageid();
        if(!TextUtils.isEmpty(diplomaImageid)){
            holder.uploadDiplomaImageLayput.setVisibility(View.GONE);
            holder.diplomaImageLayout.setVisibility(View.VISIBLE);

            ImageShow.getImageShow().displayScreenWidthThumnailImage(mContext, diplomaImageid, holder.diplomaImageView);
            holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDesigner.setDiploma_imageid("");
                    notifyItemChanged(0);
                }
            });
        }else {
            holder.diplomaImageView.setImageResource(R.mipmap.icon_default_pic);
            holder.uploadDiplomaImageLayput.setVisibility(View.VISIBLE);
            holder.diplomaImageLayout.setVisibility(View.GONE);
            holder.uploadDiplomaImageLayput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickAction.uploadDiplomaImage();
                }
            });
        }

        if(mCurrentStatus == BaseInfoAuthActicity.CURRENT_STATUS_EDIT){
            holder.headLayout.setEnabled(true);
            holder.nameEditText.setEnabled(true);
            holder.sexLayout.setEnabled(true);
            holder.addrLayout.setEnabled(true);
            holder.emailEditText.setEnabled(true);
            holder.designerConceptEditText.setEnabled(true);
            holder.designAchievementEdittext.setEnabled(true);
            holder.universityEditText.setEnabled(true);
            holder.diplomaImageLayout.setEnabled(true);
            holder.deleteImageView.setVisibility(View.VISIBLE);
            holder.diplomaImageView.setEnabled(true);
            holder.workYearEditext.setEnabled(true);
            holder.workCompanyEditext.setEnabled(true);
            holder.uploadDiplomaImageLayput.setEnabled(true);
        }else {
            holder.headLayout.setEnabled(false);
            holder.nameEditText.setEnabled(false);
            holder.sexLayout.setEnabled(false);
            holder.addrLayout.setEnabled(false);
            holder.emailEditText.setEnabled(false);
            holder.designerConceptEditText.setEnabled(false);
            holder.designAchievementEdittext.setEnabled(false);
            holder.universityEditText.setEnabled(false);
            holder.diplomaImageLayout.setEnabled(false);
            holder.deleteImageView.setVisibility(View.GONE);
            holder.diplomaImageView.setEnabled(false);
            holder.workYearEditext.setEnabled(false);
            holder.workCompanyEditext.setEnabled(false);
            holder.uploadDiplomaImageLayput.setEnabled(false);
        }
    }

    private void bingAwardViewHolder(final UploadAwardImgViewHolder holder,final int position) {
        final DesignerAwardInfo designerAwardInfo = mDesignerAwardInfoList.get(position - 1);

        final String imageid = designerAwardInfo.getAward_imageid();

        ImageShow.getImageShow().displayScreenWidthThumnailImage(mContext, imageid, holder.uploadProductImg);

        //设置封面
        if (!designerAwardInfo.isMenuOpen()) {
            holder.btnUploadProduct.setOpenStatus();
        } else {
            holder.btnUploadProduct.setCloseStatus();
        }

        holder.ivCover.setVisibility(View.GONE);

        holder.clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDesignerAwardInfoList.remove(position - 1);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mDesignerAwardInfoList.size());
            }
        });

        holder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAwardImagePosition = position - 1;
                mItemClickAction.updateAwardImage();
            }
        });

        holder.mEditTextTextWatcher.updatePosition(position - 1);
        holder.mEditImageIntroText.setText(designerAwardInfo.getDescription());

        if(mCurrentStatus == BaseInfoAuthActicity.CURRENT_STATUS_EDIT){
            holder.btnUploadProduct.setVisibility(View.VISIBLE);
            holder.mEditImageIntroText.setEnabled(true);
        }else{
            holder.btnUploadProduct.setVisibility(View.GONE);
            holder.mEditImageIntroText.setEnabled(false);
        }

    }

    public void addAwardImageItem(DesignerAwardInfo designerAwardInfo){
        mDesignerAwardInfoList.add(designerAwardInfo);
        notifyItemInserted(1 + mDesignerAwardInfoList.size());
    }

    public void changeAwardImageItem(DesignerAwardInfo designerAwardInfo){
        mDesignerAwardInfoList.set(changeAwardImagePosition,designerAwardInfo);
        notifyItemChanged(1 + changeAwardImagePosition);
    }

    private void bindAddViewHolder(UploadAddImageViewHolder holder) {
        holder.uploadPlanImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickAction.uploadAwardImage();
            }
        });

        if(mCurrentStatus == BaseInfoAuthActicity.CURRENT_STATUS_EDIT){
            holder.uploadPlanImgLayout.setEnabled(true);
        }else{
            holder.uploadPlanImgLayout.setEnabled(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_BASE;
        } else if (position >= 1 && position < 1 + mDesignerAwardInfoList.size()) {
            return VIEW_TYPE_AWARD;
        } else {
            return VIEW_TYPE_ADD;
        }
    }

    @Override
    public int getItemCount() {
        return 2 + mDesignerAwardInfoList.size();
    }

    static class BaseInfoViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.head_icon)
        ImageView headView;

        @Bind(R.id.head_layout)
        RelativeLayout headLayout;

        @Bind(R.id.name_content)
        EditText nameEditText;

        @Bind(R.id.sex_content)
        TextView sexTextView;

        @Bind(R.id.sex_layout)
        RelativeLayout sexLayout;

        @Bind(R.id.address_content)
        TextView addrTextView;

        @Bind(R.id.address_layout)
        RelativeLayout addrLayout;

        @Bind(R.id.email_addr_content)
        CustomEditText emailEditText;

        @Bind(R.id.upload_product_img)
        ImageView diplomaImageView;

        @Bind(R.id.btn_delete)
        ImageView deleteImageView;

        @Bind(R.id.show_diploma_img_layout)
        LinearLayout diplomaImageLayout;

        @Bind(R.id.upload_diploma_img_add)
        LinearLayout uploadDiplomaImageLayput;

        @Bind(R.id.design_concept_editext)
        CustomEditText designerConceptEditText;

        @Bind(R.id.university_content)
        EditText universityEditText;

        @Bind(R.id.work_company_content)
        EditText workCompanyEditext;

        @Bind(R.id.work_year_content)
        EditText workYearEditext;

        @Bind(R.id.design_achievement_editext)
        CustomEditText designAchievementEdittext;


        public BaseInfoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class UploadAddImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.upload_plan_img_layout)
        RelativeLayout uploadPlanImgLayout;

        public UploadAddImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class UploadAwardImgViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @Bind(R.id.contentLayout)
        LinearLayout contentLayout;

        @Bind(R.id.upload_product_img)
        ImageView uploadProductImg;

        @Bind(R.id.btn_upload_product)
        CustomeUploadProdcutMenuLayout btnUploadProduct;

        @Bind(R.id.product_intro_editext)
        CustomEditText mEditImageIntroText;

        @Bind(R.id.btn_edit)
        ImageView editView;

        @Bind(R.id.move_indication)
        ImageView moveIndicationView;

        @Bind(R.id.btn_clear)
        ImageView clearView;

        @Bind(R.id.upload_product_cover)
        ImageView ivCover;

        private LinearLayout.LayoutParams sourceLayoutParams;

        private EditTextTextWatcher mEditTextTextWatcher;

        public UploadAwardImgViewHolder(View view, EditTextTextWatcher editTextTextWatcher) {
            super(view);
            ButterKnife.bind(this, view);
            this.btnUploadProduct.setIsHasSettingCover(false);
            this.mEditTextTextWatcher = editTextTextWatcher;
            mEditImageIntroText.addTextChangedListener(editTextTextWatcher);
            sourceLayoutParams = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
        }

        public interface ItemClickAction {
            void changeCommitStatus();
            void uploadHead();
            void inputSex();
            void inputAddress();
            void uploadDiplomaImage();
            void uploadAwardImage();
            void updateAwardImage();
        }

        @Override
        public void onItemClear() {
            contentLayout.setLayoutParams(sourceLayoutParams);
            btnUploadProduct.setVisibility(View.VISIBLE);
            moveIndicationView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onItemSelected() {
            contentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice
                    .dip2px(MyApplication.getInstance(), 80)));
            btnUploadProduct.setVisibility(View.GONE);
            moveIndicationView.setVisibility(View.GONE);
        }

    }

    private class EditTextTextWatcher implements TextWatcher {

        private int position;

        public void updatePosition(int pos) {
            this.position = pos;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDesignerAwardInfoList.get(position).setDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
