package dev.hilligans;

import dev.hilligans.game.Game;
import dev.hilligans.network.Packet.Server.SAwardWinPacket;
import dev.hilligans.network.Packet.Server.SGameReadyPacket;
import dev.hilligans.network.Packet.Server.SGameStartPacket;
import dev.hilligans.network.Packet.Server.SPlayerInfoPacket;
import dev.hilligans.network.PacketBase;
import dev.hilligans.network.ServerNetwork;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    public GameInstance gameInstance;
    public ServerNetwork serverNetwork;
    public Game game = new Game();

    public int gameState = 0;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    public boolean running = false;

    public void startThreadLoop() {
        executorService.scheduleAtFixedRate(() -> {
            try {
                if(running) {
                    game.tickEntities();
                    int state = game.checkState();
                    if(state != 0) {
                        running = false;
                        gameState = state;
                        sendPacket(new SAwardWinPacket(state));
                        game.reset();
                    }
                    if(game.time()) {
                        running = false;
                        gameState = 3;
                        sendPacket(new SAwardWinPacket(3));
                        game.reset();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 20, TimeUnit.MILLISECONDS);
    }

    public void startGame() {
        sendPacket(new SGameStartPacket());
        running = true;
    }

    public void preStart() {
        game.player1.health = game.player1.maxHealth;
        game.player2.health = game.player2.maxHealth;

        game.player1.setPos(-800, 0, 0);
        game.player2.setPos(800, 0, 0);

        game.player1.shots = Math.min(3, game.player1.getMaxShots());
        game.player2.shots = Math.min(3, game.player2.getMaxShots());

        sendPacket(new SPlayerInfoPacket(game.player1));
        sendPacket(new SPlayerInfoPacket(game.player2));
    }
    int player1Upgrades = 1;
    int player2Upgrades = 1;

    public synchronized void handleUpgrade(int playerID) {
        if(gameState == 0) {
            if(playerID == 1) {
                player1Upgrades -= 1;
            } else {
                player2Upgrades -= 1;
            }
            if(player1Upgrades == 0 && player2Upgrades == 0) {
                Thread thread = new Thread(this::startSequence);
                thread.start();
            }
        } else {
            gameState -= playerID;
            if(gameState == 0) {
                Thread thread = new Thread(this::startSequence);
                thread.start();
            }
        }
    }

    public void startSequence() {
        sendPacket(new SGameReadyPacket(1));
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendPacket(new SGameReadyPacket());
        preStart();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startGame();
    }

    public Server(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void sendPacket(PacketBase packetBase) {
        game.sendPacketToAll(packetBase);
    }
}
