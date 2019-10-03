package com.bt.bbcp;

public class MetricsBuilder {

    public static BasicGraphiteFormat makeBasicGraphiteFormat(String[] fields, MetricState metricState){
        BasicGraphiteFormat result = null;
        metricState.setStatus(Status.OK);
        try{
            if (isNameFormatOk(fields[0], metricState)) {
                Number value = makeValue(fields[1], metricState);
                long milliSecondsSinceEpoch = makeMilliSecondsSinceEpoch(fields[2], metricState);
                result = new BasicGraphiteFormat(fields[0], value, milliSecondsSinceEpoch);
            }
        }catch(Exception e){
            return null;
        }
        return result;
    }

    public static BasicGraphiteFormat makeTaggedGraphiteFormat(String[] fields, MetricState metricState){
        BasicGraphiteFormat result = null;
        metricState.setStatus(Status.OK);
        try{
            if (isTaggedNameFormatOk(fields[0], metricState)) {
                Number value = makeValue(fields[1], metricState);
                long milliSecondsSinceEpoch = makeMilliSecondsSinceEpoch(fields[2], metricState);
                result = new BasicGraphiteFormat(fields[0], value, milliSecondsSinceEpoch);
            }
        }catch(Exception e){
            return null;
        }
        return result;
    }

    private static long makeMilliSecondsSinceEpoch(String milliSecondsSinceEpoch, MetricState metricState) {
        Long millis = 0L;
        try {
            String clean = milliSecondsSinceEpoch.replace('\r', '\n');
            clean = clean.replaceAll("\n", " ").trim();
            millis = Long.parseUnsignedLong(clean);
        } catch (Exception e) {
            millis = null;
            metricState.makeState("Invalid timestamp:" + milliSecondsSinceEpoch, Status.INVALID);
        }
        return millis;
    }

    private static Number makeValue(String value, MetricState metricState) {

        MetricValueBuilder metricValueBuilder = new MetricValueBuilder(value);
        Number result = metricValueBuilder.getResult();
        if (result == null) {
            metricState.makeState("Invalid value:" + value, Status.INVALID);
        }

        return result;
    }

    private static boolean isNameFormatOk(String name, MetricState metricState){
        String regEx = "((^[a-zA-Z][a-zA-Z0-9_+%-]+)((\\.[a-zA-Z0-9_+%-]+)*|$)$)";
        if (!name.matches(regEx)) {
            metricState.makeState("Invalid name:" + name, Status.INVALID);
            return false;
        }
        return true;
    }

    private static boolean isTaggedNameFormatOk(String name, MetricState metricState) {
        String[] fields = splitMetricIntoTags(name, metricState);

        String regEx = "((^[a-zA-Z][a-zA-Z0-9_+%-]+)((\\.[a-zA-Z0-9_+%-]+)*|$)$)";
        if (!name.matches(regEx)) {
            metricState.makeState("Invalid name:" + name, Status.INVALID);
            return false;
        }
        return true;
    }

    private static String[] splitMetricIntoTags(String metricName, MetricState metricState) {
        String[] fields = metricName.split(";");
        if (isNameFormatOk(fields[0], metricState)) {
            //TODO add tag validation
        }
        return fields;
    }
}
