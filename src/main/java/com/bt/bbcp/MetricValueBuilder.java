package com.bt.bbcp;

import java.math.BigInteger;

public class MetricValueBuilder {

    private Number result;

    public MetricValueBuilder(String value) {
        result = makeNumberValue(value);
        if (result == null) {
            result = makeDoubleValue(value);
        } else if (result instanceof BigInteger){
            if (((BigInteger) result).compareTo(new BigInteger("-1")) == 0 ) {
                result = null;
            }
        }
    }

    private Number makeNumberValue(String value) {
        Number result;
        try {
            result = Long.valueOf(value);
        } catch (NumberFormatException e) {
            result = makeBigIntValue(value);
        }
        return result;
    }

    private BigInteger makeBigIntValue(String value) {
        BigInteger result;
        try {
            result = new BigInteger(value);
            if (result.compareTo(new BigInteger("0")) < 0) {
                result = new BigInteger("-1");
            }
        } catch (NumberFormatException e) {
            result = null;
        }
        return result;
    }

    private Double makeDoubleValue(String value) {
        Double result;

        try {
            result = makeDouble(value);
        } catch (Exception e) {
            switch (value.toLowerCase()) {
                case "inf":
                case "infinity":
                case "+inf":
                case "+infinity":
                    result = Double.POSITIVE_INFINITY;
                    break;

                case "-infinity":
                case "-inf":
                    result = Double.NEGATIVE_INFINITY;
                    break;

                case "nan":
                    result = Double.NaN;
                    break;

                default:
                    result = null;
            }
        }
        return result;
    }

    private static Double makeDouble(String value){
        return Double.valueOf(value);
    }

    public Number getResult() {
        return result;
    }
}
