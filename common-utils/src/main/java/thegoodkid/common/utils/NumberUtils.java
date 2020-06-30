package thegoodkid.common.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class NumberUtils {
    /**
     * Turns an {@link ArrayList} of integers into an array of integers
     * In places of null elements, the returned array has 0
     *
     * @param integers The {@link ArrayList} of integers that will be converted
     * @return An array of integers from the given {@link ArrayList}
     */
    @NonNull
    public static int[] toArray(@NonNull ArrayList<Integer> integers) {
        int[] ints = new int[integers.size()];

        int size = integers.size();
        for (int i = 0; i < size; i++) {
            Integer x = integers.get(i);
            if (x == null) continue;

            ints[i] = x;
        }

        return ints;
    }

    @NonNull
    public static int[] toArrayRemoveNull(@NonNull ArrayList<Integer> integers) {
        integers.removeAll(Collections.singleton(null));
        return toArray(integers);
    }

    /**
     * Removes any unwanted characters from the string and parses it
     * into a double wrapper object.
     * If the the string contains multiple points (.), only
     * the last one stays
     * <p>
     * This can be useful specially in places like query
     *
     * @param str      The string from which numbers are to be extracted
     * @param fallback The fallback object to be returned if the string
     *                 can't be parsed into a double wrapper object
     * @return A Double Object from the given string
     */
    @Nullable
    public static Double extractNumbers(@NonNull String str, @Nullable Double fallback) {
        StringBuilder builder = new StringBuilder(str);

        for (int latestDotIndex = builder.indexOf("."), lastDotIndex = builder.lastIndexOf(".");
             latestDotIndex != lastDotIndex;
             latestDotIndex = builder.indexOf("."), lastDotIndex--) {

            builder.deleteCharAt(latestDotIndex);
        }

        int length = builder.length();
        for (int i = 0; i < length; i++) {
            char c = builder.charAt(i);
            if (Character.isDigit(c) || c == '.') continue;

            builder.deleteCharAt(i);
            i--;
            length--;
        }

        if (builder.length() == 1 && builder.charAt(0) == '.') return fallback;
        if (builder.length() > 0) return Double.parseDouble(builder.toString());
        else return fallback;
    }
}
