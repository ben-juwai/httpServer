package com.archserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ArchHttpServer {
    private ServerSocket serverSocket = null;

    private int port = 8990;

    private String addrIp = "127.0.0.1";

    private InetAddress addr;

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    public ArchHttpServer() {
        try {
            addr = InetAddress.getByName(addrIp);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        serverInit(port, addr);
    }

    public ArchHttpServer(InetAddress addr) {
        serverInit(port, addr);
    }

    public ArchHttpServer(int port, String addrIp) {
        try {
            addr = InetAddress.getByName(addrIp);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        serverInit(port, addr);
    }

    private void serverInit(int port, InetAddress addr) {
        try {
            serverSocket = new ServerSocket(port, 2, addr);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void serve() {
        int i = 0;
        while (true) {
            Socket socket = null;
            InputStream input = null;
            OutputStream output = null;

            try {
                socket = serverSocket.accept();
                i++;
                System.out.println("Got connect " + i);
                input = socket.getInputStream();
                output = socket.getOutputStream();
                String data = read(input);
                if (data.contains(SHUTDOWN_COMMAND)) {
                    break;
                }
                write(output, data);
                socket.close();
                i--;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private String read(InputStream input) {
        //StringBuffer request = new StringBuffer(2048);
        byte[] request = new byte[2048];
        try {
            input.read(request);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return new String(request, StandardCharsets.UTF_8);
    }

    private void write(OutputStream output, String data) {
        try {
            String header = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
            output.write(header.getBytes());
            output.write("Get data from request: ".getBytes());
            output.write(data.getBytes());
            Thread.sleep(1000 * 5);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
