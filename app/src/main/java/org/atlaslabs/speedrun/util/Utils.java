package org.atlaslabs.speedrun.util;

import android.os.Handler;
import android.os.Looper;

import org.atlaslabs.speedrun.models.Link;

import java.util.Iterator;
import java.util.List;

public class Utils {
    public static void runOnUIThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(() -> r.run());
    }

    /**
     * @param time number of seconds as a int/float, optionally including ms
     * @return pretty version of time, for example: 121.1 returns 2:01.1
     */
    public static String timePretty(float time) {
        StringBuilder builder = new StringBuilder();
        int hours = (int) time / 3600;
        int minutes = ((int) time / 60) % 60;
        int seconds = ((int) time) % 60;
        int ms = ((int) time * 1000) % 1000;
        if (hours > 0) {
            builder.append(hours);
            builder.append(":");
            if (minutes < 10)
                builder.append(0);
        }
        builder.append(minutes);
        builder.append(":");
        if (seconds < 10)
            builder.append("0");
        builder.append(seconds);
        if (ms > 0) {
            builder.append(".");
            builder.append(ms);
        }
        return builder.toString();
    }

    public static String buildVideoLinks(List<Link> links) {
        StringBuilder linksHTMLBuilder = new StringBuilder();
        for (Link link : links) {
            linksHTMLBuilder.append("<a href=\"");
            linksHTMLBuilder.append(link.getUri());
            linksHTMLBuilder.append("\">");
            linksHTMLBuilder.append(link.getUri());
            linksHTMLBuilder.append("</a><br />");
        }
        return linksHTMLBuilder.toString();
    }

    /**
     * @param iter      iterator through which we will generate data
     * @param separator string to inject between each item
     * @return String with each item concatenated, with separator between each concatenation
     */
    public static String join(Iterator<String> iter, String separator) {
        StringBuilder sb = new StringBuilder();
        if (iter.hasNext()) {
            sb.append(iter.next());
            while (iter.hasNext())
                sb.append(separator).append(iter.next());
        }
        return sb.toString();
    }
}
