package org.e6.engine;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EngineResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Map<Integer, Integer> min;
    public Map<Integer, Integer> max;
    public Map<Integer, Float> avg;

    public EngineResult(){
        this.min = new HashMap<>();
        this.max = new HashMap<>();
        this.avg = new HashMap<>();
    }
    public EngineResult(Map<Integer, Integer> min, Map<Integer, Integer> max, Map<Integer, Float> avg) {
        this.min = min;
        this.max = max;
        this.avg = avg;
    }

    public void writeToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Integer key : min.keySet()) {
                int minVal = min.getOrDefault(key, 0);
                int maxVal = max.getOrDefault(key, 0);
                float avgVal = avg.getOrDefault(key, 0f);
                int flooredAvg = (int) Math.floor(avgVal);
                writer.write(String.format("%d,%d,%d,%d%n", key, minVal, maxVal, flooredAvg));
            }
        } catch (IOException e) {
            System.err.println("Failed to write EngineResult to file: " + e.getMessage());
        }
    }
}
