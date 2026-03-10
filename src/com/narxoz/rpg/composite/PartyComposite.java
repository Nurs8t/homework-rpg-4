package com.narxoz.rpg.composite;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartyComposite implements CombatNode {
    private final String name;
    private final List<CombatNode> children = new ArrayList<>();

    public PartyComposite(String name) {
        this.name = name;
    }

    public void add(CombatNode node) {
        children.add(node);
    }

    public void remove(CombatNode node) {
        children.remove(node);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        int h=0;
        for (CombatNode child : children) {
            h += child.getHealth();
        }
        return h;
    }

    @Override
    public int getAttackPower() {
        int a=0;
        for (CombatNode child : children) {
            if (child.isAlive()) {
                a += child.getAttackPower();
            }
        }
        return a;
    }

    @Override
    public void takeDamage(int amount) {
        List<CombatNode> aliveChildren = getAliveChildren();

        if (aliveChildren.isEmpty()) {
            return;
        }

        int damCh = amount / aliveChildren.size();
        int remDam= amount % aliveChildren.size();
        System.out.println("   " + name + " takes " + amount + " damage distributed among " + aliveChildren.size() + " members");

        for (CombatNode child : aliveChildren) {
            child.takeDamage(damCh);
        }
        for (int i = 0; i < remDam; i++) {
            if (i < aliveChildren.size() && aliveChildren.get(i).isAlive()) {
                aliveChildren.get(i).takeDamage(1);
            }
        }
    }

    @Override
    public boolean isAlive() {
        for (CombatNode child : children) {
            if (child.isAlive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CombatNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public void printTree(String indent) {
        int HP = 0;
        int ATK = 0;
        for (CombatNode child : children) {
            HP += child.getHealth();
            if (child.isAlive()) {
                ATK += child.getAttackPower();
            }
        }
        System.out.println(indent  + "+ " + name + " [HP:" + HP + ", ATK:" + ATK + "]");
        for (CombatNode child : children) {
            child.printTree(indent + "   ");
        }
    }

    public List<CombatNode> getAliveChildren() {
        List<CombatNode> alive = new ArrayList<>();
        for (CombatNode child : children) {
            if (child.isAlive()) {
                alive.add(child);
            }
        }
        return alive;
    }
}
