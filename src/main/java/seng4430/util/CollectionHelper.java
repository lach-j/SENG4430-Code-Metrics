package seng4430.util;

import java.util.Collection;

public class CollectionHelper {
    public static Double calculateAverage(Collection<Integer> list) {
        var total = sumValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

    public static Integer sumValues(Collection<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }
}
