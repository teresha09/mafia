package com.company.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private static GameServer gameInstance;
    private List<Game> games;

    private GameServer() {
        games = new ArrayList<>();
    }

    public static GameServer getGameInstance() {
        if (gameInstance == null) {
            gameInstance = new GameServer();
        }
        return gameInstance;
    }

    public void start(int port) {
        CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        while (players.size() < 5) {
            try {
                Player player = new Player(serverSocket.accept());
                players.add(player);
                System.out.println(players.size() + " / 5");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        Game gameRoom = new Game(players);
        games.add(gameRoom);
        gameRoom.start();
    }
}
