package dev.hilligans.upgrades;

import dev.hilligans.GameInstance;
import dev.hilligans.client.graphics.*;
import dev.hilligans.registry.IRegistryElement;
import dev.hilligans.resource.ResourceLocation;

import java.util.ArrayList;

public class Upgrade implements IRegistryElement {

    public String upgradeName;

    public String upgradeImageName;
    public GameInstance gameInstance;

    public Texture texture;


    public int damage;
    public int health;
    public float speed = 1;

    public Upgrade(String upgradeName, String upgradeImageName) {
        this.upgradeName = upgradeName;
        this.upgradeImageName = upgradeImageName;
        this.texture = new Texture("images/upgrades/" + upgradeImageName);
    }

    public Upgrade damage(int damage) {
        this.damage = damage;
        return this;
    }

    public Upgrade health(int health) {
        this.health = health;
        return this;
    }

    public Upgrade speed(float multiplier) {
        this.speed = multiplier;
        return this;
    }

    @Override
    public void load(GameInstance gameInstance) {
        IRegistryElement.super.load(gameInstance);
        this.gameInstance = gameInstance;
    }

    @Override
    public String getResourceName() {
        return null;
    }

    @Override
    public String getUniqueName() {
        return null;
    }

    public ArrayList<String> getLines() {
        ArrayList<String> lines = new ArrayList<>();
        if(damage != 0) {
            lines.add("+" + damage + " damage");
        }
        if(health != 0) {
            lines.add("+" + health + " health");
        }
        if(speed != 1) {
            lines.add(speed + "x speed");
        }
        return lines;
    }
}
