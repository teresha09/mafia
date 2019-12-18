package com.company.services;

import com.company.core.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MessageService {
    private List<Player> players;

    public MessageService(List<Player> players) {
        this.players = players;
    }

    public void sayRoles() {
        for (Player player : players) {
            try {
                PrintWriter out = new PrintWriter(player.getPlayersSocket().getOutputStream(), true);
                if (player.getMafia()) {
                    out.println("[ID:" + player.getPlayersId() + "] " + player.getNickname() + " - вы мафия!");
                } else {
                    out.println("[ID:" + player.getPlayersId() + "] " + player.getNickname() + " - вы мирный житель.");
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void messageAllPlayers(String text) {
        for (Player player: players) {
            try {
                PrintWriter out = new PrintWriter(player.getPlayersSocket().getOutputStream(), true);
                out.println(text);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void messageAllPlayers(String text, String sender) {
        for (Player player: players) {
            try {
                PrintWriter out = new PrintWriter(player.getPlayersSocket().getOutputStream(), true);
                out.println(sender + ": " + text);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void messageSomePlayers(List<Player> players, String text) {
        for (Player player: players) {
            try {
                PrintWriter out = new PrintWriter(player.getPlayersSocket().getOutputStream(), true);
                out.println(text);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void messageSomePlayers(List<Player> players, String text, String sender) {
        for (Player player: players) {
            try {
                PrintWriter out = new PrintWriter(player.getPlayersSocket().getOutputStream(), true);
                out.println(sender + ": " + text);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
