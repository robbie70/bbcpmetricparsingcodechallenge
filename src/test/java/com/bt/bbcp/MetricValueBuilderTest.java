package com.bt.bbcp;

import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

public class MetricValueBuilderTest {

    @Test
    public void shouldFailBigUnsigned(){
        String inputString = "-9223372036854775809";
        MetricValueBuilder metricValueBuilder = new MetricValueBuilder(inputString);

        assertNull(metricValueBuilder.getResult());
    }

    @Test
    public void shouldCreateSignedLong() {
        MetricValueBuilder metricValueBuilder = new MetricValueBuilder("-1");
        assertTrue(metricValueBuilder.getResult() instanceof Long);

        metricValueBuilder = new MetricValueBuilder("-10000000");
        assertTrue(metricValueBuilder.getResult() instanceof Long);

        metricValueBuilder = new MetricValueBuilder("10000000");
        assertTrue(metricValueBuilder.getResult() instanceof Long);

        metricValueBuilder = new MetricValueBuilder(String.valueOf(Long.MAX_VALUE));
        assertTrue(metricValueBuilder.getResult() instanceof Long);

        metricValueBuilder = new MetricValueBuilder(String.valueOf(Long.MIN_VALUE));
        assertTrue(metricValueBuilder.getResult() instanceof Long);
    }

    @Test
    public void shouldCreateBigInteger(){
        String inputString = "9223372036854775808";
        MetricValueBuilder metricValueBuilder = new MetricValueBuilder(inputString);

        assertTrue(metricValueBuilder.getResult() instanceof BigInteger);
        assertTrue(metricValueBuilder.getResult().toString().equals(inputString));

    }
}
