package com.company;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.Scanner;

class Main {
    @Parameter(names={"--port"}, required = true)
    private int port;
    @Parameter(names={"--ip"}, required = true)
    private String ip;

    public static void main(String ... argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        SocketClient client = new SocketClient();
        client.startConnection(ip, port);
        while (true) {
            String message = scanner.nextLine();
            client.sendMessage(message);
        }
    }
}