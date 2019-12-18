package com.company;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.company.core.GameServer;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    @Parameter(names = {"--port"}, required = true)
    private int port;

    public static void main(String... argv) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
        main.run();
    }

    private void run() {
        GameServer gameServer = GameServer.getGameInstance();
        int i = 0;
        while (true) {
            gameServer.start(port + i);
            i++;
        }
    }
}
