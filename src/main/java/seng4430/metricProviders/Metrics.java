package seng4430.metricProviders;

import java.util.HashMap;

/**
 * Provides the set of all current metrics.
 * The Metrics class contains a static HashMap called metricProviders, which stores instances of various MetricProvider
 * classes representing different metrics. The keys in the map are strings representing the metric names, and the values
 * are the corresponding MetricProvider instances.
 * <p>
 * This class serves as a centralized location to access all the available metric providers. By adding new entries to the
 * metricProviders map, new metrics can be easily incorporated into the system.
 *
 * @author Lachlan
 */
public class Metrics {
    /**
     * A HashMap that stores instances of MetricProvider classes.
     * The keys are strings representing the metric names, and the values are the corresponding MetricProvider instances.
     * The metricProviders map is initialized with the current set of metrics by instantiating the respective
     * MetricProvider classes and adding them to the map.
     */
    public static final HashMap<String, MetricProvider> metricProviders = new HashMap<>() {
        {
            put("loi", new LengthOfIdentifiersMetricProvider());
            put("comments", new CommentsMetricProvider());
            put("dit", new DITMetricProvider());
            put("fanin", new FanInMetricProvider());
            put("fanout", new FanOutMetricProvider());
            put("lcom", new LCOMMetricProvider());
            put("cc", new CyclomaticComplexityProvider());
            put("docn", new DepthOfConditionalNestingProvider());
            put("noc", new NumberOfChildrenMetricProvider());
            put("wmc", new WeightedMethodsPerClassMetricProvider());
            put("rfc", new ResponseForClassProvider());
            put("cbo", new CouplingBetweenObjectClassesMetricProvider());
        }
    };
}
