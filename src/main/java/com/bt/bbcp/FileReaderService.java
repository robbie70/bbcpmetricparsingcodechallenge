package com.bt.bbcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileReaderService implements ReaderService {

    private final String fileName;

    public FileReaderService(String fileName){
        this.fileName = fileName;
    }

    @Override
    public String getData() {
        File initialFile = getFileFromResources(fileName);
        try (InputStream targetStream = new FileInputStream(initialFile)) {
            return readfile_iso_8859_1(targetStream);
        } catch (IOException fe) {
            throw new RuntimeException(fe);
        }
    }

    private static String readfile_iso_8859_1(InputStream is) throws IOException {
        StringBuilder chars = new StringBuilder(Math.max(is.available(), 4096));
        byte[] buffer = new byte[4096];
        int n;
        while ((n = is.read(buffer)) != -1) {
            for (int i = 0; i < n; i++) {
                chars.append((char) (buffer[i] & 0xFF));
            }

        }
        return chars.toString();
    }

    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

}
