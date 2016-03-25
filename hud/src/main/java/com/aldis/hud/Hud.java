package com.aldis.hud;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by Aldis on 16/3/25.
 */
public class Hud {
    public static final class Style {

        // dim amount of dialog
        public static float dimAmount = 0;

        // background color of dialog,
        public static int backgroundColor = Color.argb(0xb1, 0, 0 ,0);

        // corner radius of dialog, default is 10
        public static int cornerRadius = 10;

        // can cancel by back or not, default is can not
        public static boolean cancelable = false;
    }

    private static HudDialog hudDialog = null;

    public static synchronized void show(Context context) {
        show(null, context);
    }

    public static synchronized void show(String message, Context context) {
        if (Hud.hudDialog != null && Hud.hudDialog.isShowing()) {
            //Only one HUD dialog can be show at same time
            return;
        }

        Hud.hudDialog = new HudDialog(context);
        Hud.hudDialog.setCancelable(Style.cancelable);
        Hud.hudDialog.setLabel(message);
        Hud.hudDialog.show();
    }

    public static synchronized void dismiss() {
        if (Hud.hudDialog != null && Hud.hudDialog.isShowing()) {
            Hud.hudDialog.dismiss();
            Hud.hudDialog = null;
        }
    }
}
