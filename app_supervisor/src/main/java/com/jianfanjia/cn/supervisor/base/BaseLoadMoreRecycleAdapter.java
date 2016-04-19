package com.jianfanjia.cn.supervisor.base;

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

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.tools.ImageShow;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-29 14:37
 */
public abstract class BaseLoadMoreRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int STATE_LOAD_MORE = 0x111;//正在加载状态
    public static final int STATE_NO_MORE = 0x112;//没有更多数据加载
    public static final int STATE_NO_DATA = 0x113;//没有任何数据，list为空
    public static final int STATE_INIT = 0x114;//初始状态
    public static final int STATE_NETWORK_ERROR = 0x115;//数据加载错误

    //正常条目
    protected static final int TYPE_NORMAL_ITEM = 0x116;//正常item
    //加载条目
    protected static final int TYPE_LOADING_ITEM = 0x117;//加载item

    protected ArrayList<T> mDatas = new ArrayList<>();

    private int state = STATE_INIT;

    private boolean isHasFooterView = true;

    private LoadMoreListener loadMoreListener;

    //加载viewHolder
    private LoadingViewHolder mLoadingViewHolder;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    protected ImageShow imageShow;

    protected Context context;

    protected LayoutInflater layoutInflater;

    private View emptyView;

    private View errorView;

    private RecyclerView recyclerView;

    public BaseLoadMoreRecycleAdapter(Context context, RecyclerView recyclerView) {

        this.recyclerView = recyclerView;

        initFooterViewSpanSize(recyclerView);

        setScrollListener(recyclerView);

        imageShow = ImageShow.getImageShow();

        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setState(int state) {
        this.state = state;
        if (isHasFooterView()) {
            if (mDatas.size() > 0) {
                //设置状态只需通知改变footerview的显示
                notifyItemChanged(getItemCount() - 1);
            }
        }
    }

    public int getState() {
        return this.state;
    }

    public boolean isHasFooterView() {
        return isHasFooterView;
    }

    public void setIsHasFooterView(boolean isHasFooterView) {
        this.isHasFooterView = isHasFooterView;
    }

    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setData(ArrayList<T> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return mDatas == null ? (mDatas = new ArrayList<T>()) : mDatas;
    }

    public void addData(List<T> data) {
        if (mDatas != null && data != null && !data.isEmpty()) {
            for (T t : data) {
                addItem(t);
            }
        } else {
            notifyDataSetChanged();
        }
    }

    public void addItem(T obj) {
        if (mDatas != null) {
            mDatas.add(obj);
            notifyItemInserted(mDatas.size());
        }
    }

    public void addItem(int pos, T obj) {
        if (mDatas != null) {
            mDatas.add(pos, obj);
            notifyItemInserted(pos);
        }
    }

    public void updateItem(T obj) {
        if (mDatas != null) {
            int pos = mDatas.indexOf(obj);
            if (pos > -1) {
                notifyItemChanged(pos);
            }
        }
    }

    public void removeItem(int pos) {
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            if (isHasFooterView) {
                return mDatas.size() + 1;
            }
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHasFooterView && position == mDatas.size()) {
            return TYPE_LOADING_ITEM;
        }
        return TYPE_NORMAL_ITEM;
    }

    /**
     * 设置每个条目占用的列数
     *
     * @param recyclerView recycleView
     */
    private void initFooterViewSpanSize(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isHasFooterView && position == gridLayoutManager.getItemCount() - 1) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
    }

    /**
     * 创建viewHolder
     *
     * @param parent viewGroup
     * @return viewHolder
     */
    public abstract RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定viewHolder
     *
     * @param holder   viewHolder
     * @param position position
     */
    public abstract void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADING_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.loading_view, parent, false);
            return new LoadingViewHolder(view);
        } else {
            return onCreateNormalViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_LOADING_ITEM) {
            onBindLoadMoreViewHolder(holder, position);
            if (mStaggeredGridLayoutManager != null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams =
                        new StaggeredGridLayoutManager.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);

                mLoadingViewHolder.llyLoading.setLayoutParams(layoutParams);
            }
        } else {
            onBindNormalViewHolder(holder, position);
        }
    }

    public void hideErrorAndEmptyView() {
        if (errorView != null) {
            LogTool.d(this.getClass().getName(), "set_error_view =");
            errorView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void setErrorViewShow() {
        if (mDatas.size() == 0) {
            if (errorView != null) {
                LogTool.d(this.getClass().getName(), "set_error_view =");
                errorView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public void setEmptyViewShow() {
        if (mDatas.size() == 0) {
            if (emptyView != null) {
                LogTool.d(this.getClass().getName(), "set_empty_view =");
                emptyView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void onBindLoadMoreViewHolder(RecyclerView.ViewHolder holder, int position) {
        mLoadingViewHolder = (LoadingViewHolder) holder;
        LogTool.d(this.getClass().getName(), "isHasLoadMore =" + state);
        switch (state) {
            case STATE_NETWORK_ERROR:
                mLoadingViewHolder.llyLoading.setVisibility(View.VISIBLE);
                mLoadingViewHolder.progressBar.setVisibility(View.GONE);
                mLoadingViewHolder.tvLoading.setText("加载失败，点击重新加载！");

                mLoadingViewHolder.tvLoading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loadMoreListener != null) {
                            mLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                            mLoadingViewHolder.tvLoading.setText("正在加载...");

                            loadMoreListener.loadMore();
                        }
                    }
                });
                break;
            case STATE_NO_MORE:
                mLoadingViewHolder.llyLoading.setVisibility(View.VISIBLE);
                mLoadingViewHolder.progressBar.setVisibility(View.GONE);
                mLoadingViewHolder.tvLoading.setText("数据已全部加载完！");
                break;
            case STATE_LOAD_MORE:
                mLoadingViewHolder.llyLoading.setVisibility(View.VISIBLE);
                mLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                mLoadingViewHolder.tvLoading.setText("正在加载...");
                break;
            case STATE_NO_DATA:
                mLoadingViewHolder.llyLoading.setVisibility(View.GONE);
                break;
            default:
                mLoadingViewHolder.llyLoading.setVisibility(View.GONE);
                break;
        }
    }

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

    public interface LoadMoreListener {
        void loadMore();

    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    private boolean canScrollDown(RecyclerView recyclerView) {
        return ViewCompat.canScrollVertically(recyclerView, 1);
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

                if (!canScrollDown(recyclerView) && state == STATE_LOAD_MORE) {
                    LogTool.d(this.getClass().getName(), "loading...");
                    if (loadMoreListener != null) {
                        loadMoreListener.loadMore();
                    }
                }
            }
        });
    }
}
