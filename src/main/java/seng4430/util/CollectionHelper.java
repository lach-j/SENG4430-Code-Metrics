package seng4430.util;

import java.util.Collection;
import java.util.Comparator;

/**
 * A utility class providing helper methods for collections.
 */
public class CollectionHelper {
    /**
     * Calculates the average of all {@link Integer}s within a {@link Collection}.
     *
     * @param list The {@link Collection} of {@link Integer}s to be averaged.
     * @return The average of all {@link Integer}s within the {@link Collection}.
     */
    public static Double calculateIntegerAverage(Collection<Integer> list) {
        Integer total = sumIntegerValues(list);
        if (total == 0) return 0.0;

        return ((double) total) / list.size();
    }

    /**
     * @param list The {@link Collection} of {@link Integer}s to be summed.
     * @return The sum of all {@link Integer}s.
     */
    public static Integer sumIntegerValues(Collection<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    public static int calculateMinInteger(Collection<Integer> values) { //calculate minimum
        return values.stream().min(Comparator.naturalOrder()).orElse(0);
    }

    public static int calculateMaxInteger(Collection<Integer> values) { //calculate maximum
        return values.stream().max(Comparator.naturalOrder()).orElse(0);
    }

    public static Double calculateDoubleAverage(Collection<Double> list) {
        Double total = sumDoubleValues(list);
        if (total == 0) return 0.0;

        return total / list.size();
    }

    public static Double sumDoubleValues(Collection<Double> list) {
        return list.stream().reduce(0.0, Double::sum);
    }
}
