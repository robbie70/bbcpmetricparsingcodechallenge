package com.bt.bbcp;

import org.junit.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MetricsParserTest {

    @Test
    public void shouldParseDouble(){
        String metricStringToTest = "another.metric.value.cpu% 1.23 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        assertEquals(1, metrics.size());
        undertest.printMetrics();
    }

    @Test
    public void shouldRejectMetricForIncorrectNumberOfFields(){
        String metricStringToTest = "some.metric.name 156276319\n";
                //"another.metric.value.cpu% 1.23e-1 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        assertEquals(0, metrics.size());
    }

    @Test
    public void shouldReadEachLineOfMetrics(){
        ReaderService readerService = new FileReaderService("metrics-file.log");
        MetricsParser undertest = new MetricsParser(readerService);
        String[] lines = undertest.getMetricsInput().split(System.getProperty("line.separator"));
        assertEquals(2, lines.length);
    }

    @Test
    public void shouldReadMetricsFile() throws IOException {
        String metricStringToTest = "some.metric.name 1234 156276319\n" +
                "another.metric.value.cpu% 1.23e-1 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        System.out.println(undertest.toString());
        assertEquals(true, undertest.isFileLoaded());
    }



}
