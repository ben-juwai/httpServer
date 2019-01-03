package com.archserver;

public class Main {
    public static void main(String[] args) {
        ArchHttpServer server = new ArchHttpServer();
        server.serve();
    }
}
