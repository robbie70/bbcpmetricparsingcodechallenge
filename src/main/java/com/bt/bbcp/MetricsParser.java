package com.bt.bbcp;

import java.util.ArrayList;
import java.util.List;

public class MetricsParser {

    private String metricsInput;
    private boolean loaded;
    private List<Metric> metrics = new ArrayList<>();
    private long invalidMetrics;
    private long validMetrics;


    public MetricsParser(ReaderService readerService) {
        metricsInput = readerService.getData();
        loaded = true;
    }

    public void parse() {
        MetricState metricState = new MetricState();

        String[] lines = getMetricsInput().split("\n");
        for(String line : lines) {
            String[] fields = splitLineIntoFields(line);
            if (fields.length != 3) {
                metricState.makeState("Invalid Metric found - incorrect number of fields - rejecting. <" + line + ">", Status.INVALID);
            }else {
                Metric metric = MetricsBuilder.makeBasicGraphiteFormat(fields, metricState);
                if (metricState.getStatus() == Status.OK) {
                    validMetrics ++;
                    metrics.add(metric);
                } else {
                    invalidMetrics ++;
                    System.out.println("metric " + line + " is invalid " + metricState.getMessage());
                }
            }
        }
    }

    private String[] splitLineIntoFields(String line) {
        String[] fields = new String[3];
        fields = line.split(" ");
        return fields;
    }



    public List<Metric> getMetrics(){
        return metrics;
    }

    public boolean isFileLoaded() {
        return loaded;
    }

    public String getMetricsInput(){
        return metricsInput;
    }

    public void printMetrics(){
        for (Metric metric : metrics){
            System.out.println(metric.toString());
        }
    }

    public long getInvalidMetrics() {
        return invalidMetrics;
    }

    public long getValidMetrics() {
        return validMetrics;
    }

    @Override
    public String toString() {
        return "MetricsParser{" +
                "metrics='\n" + metricsInput +
                '}';
    }
}
