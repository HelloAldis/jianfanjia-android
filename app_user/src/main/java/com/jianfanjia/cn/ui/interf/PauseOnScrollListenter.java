package com.jianfanjia.cn.ui.interf;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Description: com.jianfanjia.cn.ui.interf
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-28 09:27
 */
public class PauseOnScrollListenter extends RecyclerView.OnScrollListener {

    private ImageLoader imageLoader;

    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;
    private final RecyclerView.OnScrollListener externalListener;

    /**
     * Constructor
     *
     * @param imageLoader   {@linkplain ImageLoader} instance for controlling
     * @param pauseOnScroll Whether {@linkplain ImageLoader#pause() pause ImageLoader} during touch scrolling
     * @param pauseOnFling  Whether {@linkplain ImageLoader#pause() pause ImageLoader} during fling
     */
    public PauseOnScrollListenter(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    /**
     * Constructor
     *
     * @param imageLoader    {@linkplain ImageLoader} instance for controlling
     * @param pauseOnScroll  Whether {@linkplain ImageLoader#pause() pause ImageLoader} during touch scrolling
     * @param pauseOnFling   Whether {@linkplain ImageLoader#pause() pause ImageLoader} during fling
     * @param customListener Your custom {@link OnScrollListener} for {@linkplain AbsListView list view} which also
     *                       will be get scroll events
     */
    public PauseOnScrollListenter(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling,
                                 RecyclerView.OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        externalListener = customListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                if (pauseOnFling) {
                    imageLoader.pause();
                }
                break;
        }
        if (externalListener != null) {
            externalListener.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(externalListener != null){
            externalListener.onScrolled(recyclerView,dx,dy);
        }

    }

}
