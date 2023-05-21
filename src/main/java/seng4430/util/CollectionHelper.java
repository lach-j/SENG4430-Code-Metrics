package seng4430.util;

import java.util.Collection;

/**
 * A utility class providing helper methods for collections.
 */
public class CollectionHelper {
    /**
     * Calculates the average of all {@link Integer}s within a {@link Collection}.
     * @param list The {@link Collection} of {@link Integer}s to be averaged.
     * @return The average of all {@link Integer}s within the {@link Collection}.
     */
    public static Double calculateAverage(Collection<Integer> list) {
        var total = sumValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

    /**
     * @param list The {@link Collection} of {@link Integer}s to be summed.
     * @return The sum of all {@link Integer}s.
     */
    public static Integer sumValues(Collection<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }
}
