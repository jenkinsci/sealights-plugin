package io.sealights.plugins.sealightsjenkins.utils;


/**
 * Created by Ronis on 6/4/2017.
 */
public class TimeUtils {

    public static String toStringDuration(long duration) {
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        long hours = (duration / (1000 * 60 * 60));
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
