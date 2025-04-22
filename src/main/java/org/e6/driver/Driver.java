package org.e6.driver;

import org.e6.engine.EngineResult;
import org.e6.engine.EngineTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Driver {

    private final List<EngineTask> engines;

    public Driver(List<Integer> ports) {
        engines = new ArrayList<>();
        for (int port : ports) {
            engines.add(new EngineTask(port));
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Driver <port1> <port2> <port3>");
            return;
        }

        List<Integer> ports = new ArrayList<>();
        try {
            for (String arg : args) {
                ports.add(Integer.parseInt(arg));
            }
        } catch (NumberFormatException e) {
            System.err.println("All arguments must be valid integers.");
            return;
        }

        Driver driver = new Driver(ports);
        EngineResult result = driver.execute("sample_dataset/student_scores");
        driver.writeResult(result, "output.txt");
    }

    public void writeResult(EngineResult result, String filename) {
        result.writeToFile(filename);
    }

    private List<String> getDatasetFiles(String datasetFolder){
        try (var paths = Files.list(Paths.get(datasetFolder))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .map(path -> path.getFileName().toString())
                    .toList();
        }
        catch (IOException e) {
            System.err.println("Error reading getting dataset files: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public EngineResult execute(String datasetFolder){

        List<String> dataFiles = getDatasetFiles(datasetFolder);

        EngineResult result = new EngineResult();
        for (String dataFile : dataFiles) {
            int engineIndex = dataFile.hashCode() % engines.size();
            result = engines.get(engineIndex).submit(dataFile);
        }
        return result;
    }

    public void close(){
        for (EngineTask engine : engines) {
            engine.close();
        }
    }
}
