package com.bt.bbcp;

public class MetricsBuilder {

    public static BasicGraphiteFormat makeBasicGraphiteFormat(String [] fields){
        BasicGraphiteFormat result = null;
        try{
            if (isNameFormatOk(fields[0])) {
                Number value = makeValue(fields[1]);
                long milliSecondsSinceEpoch = makeMilliSecondsSinceEpoch(fields[2]);
                result = new BasicGraphiteFormat(fields[0], value, milliSecondsSinceEpoch);
            }
        }catch(Exception e){
            return null;
        }
        return result;
    }

    private static long makeMilliSecondsSinceEpoch(String milliSecondsSinceEpoch) {
        String clean = milliSecondsSinceEpoch.replace('\r', '\n');
        clean = milliSecondsSinceEpoch.replaceAll("\n", " ").trim();
        return Long.valueOf(clean);
    }

    private static Number makeValue(String value) {
        Number result;
        result = makeDouble(value);
        //return Double.valueOf(value);
        return result;
    }

    private static Double makeDouble(String value){
        return Double.valueOf(value);
    }

    private static boolean isNameFormatOk(String name){
        String regEx = "((^[a-zA-Z][a-zA-Z0-9_+%-]+)((\\.[a-zA-Z0-9_+%-]+)*|$)$)";
        if (!name.matches(regEx)){
            return false;
        }
        return true;
    }

}
