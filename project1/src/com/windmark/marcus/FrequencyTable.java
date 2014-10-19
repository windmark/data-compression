package com.windmark.marcus;

import java.io.*;

public class FrequencyTable {
    private int[] frequencies;

    public FrequencyTable(int size) {
        if (size < 2)
            throw new IllegalArgumentException("A length of at least 2 is needed");
        frequencies = new int[size];
    }

    public void generate(File inputFile) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));

        try {
            int data;
            while ((data = inputStream.read()) != -1) {
                data = inputStream.read();
                frequencies[data]++;
            }

        } finally {
            inputStream.close();
        }
    }

    public int get(int i) {
        if (i < 0 || i >= frequencies.length)
            throw new IllegalArgumentException("Index out of range");
        return frequencies[i];
    }

    public int length() {
        return frequencies.length;
    }
}



