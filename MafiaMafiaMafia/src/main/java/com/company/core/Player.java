package com.company.core;

import com.company.services.MessageService;

import java.io.*;
import java.net.Socket;

public class Player extends Thread {
    private MessageService messageService;
    private Socket playersSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Integer id;
    private String nickname;
    private boolean isMafia;
    private boolean isAlive = true;
    private boolean isChatting;
    private boolean isVoting;
    private Game gameInstance;

    public Player(Socket playersSocket) {
        this.playersSocket = playersSocket;
    }

    public Integer getPlayersId() {
        return id;
    }

    public void setPlayersId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getMafia() {
        return isMafia;
    }

    public void setMafia(Boolean mafia) {
        isMafia = mafia;
    }

    public Boolean getAlive() {
        return isAlive;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public Socket getPlayersSocket() {
        return playersSocket;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(
                    new InputStreamReader(playersSocket.getInputStream()));
            out = new PrintWriter(playersSocket.getOutputStream(), true);

            String inputLine;

            while ((inputLine = in.readLine()) != null && isAlive && (isChatting || isVoting)) {
                if (isVoting) {
                    try {
                        int vote = Integer.parseInt(inputLine);
                        if (vote > 4) {
                            out.println("В игре всего 5 игроков!");
                            continue;
                        }
                        if (vote == id) {
                            out.println("Нельзя голосовать за самого себя!");
                            continue;
                        }
                        if (!gameInstance.vote(vote)) {
                            out.println("Игрок уже мертв!");
                            continue;
                        }
                        messageService.messageAllPlayers("[ID:" + id + "] " + nickname + " проголосовал(а) против [ID:" + vote + "]");
                        isVoting = false;
                    } catch (NumberFormatException e) {
                        out.println("Неправильный формат!");
                    }
                } else {
                    messageService.messageAllPlayers(inputLine, "[ID:" + id + "] " + nickname);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void isChatting(Boolean isChatting) {
        this.isChatting = isChatting;
    }

    public void isVoting(Boolean isVoting) {
        this.isVoting = isVoting;
    }

    public void setGameInstance(Game gameInstance) {
        this.gameInstance = gameInstance;
    }
}
