package com.bt.bbcp;

public class StringReaderService implements ReaderService {

    String metricsString;

    public StringReaderService(String metricsString){
        this.metricsString = metricsString;
    }


    @Override
    public String getData() {
        return metricsString;
    }
}
