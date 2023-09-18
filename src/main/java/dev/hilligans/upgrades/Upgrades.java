package dev.hilligans.upgrades;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;

import java.util.Random;

public class Upgrades {

    //aseprite -b Sprite-0001.aseprite --save-as {slice}.png

    public static final Upgrade CARROT = new Upgrade("carrot", "carrot.png", 1).damage(10).reloadCount(-1).reloadSpeed(1.2f);
    public static final Upgrade TOMATO = new Upgrade("tomato", "tomato.png", 0).damage(5).reloadSpeed(0.9f);
    public static final Upgrade GARLIC = new Upgrade("garlic", "garlic.png", 2).damage(7).reloadSpeed(1.2f);
    public static final Upgrade BANANA = new Upgrade("banana", "banana.png", 4).damage(4).health(10).customUpgradeMessage("mmm potassium");//.customUpgradeMessage("Spawns a banana peel");
    public static final Upgrade MOLASSES = new Upgrade("molasses", "molasses.png", 3).damage(3).temporarySpeed(0.50f);
    public static final Upgrade PEPSI = new Upgrade("pepsi", "pepsi.png", -1).speed(1.2f).reloadSpeed(0.8f).reloadCount(1).damage(-2);
    public static final Upgrade MELON_HELMET = new Upgrade("melon helmet", "melon helmet.png", -1).health(30).speed(0.9f);
    public static final Upgrade SUGAR = new Upgrade("\"sugar\"", "sugar.png", -1).speed(1.5f).reloadCount(-2).reloadSpeed(0.5f);

    public static void loadUpgrades(GameInstance gameInstance) {
        gameInstance.UPGRADES.put(TOMATO).put(CARROT).put(GARLIC).put(MELON_HELMET).put(BANANA).put(MOLASSES).put(PEPSI).put(SUGAR);
        int x = 0;
        for(Upgrade upgrade : gameInstance.UPGRADES.ELEMENTS) {
            gameInstance.TEXTURES.put(upgrade.texture);
            upgrade.upgradeID = x++;
        }
    }

    public static Upgrade[] getRandomUpgrades() {
        Upgrade[] upgrades = new Upgrade[5];
        Random random = new Random();
        int length = Main.main.gameInstance.UPGRADES.ELEMENTS.size();
        for(int x = 0; x < upgrades.length; x++) {
            upgrades[x] = getUpgrade(random.nextInt(length));
        }
        return upgrades;
    }

    public static Upgrade getUpgrade(int id) {
        return Main.main.gameInstance.UPGRADES.get(id);
    }
}
