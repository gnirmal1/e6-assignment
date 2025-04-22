package org.e6.engine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Engine {
    private final int port;
    private volatile boolean running;

    public Engine(int port) {
        this.port = port;
        this.running = true;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Engine <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Engine engine = new Engine(port);
        engine.start();
    }

    public void start() {
        running = true;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Engine shutting down...");
            stop();
        }));

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Engine listening on port " + port);
            while (running) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
                ) {
                    String fileName = in.readUTF();
                    System.out.println("Received file: " + fileName);

                    EngineResult result = processFile(fileName);
                    out.writeObject(result);
                    System.out.println("Sent result.");
                } catch (IOException e) {
                    if (running) System.err.println("Connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
    }

    private EngineResult processFile(String fileName) {
        Map<Integer, Integer> min = new HashMap<>();
        Map<Integer, Integer> max = new HashMap<>();
        Map<Integer, Float> avg = new HashMap<>();

        min.put(2011, 1);
        max.put(2012, 100);
        avg.put(2013, 50.5f);

        return new EngineResult(min, max, avg);
    }
}
