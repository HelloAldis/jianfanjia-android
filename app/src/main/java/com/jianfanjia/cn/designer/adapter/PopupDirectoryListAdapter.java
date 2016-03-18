package com.jianfanjia.cn.designer.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.bean.PhotoDirectory;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 * Created by donglua on 15/6/28.
 */
public class PopupDirectoryListAdapter extends BaseAdapter {

    private Context context;

    private List<PhotoDirectory> directories = new ArrayList<>();

    private LayoutInflater mLayoutInflater;


    public PopupDirectoryListAdapter(Context context, List<PhotoDirectory> directories) {
        this.context = context;
        this.directories = directories;

        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position));

        return convertView;
    }


    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;

        public ViewHolder(View rootView) {
            ivCover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tvName = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tvCount = (TextView) rootView.findViewById(R.id.tv_dir_count);
        }

        public void bindData(PhotoDirectory directory) {
            if (context instanceof Activity && ((Activity) context).isFinishing()) {
                return;
            }
     /* Glide.with(context)
          .load(directory.getCoverPath())
          .dontAnimate()
          .thumbnail(0.1f)
          .into(ivCover);*/
//      Picasso.with(context).load(Uri.fromFile(new File(directory.getCoverPath()))).fit().centerCrop()
//              .into(ivCover);
            ImageLoader.getInstance().displayImage(Uri.fromFile(new File(directory.getCoverPath())).toString(), ivCover);
            tvName.setText(directory.getName());
            tvCount.setText(context.getString(R.string.__picker_image_count, directory.getPhotos().size()));
        }
    }

}
