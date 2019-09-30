package com.bt.bbcp;

import java.util.ArrayList;
import java.util.List;

public class MetricsParser {

    private String metricsInput;
    private boolean loaded;
    private List<Metric> metrics = new ArrayList<>();

    public MetricsParser(ReaderService readerService) {
        metricsInput = readerService.getData();
        loaded = true;
    }

    public void parse() {
        String[] lines = getMetricsInput().split("\n");
        for(String line : lines) {
            String[] fields = splitLineIntoFields(line);
            if (fields.length != 3) {
                System.out.println("Invalid Metric found - incorrect number of fields - rejecting. <" + line + ">");
            }else {
                Metric metric = MetricsBuilder.makeBasicGraphiteFormat(fields);
                if (metric != null) {
                    metrics.add(metric);
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

    @Override
    public String toString() {
        return "MetricsParser{" +
                "metrics='\n" + metricsInput +
                '}';
    }
}
