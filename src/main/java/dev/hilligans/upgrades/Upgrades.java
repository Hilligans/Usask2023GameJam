package dev.hilligans.upgrades;

import dev.hilligans.GameInstance;
import dev.hilligans.Main;

public class Upgrades {

    //aseprite -b Sprite-0001.aseprite --save-as {slice}.png

    public static final Upgrade TOMATO = new Upgrade("tomato", "tomato.png").damage(2);
    public static final Upgrade CARROT = new Upgrade("carrot", "carrot.png").damage(5);
    public static final Upgrade GARLIC = new Upgrade("garlic", "garlic.png").damage(10).speed(0.7f);
    //todo add special garlic effect

    public static final Upgrade BANANA = new Upgrade("banana", "banana.png").damage(2);
    public static final Upgrade MOLASSES = new Upgrade("molasses", "molasses.png").damage(1);
    public static final Upgrade PEPSI = new Upgrade("pepsi", "pepsi.png").speed(1.3f);

    public static final Upgrade MELON_HELMET = new Upgrade("melon helmet", "melon helmet.png").health(15);

    public static void loadUpgrades(GameInstance gameInstance) {
        gameInstance.UPGRADES.put(TOMATO).put(CARROT).put(GARLIC).put(MELON_HELMET);
        for(Upgrade upgrade : gameInstance.UPGRADES.ELEMENTS) {
            gameInstance.TEXTURES.put(upgrade.texture);
        }
    }




}
