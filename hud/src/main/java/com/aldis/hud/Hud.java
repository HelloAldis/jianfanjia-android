package com.aldis.hud;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Aldis on 16/3/25.
 */
public class Hud {
    public static final class Style {

        // dim amount of dialog
        public static float dimAmount = 0;

        // background color of dialog,
        public static int backgroundColor = Color.argb(0x33, 0, 0, 0);

        // corner radius of dialog, default is 10
        public static int cornerRadius = 10;

        // can cancel by back or not, default is can not
        public static boolean cancelable = false;
    }

    private static HudDialog hudDialog = null;

    private static ShowDialogRunnable sShowDialogRunnable;

    private static Handler sHandler = new Handler();


    public static void showDelay(final String message, long delay, final Context context) {

        Log.d(Hud.class.getName(), "showDelay");

        sShowDialogRunnable = new ShowDialogRunnable();
        sShowDialogRunnable.setContext(context);
        sShowDialogRunnable.setMessage(message);

        sHandler.postDelayed(sShowDialogRunnable, delay);
    }

    public static void show(Context context) {
        show(null, context);
    }

    public static void show(String message, Context context) {
        if (Hud.hudDialog != null && Hud.hudDialog.isShowing()) {
            //Only one HUD dialog can be show at same time
            return;
        }

        Hud.hudDialog = new HudDialog(context);
        Hud.hudDialog.setCancelable(Style.cancelable);
        Hud.hudDialog.setLabel(message);
        Hud.hudDialog.show();
    }

    public static void dismiss() {
      /*  if (sShowDialogRunnable != null) {
            Log.d(Hud.class.getName(), "sShowDialogRunnable != null");
            sHandler.removeCallbacks(sShowDialogRunnable);
            sShowDialogRunnable = null;
        }*/
        if (Hud.hudDialog != null && Hud.hudDialog.isShowing()) {
            Hud.hudDialog.dismiss();
            Hud.hudDialog = null;
        }
    }

    private static class ShowDialogRunnable implements Runnable {

        private String message;
        private Context context;

        public void setMessage(String message) {
            this.message = message;
        }


        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            if (Hud.hudDialog != null && Hud.hudDialog.isShowing()) {
                //Only one HUD dialog can be show at same time
                return;
            }

            Hud.hudDialog = new HudDialog(context);
            Hud.hudDialog.setCancelable(Style.cancelable);
            Hud.hudDialog.setLabel(message);
            Hud.hudDialog.show();
        }
    }

}
