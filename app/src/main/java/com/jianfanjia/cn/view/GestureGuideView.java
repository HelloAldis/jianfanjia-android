package com.jianfanjia.cn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.tools.TDevice;

/**
 * Description: com.jianfanjia.cn.view
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-24 18:20
 */
public class GestureGuideView extends View {

    private Paint paint;
    private int bitmapWidth;
    private int bitmapHeight;

    public GestureGuideView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(getResources().getColor(R.color.black_translucencen));
    }

    public GestureGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(getResources().getColor(R.color.black_translucencen));
    }

    private Bitmap loadBitmap(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.home_guide_gesture);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, TDevice.getScreenWidth(), TDevice.getScreenHeight(), paint);
        Bitmap bitmap = loadBitmap();
        canvas.drawBitmap(bitmap,(TDevice.getScreenWidth() - bitmap.getWidth())/2 , (TDevice.getScreenHeight() - bitmap.getHeight())/2 ,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(getResources().getColor(R.color.transparent_bg));
        canvas.drawCircle(200, 200, 100, paint);
        paint.reset();
    }
}
