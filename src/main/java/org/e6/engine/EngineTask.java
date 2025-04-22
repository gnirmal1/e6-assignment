package org.e6.engine;

import java.io.*;
import java.net.Socket;

public class EngineTask {
    private final int port;
    private final String host;
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public EngineTask(int port) {
        this("localhost", port);
    }

    public EngineTask(String host, int port) {
        try {
            this.port = port;
            this.host = host;
            this.socket = new Socket(host, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            throw new RuntimeException("Could not connect to engine on port " + port, e);
        }
    }

    public EngineResult submit(String fileName) {
        try {
            out.writeUTF(fileName);
            out.flush();

            return (EngineResult) in.readObject();
        }
        catch (IOException e) {
            System.err.println("Error sending file: " + e.getMessage());
            return null;
        }
        catch (ClassNotFoundException e) {
            System.err.println("Error reading result: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
