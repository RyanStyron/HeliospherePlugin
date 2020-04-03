package mc.rysty.heliosphereplugin.utils;

public class MathUtils {

    public static int getLargestInt(int... numbers) {
        Integer largest = null;

        for (int number : numbers)
            if (largest == null || number > largest)
                largest = number;

        return largest;
    }

    // I don't remember what this was for
    public static String formatBytes(long bytes) {
        String s = (bytes < 0L) ? "-" : "";
        long b = (bytes == Long.MIN_VALUE) ? Long.MAX_VALUE : Math.abs(bytes);
        return (b < 1000L) ? (bytes + " B") : ((b < 999950L) ?
                String.format("%s%.1f kB", new Object[] { s, Double.valueOf(b / 1000.0D) }) : (((b /= 1000L) < 999950L) ?
                String.format("%s%.1f MB", new Object[] { s, Double.valueOf(b / 1000.0D) }) : (((b /= 1000L) < 999950L) ?
                String.format("%s%.1f GB", new Object[] { s, Double.valueOf(b / 1000.0D) }) : (((b /= 1000L) < 999950L) ?
                String.format("%s%.1f TB", new Object[] { s, Double.valueOf(b / 1000.0D) }) : (((b /= 1000L) < 999950L) ?
                String.format("%s%.1f PB", new Object[] { s, Double.valueOf(b / 1000.0D) }) : String.format("%s%.1f EB", new Object[] { s, Double.valueOf(b / 1000000.0D) }))))));
    }
}