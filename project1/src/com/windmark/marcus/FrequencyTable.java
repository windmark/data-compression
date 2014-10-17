package com.windmark.marcus;

import java.io.FileInputStream;
import java.io.IOException;

public class FrequencyTable {
    private int[] frequencies;

    public FrequencyTable(int size) {
        if (size < 2)
            throw new IllegalArgumentException("A length of at least 2 is needed");
        frequencies = new int[size];
    }

    public void init(FileInputStream fileInputStream) {
        int data;
        try {
            while ((data = fileInputStream.read()) != -1) {
                data = fileInputStream.read();
                frequencies[data]++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int get(int i) {
        return frequencies[i];
    }

    public int length() {
        return frequencies.length;
    }
}



