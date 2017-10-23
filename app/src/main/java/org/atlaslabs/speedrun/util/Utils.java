package org.atlaslabs.speedrun.util;

import android.os.Handler;
import android.os.Looper;

public class Utils {
    public static void runOnUIThread(Runnable r){
        new Handler(Looper.getMainLooper()).post(() -> r.run());
    }

    /**
     * @param time number of seconds as a int/float, optionally including ms
     * @return pretty version of time, for example: 121.1 returns 2:01.1
     */
    public static String timePretty(float time){
        StringBuilder builder = new StringBuilder();
        int hours = (int)time / 3600;
        int minutes = ((int)time/60) % 60;
        int seconds = ((int)time) % 60;
        int ms = ((int)time * 1000) % 1000;
        if(hours > 0) {
            builder.append(hours);
            builder.append(":");
            if(minutes < 10)
                builder.append(0);
        }
        builder.append(minutes);
        builder.append(":");
        if(seconds < 10)
            builder.append("0");
        builder.append(seconds);
        if(ms > 0){
            builder.append(".");
            builder.append(ms);
        }
        return builder.toString();
    }
}
