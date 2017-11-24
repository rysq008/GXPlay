package com.game.helper.utils;

import android.app.Dialog;
import android.view.WindowManager;

/**
 * author: ${User} date: ${Date}.
 */
public class DialogUtils {


    public static void safeShowDialog(Dialog dialog) {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (WindowManager.BadTokenException e) {

        }
    }

    public static void safeDismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (WindowManager.BadTokenException e) {

            }
        }
    }
}
