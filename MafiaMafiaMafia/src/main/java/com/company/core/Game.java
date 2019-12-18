package com.company.core;

import com.company.services.MessageService;

import java.util.ArrayList;
import java.util.List;

public class Game extends Thread {
    private MessageService messageService;
    private List<Player> players;
    private List<Player> mafia;
    private int[] votingStash;

    public Game(List<Player> players) {
        this.players = players;
        mafia = new ArrayList<>();
        messageService = new MessageService(players);
        String[] names = {"Egor", "Bogdan", "Vika", "Niyaz", "Syumbel"};
        int i = 0;
        for (Player player : players) {
            player.setPlayersId(i);
            player.setNickname(names[i]);
            player.setMessageService(messageService);
            player.setGameInstance(this);
            i++;
            player.start();
        }
    }

    private void chooseMafia() {
        int r = (int) (Math.random() * players.size());
        players.get(r).setMafia(true);
        mafia.add(players.get(r));
    }

    public boolean vote(int playersId) {
        if (players.get(playersId).getAlive()) {
            votingStash[playersId] = votingStash[playersId] + 1;
            return true;
        }
        return false;
    }

    private Integer getIndexOfMax(int[] arr) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                index = i;
            }
        }
        if (max == 0) {
            return null;
        }
        return index;
    }

    public boolean isCitizensAlive() {
        for (Player player : players) {
            if (!player.getMafia() && player.getAlive()) {
                return true;
            }
        }
        return false;
    }

    public boolean isMafiaAlive() {
        for (Player player : mafia) {
            if (player.getAlive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        chooseMafia();
        messageService.sayRoles();
        while (isCitizensAlive() && isMafiaAlive()) {
            dayChat();
            dayVote();
            if (!isCitizensAlive()) {
                messageService.messageAllPlayers("Победила мафия!");
                break;
            } else if (!isMafiaAlive()) {
                messageService.messageAllPlayers("Победили мирные жители!");
                break;
            }
            nightChat();
            nightVote();
            if (!isCitizensAlive()) {
                messageService.messageAllPlayers("Победила мафия!");
                break;
            } else if (!isMafiaAlive()) {
                messageService.messageAllPlayers("Победили мирные жители!");
                break;
            }
        }
    }

    public void dayChat() {
        messageService.messageAllPlayers("Наступил день, игроки могут писать в чат.");
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            if (player.getAlive()) {
                builder.append("[ID:").append(player.getPlayersId()).append("] ").append(player.getNickname()).append(", ");
            }
        }
        messageService.messageAllPlayers(builder.substring(0, builder.length() - 2));
        try {
            for (Player player : players) {
                player.isChatting(true);
            }

            Thread.sleep(30000);

            for (Player player : players) {
                player.isChatting(false);
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void dayVote() {
        messageService.messageAllPlayers("\n\rНачинается голосование, напишите ID игрока, против которого вы хотите проголосовать.");
        try {
            votingStash = new int[players.size()];
            for (Player player : players) {
                player.isVoting(true);
            }

            Thread.sleep(10000);

            for (Player player : players) {
                player.isVoting(false);
            }
            Integer idOfDead = getIndexOfMax(votingStash);
            if (idOfDead != null) {
                players.get(idOfDead).setAlive(false);
                if (players.get(idOfDead).getMafia()) {
                    messageService.messageAllPlayers("Убит игрок [ID: " + players.get(idOfDead).getPlayersId() + "] " + players.get(idOfDead).getNickname() + ", он был мафией.");
                } else {
                    messageService.messageAllPlayers("Убит игрок [ID: " + players.get(idOfDead).getPlayersId() + "] " + players.get(idOfDead).getNickname() + ", он был мирным жителем.");
                }
            } else {
                messageService.messageAllPlayers("\n\rНикто ни за кого не проголосовал, все остаются в живых.");
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void nightChat() {
        messageService.messageAllPlayers("\n\rНаступила ночь, мафия общается.");
        try {
            messageService.setPlayers(mafia);
            for (Player player : mafia) {
                player.isChatting(true);
            }

            Thread.sleep(10000);

            for (Player player : players) {
                player.isChatting(false);
            }
            messageService.setPlayers(players);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void nightVote() {
        messageService.messageAllPlayers("\n\rМафия выбирает жертву.");
        try {
            votingStash = new int[players.size()];
            for (Player player : mafia) {
                player.isVoting(true);
            }

            Thread.sleep(5000);

            for (Player player : players) {
                player.isVoting(false);
            }

            Integer idOfDead = getIndexOfMax(votingStash);
            if (idOfDead != null) {
                players.get(idOfDead).setAlive(false);
                if (players.get(idOfDead).getMafia()) {
                    messageService.messageAllPlayers("Убит игрок [ID: " + players.get(idOfDead).getPlayersId() + "] " + players.get(idOfDead).getNickname() + ", он был мафией.");
                } else {
                    messageService.messageAllPlayers("Убит игрок [ID: " + players.get(idOfDead).getPlayersId() + "] " + players.get(idOfDead).getNickname() + ", он был мирным жителем.");
                }
            } else {
                messageService.messageAllPlayers("Никто ни за кого не проголосовал, все остаются в живых.");
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
