package seng4430.metricProviders;

import java.util.HashMap;

public class Metrics {
    public static HashMap<String, MetricProvider> metricProviders = new HashMap<>() {
        {
            put("loi", new LengthOfIdentifiersMetricProvider());
            put("comments", new CommentsMetricProvider());
            put("dit", new DITMetricProvider());
            put("fanin", new FanInMetricProvider());
            put("fanout", new FanOutMetricProvider());
            put("lcom", new LCOMMetricProvider());
            put("cc", new CyclomaticComplexityProvider());
            put("docn", new DepthOfConditionalNestingProvider());
            put("wmc", new WeightedMethodsPerClassMetricProvider());
        }
    };
}
