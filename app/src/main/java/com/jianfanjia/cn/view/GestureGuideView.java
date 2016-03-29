package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.ImageUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.TDevice;

/**
 * Description: com.jianfanjia.cn.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-24 18:20
 */
public class GestureGuideView extends View {

    private Paint paint;
    private float cx, cy, radius;
    private OnClickListener onClickListener;

    private GestureDetector gestureDetector;

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float lastY = e1.getY();
            float currentY = e2.getY();
            if (distanceY > distanceX && lastY - currentY > 200) {
                if (onClickListener != null) {
                    onClickListener.onClick(GestureGuideView.this);
                    return false;
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (velocityY < -200 && Math.abs(velocityY) > Math.abs(velocityX)) {
                if (onClickListener != null) {
                    onClickListener.onClick(GestureGuideView.this);
                    return false;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getRawX();
            float y = e.getRawY();
            if (x >= cx - radius && x <= cx + radius && y >= cy - radius && y <= cy + radius) {
                if (onClickListener != null) {
                    onClickListener.onClick(GestureGuideView.this);
                }
            }
            return super.onSingleTapUp(e);
        }

    };

    public void setOnClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    public GestureGuideView(Context context) {
        this(context, null);
        gestureDetector = new GestureDetector(context, simpleOnGestureListener);
    }

    public GestureGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    private Bitmap loadBitmap() {
        Bitmap bitmap = ImageUtil.drawableResToBitmap(getContext(), R.mipmap.home_guide_gesture);
        return bitmap;
    }

    public void setCicrePosition(float x, float y, float radius) {
        this.cx = x + radius;
        this.cy = y + radius;
        this.radius = radius;

        LogTool.d(this.getClass().getName(), "cx =" + cx + " cy" + cy);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(false);
        paint.setColor(getResources().getColor(R.color.transparent_bg));
        paint.setAlpha(144);
        canvas.drawRect(0, 0, TDevice.getScreenWidth(), TDevice.getScreenHeight(), paint);
        paint.setAlpha(255);
        Bitmap bitmap = loadBitmap();
        canvas.drawBitmap(bitmap, (TDevice.getScreenWidth() - bitmap.getWidth()) / 2, (TDevice.getScreenHeight() - bitmap.getHeight()) / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(getResources().getColor(R.color.transparent_bg));
        canvas.drawCircle(cx, cy, radius, paint);
        paint.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

}
