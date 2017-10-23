package org.atlaslabs.speedrun.util;

import android.os.Handler;
import android.os.Looper;

public class Utils {
    public static void runOnUIThread(Runnable r){
        new Handler(Looper.getMainLooper()).post(() -> r.run());
    }
}
