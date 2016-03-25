/*
 *    Copyright 2015 Kaopiz Software Co., Ltd.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.aldis.hud;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Aldis on 16/3/25.
 */
class SpinnerView extends ImageView {

    private static final int FPS = 60;
    private static final int DEGREES_PER_FRAME = 30;

    private float rotateDegrees;
    private boolean needToUpdateView;
    private Runnable updateViewRunnable;


    public SpinnerView(Context context) {
        super(context);
        init();
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setImageResource(R.drawable.hud);
        updateViewRunnable = new Runnable() {
            @Override
            public void run() {
                rotateDegrees += DEGREES_PER_FRAME;
                rotateDegrees = rotateDegrees < 360 ? rotateDegrees : rotateDegrees - 360;
                invalidate();
                if (needToUpdateView) {
                    postDelayed(this, FPS);
                }
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(rotateDegrees, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        needToUpdateView = true;
        post(updateViewRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        needToUpdateView = false;
        super.onDetachedFromWindow();
    }
}
