package com.bt.bbcp;

public class MetricsBuilder {

    public static BasicGraphiteFormat makeBasicGraphiteFormat(String[] fields, MetricState metricState){
        BasicGraphiteFormat result = null;
        metricState.setStatus(Status.OK);
        try{
            if (isNameFormatOk(fields[0])) {
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

    private static boolean isNameFormatOk(String name){
        String regEx = "((^[a-zA-Z][a-zA-Z0-9_+%-]+)((\\.[a-zA-Z0-9_+%-]+)*|$)$)";
        if (!name.matches(regEx)){
            return false;
        }
        return true;
    }

}
