package seng4430.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class CollectionHelper {
    public static Double calculateIntegerAverage(Collection<Integer> list) {
        var total = sumIntegerValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

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
        var total = sumDoubleValues(list);
        if (total == 0) return 0.0;

        return ((double)total) / list.size();
    }

    public static Double sumDoubleValues(Collection<Double> list) {
        return list.stream().reduce(0.0, Double::sum);
    }
}
