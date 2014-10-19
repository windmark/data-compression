package com.windmark.marcus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FrequencyTable {
    private int[] frequencies;

    public FrequencyTable(int size) {
        if (size < 2)
            throw new IllegalArgumentException("A length of at least 2 is needed");
        frequencies = new int[size];
    }

    public void generate(InputStream inputStream) {
        int data;
        try {
            while ((data = inputStream.read()) != -1) {
                data = inputStream.read();
                frequencies[data]++;

            }
        } catch (IOException e) {
            e.printStackTrace();
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



