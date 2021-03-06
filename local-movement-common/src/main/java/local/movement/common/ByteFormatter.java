package local.movement.common;

public class ByteFormatter {

    public static String format(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String length(long bytes) {
        return format(bytes, false);
    }

    public static String speedInSecond(long sizeInSecond) {
        return length(sizeInSecond) + "/s";
    }

}
