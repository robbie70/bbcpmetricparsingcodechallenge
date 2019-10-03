package com.bt.bbcp;

import com.sun.javafx.font.Metrics;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MetricsParserTest {

    @Test
    public void shouldParseSignedLong(){
        String metricStringToTest = "another.metric.result.cpu% 0 1562763195000\n" +
                "another.metric.result.cpu% 1.09 1562763195000\n" +
                "another.metric.result.cpu% " + Long.MIN_VALUE + " 1562763195000\n" +
                "another.metric.result.cpu% " + Long.MAX_VALUE + " 1562763195000\n" +
                "another.metric.result.cpu% -1 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(5, 5, 0, metrics, undertest);
        undertest.printMetrics();
    }





    @Test
    public void shouldParseNotANumber(){
        String metricStringToTest = "another.metric.result.cpu% Nan 1562763195000\n" +
                "another.metric.result.cpu% NAN 1562763195000\n" +
                "another.metric.result.cpu% nan 1562763195000\n" +
                "another.metric.result.cpu% non 1562763195000\n" +
                "another.metric.result.cpu% NANNY 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(3, 3, 2, metrics, undertest);
        undertest.printMetrics();
    }

    @Test
    public void shouldParseInfinity(){
        String metricStringToTest = "another.metric.result.cpu% +inf 1562763195000\n" +
                "another.metric.result.cpu% inf 1562763195000\n" +
                "another.metric.result.cpu% -inf 1562763195000\n" +
                "another.metric.result.cpu% -infaty 1562763195000\n" +
                "another.metric.result.cpu% -i 1562763195000\n" +
                "another.metric.result.cpu% +i 1562763195000\n" +
                "another.metric.result.cpu% infinity 1562763195000\n" +
                "another.metric.result.cpu% +infinity 1562763195000\n" +
                "another.metric.result.cpu% -infinity 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(6, 6, 3, metrics, undertest);
        undertest.printMetrics();
    }

    @Test
    public void shouldParseExponent(){
        String metricStringToTest = "another.metric.result.cpu% 1.23e-1 1562763195000\n" +
                "another.metric.result.cpu% 1+12 1562763195000\n" +
                "another.metric.result.cpu% 1E+12 1562763195000\n" +
                "another.metric.result.cpu% 1.65e4 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(3, 3, 1, metrics, undertest);
        undertest.printMetrics();
    }

    @Test
    public void shouldParseDouble(){
        String metricStringToTest = "another.metric.result.cpu% 1.23 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(1, 1, 0, metrics, undertest);
        undertest.printMetrics();
    }

    @Test
    public void shouldRejectMetricForIncorrectNumberOfFields(){
        String metricStringToTest = "some.metric.name 156276319\n";
                //"another.metric.result.cpu% 1.23e-1 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        assertEquals(0, metrics.size());
    }

    @Test
    public void shouldFailOnInvalidTimestamp(){
        String metricStringToTest = "some.metric.name 1234 1562HH76319\n" +
                "another.metric.result.cpu% 1.23e-1 -1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        String[] lines = undertest.getMetricsInput().split(System.getProperty("line.separator"));
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        testSuccessFail(0, 0, 2, metrics, undertest);
    }

    @Test
    public void shouldValidateGraphiteTaggedMetricName() throws IOException {
        String metricStringToTest = "some.metric.name;host=kipper;name=flash;test=ISOK 1234 156276319\n" ;
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        String[] lines = undertest.getMetricsInput().split(System.getProperty("line.separator"));
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        System.out.println(undertest.toString());
        testSuccessFail(1, 0, 0, metrics, undertest);
        assertEquals(true, undertest.isFileLoaded());
    }

    @Test
    public void shouldValidateGraphiteMetricName() throws IOException {
        String metricStringToTest = "a.metr|ic.name 1234 156276319\n" +
                "another.metr^ic.result.cpu% 1.23 1562763195000\n" +
                "% 1999991 1562763195000\n" +
                "1stserver.result.cpu% 123123 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        String[] lines = undertest.getMetricsInput().split(System.getProperty("line.separator"));
        undertest.parse();
        List<Metric> metrics = undertest.getMetrics();
        System.out.println(undertest.toString());
        testSuccessFail(0, 0, 4, metrics, undertest);
        assertEquals(true, undertest.isFileLoaded());
    }

    @Test
    public void shouldReadEachLineOfMetrics(){
        ReaderService readerService = new FileReaderService("metrics-file.log");
        MetricsParser undertest = new MetricsParser(readerService);
        String[] lines = undertest.getMetricsInput().split(System.getProperty("line.separator"));
        assertEquals(2, lines.length);
    }

    @Test
    public void shouldReadMetricsString() throws IOException {
        String metricStringToTest = "some.metric.name 1234 156276319\n" +
                "another.metric.result.cpu% 1.23e-1 1562763195000\n";
        ReaderService readerService = new StringReaderService(metricStringToTest);
        MetricsParser undertest = new MetricsParser(readerService);
        System.out.println(undertest.toString());
        assertEquals(true, undertest.isFileLoaded());
    }

    private void testSuccessFail(long size, long validMetrics, long invalidMetrics, List<Metric> metrics, MetricsParser undertest) {
        assertEquals(size, metrics.size());
        assertEquals(validMetrics, undertest.getValidMetrics());
        assertEquals(invalidMetrics, undertest.getInvalidMetrics());
    }

}
