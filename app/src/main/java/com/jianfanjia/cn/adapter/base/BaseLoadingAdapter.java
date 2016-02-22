package com.jianfanjia.cn.adapter.base;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:-- :
 */

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.tools.LogTool;

import java.util.Collection;
import java.util.List;

public abstract class BaseLoadingAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "BaseLoadingAdapter";

    //正常条目
    private static final int TYPE_NORMAL_ITEM = 0;
    //加载条目
    private static final int TYPE_LOADING_ITEM = 1;
    //加载viewHolder
    private LoadingViewHolder mLoadingViewHolder;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    //数据集
    protected List<T> mTs;

    protected boolean isHasLoadMore;//是否还有更多加载

    protected ImageShow imageShow;

    protected Context context;

    protected LayoutInflater layoutInflater;

    private int pageSize;

    public BaseLoadingAdapter(Context context, RecyclerView recyclerView, List<T> ts, int pageSize) {
        mTs = ts;

        setSpanCount(recyclerView);

        setScrollListener(recyclerView);

        this.pageSize = pageSize;

        initLoadMore();

        imageShow = ImageShow.getImageShow();

        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    private OnLoadingListener mOnLoadingListener;

    /**
     * 加载更多接口
     */
    public interface OnLoadingListener {
        void loading();
    }

    private void initLoadMore() {
        if (mTs != null) {
            mTs.add(null);
            if (mTs.size() < pageSize) {
                setLoadingNoMore(false);
            } else {
                setLoadingNoMore(true);
            }
        }
    }

    /**
     * 设置监听接口
     *
     * @param onLoadingListener onLoadingListener
     */
    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    /**
     * 没有更多数据
     */
    private void setLoadingNoMore(boolean isHasLoadMore) {
        this.isHasLoadMore = isHasLoadMore;
    }

    public void addAll(Collection<T> t) {
        if (t != null) {
            if (t.size() == pageSize) {
                setLoadingNoMore(true);
            } else {
                setLoadingNoMore(false);
            }
            int lastIndex = mTs.size() - 1;
            LogTool.d(this.getClass().getName(),"lastIndex =" + lastIndex);
            mTs.addAll(lastIndex, t);
            notifyItemRangeInserted(lastIndex, t.size());
        }
    }

    /**
     * 加载失败
     */
    public void setLoadingError() {
        if (mLoadingViewHolder != null) {
            mLoadingViewHolder.progressBar.setVisibility(View.GONE);
            mLoadingViewHolder.tvLoading.setText("加载失败，点击重新加载！");

            mLoadingViewHolder.tvLoading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadingListener != null) {
                        mLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                        mLoadingViewHolder.tvLoading.setText("正在加载...");

                        mOnLoadingListener.loading();
                    }
                }
            });
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    private boolean canScrollDown(RecyclerView recyclerView) {
        return ViewCompat.canScrollVertically(recyclerView, 1);
    }

    /**
     * 设置每个条目占用的列数
     *
     * @param recyclerView recycleView
     */
    private void setSpanCount(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == TYPE_NORMAL_ITEM) {
                        return 1;
                    } else {
                        return gridLayoutManager.getSpanCount();
                    }
                }
            });
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
    }

    /**
     * 监听滚动事件
     *
     * @param recyclerView recycleView
     */
    private void setScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!canScrollDown(recyclerView) && isHasLoadMore) {
                    if (mOnLoadingListener != null) {
                        mOnLoadingListener.loading();
                    }
                }
            }
        });
    }

    /**
     * 创建viewHolder
     *
     * @param parent viewGroup
     * @return viewHolder
     */
    public abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent);

    /**
     * 绑定viewHolder
     *
     * @param holder   viewHolder
     * @param position position
     */
    public abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 加载布局
     */
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView tvLoading;
        public LinearLayout llyLoading;

        public LoadingViewHolder(View view) {
            super(view);

            progressBar = (ProgressBar) view.findViewById(R.id.progress_loading);
            tvLoading = (TextView) view.findViewById(R.id.tv_loading);
            llyLoading = (LinearLayout) view.findViewById(R.id.lly_loading);
        }
    }

    @Override
    public int getItemViewType(int position) {
        T t = mTs.get(position);
        if (t == null) {
            return TYPE_LOADING_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {
            return onCreateNormalViewHolder(parent);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.loading_view, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_NORMAL_ITEM) {
            onBindNormalViewHolder(holder, position);
        } else {
            onBindLoadMoreViewHolder(holder, position);
            if (mStaggeredGridLayoutManager != null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        new StaggeredGridLayoutManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);

                mLoadingViewHolder.llyLoading.setLayoutParams(layoutParams);
            }
        }
    }

    private void onBindLoadMoreViewHolder(RecyclerView.ViewHolder holder, int position) {
        mLoadingViewHolder = (LoadingViewHolder) holder;
        LogTool.d(this.getClass().getName(), "isLoadMore =" + isHasLoadMore);
        if (!isHasLoadMore) {
            mLoadingViewHolder.progressBar.setVisibility(View.GONE);
            mLoadingViewHolder.tvLoading.setText("数据已全部加载完！");
        } else {
            mLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
            mLoadingViewHolder.tvLoading.setText("正在加载...");
        }
    }

    @Override
    public int getItemCount() {
        return mTs.size();
    }

}
