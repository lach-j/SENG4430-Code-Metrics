package seng4430.util;

import java.util.Collection;

public class CollectionHelper {
    public static Double calculateIntegerAverage(Collection<Integer> list) {
        var total = sumIntegerValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

    public static Integer sumIntegerValues(Collection<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    public static Double calculateDoubleAverage(Collection<Double> list) {
        var total = sumDoubleValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

    public static Double sumDoubleValues(Collection<Double> list) {
        return list.stream().reduce(0.0, Double::sum);
    }
}
