package com.bt.bbcp;

public abstract class Metric {

    private String name;
    private Number value;

    private long milliSecondsSinceEpoch;

    public Metric(String name, Number value, long milliSecondsSinceEpoch) {
        this.name = name;
        this.value = value;
        this.milliSecondsSinceEpoch = milliSecondsSinceEpoch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public long getMilliSecondsSinceEpoch() {
        return milliSecondsSinceEpoch;
    }

    public void setMilliSecondsSinceEpoch(int milliSecondsSinceEpoch) {
        this.milliSecondsSinceEpoch = milliSecondsSinceEpoch;
    }
//    public long getSecondsSinceEpoch() {
//        return milliSecondsSinceEpoch / 1000;
//    }
//
//    public void setSecondsSinceEpoch(int SecondsSinceEpoch) {
//        this.milliSecondsSinceEpoch = SecondsSinceEpoch * 1000;
//    }

    @Override
    public String toString() {
        return "Metric{" +
                "name='" + name + '\'' +
                ", result=" + value +
                ", milliSecondsSinceEpoch=" + milliSecondsSinceEpoch +
                '}';
    }
}
