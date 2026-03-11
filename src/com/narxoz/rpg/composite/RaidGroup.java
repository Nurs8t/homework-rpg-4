
package com.narxoz.rpg.composite;

import java.util.List;

public class RaidGroup extends PartyComposite {

    public RaidGroup(String name) {
        super(name);
    }

    @Override
    public void printTree(String indent) {
        int HP = 0;
        int ATK = 0;
        List<CombatNode> children = getChildren();

        for (CombatNode child : children) {
            HP += child.getHealth();
            if (child.isAlive()) {
                ATK += child.getAttackPower();
            }
        }
        System.out.println(indent + getName() + " [HP:" + HP + ", ATK:" + ATK + "]");
        for (CombatNode child : children) {
            child.printTree(indent + "   ");
        }
    }


    @Override
    public void takeDamage(int amount) {
        List<CombatNode> aliveChildren = getAliveChildren();

        if (aliveChildren.isEmpty()) {
            return;
        }

        int damCh = amount / aliveChildren.size();
        int remDam = amount % aliveChildren.size();

        System.out.println( getName() + " takes " + amount + " damage distributed among " + aliveChildren.size() + " members");

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
    public int getAttackPower() {
        int totalAttack = 0;
        List<CombatNode> children = getChildren();

        for (CombatNode child : children) {
            if (child.isAlive()) {
                totalAttack += child.getAttackPower();
            }
        }


        if (getAliveCount() > 3) {
            totalAttack = (int)(totalAttack * 1.2);
            System.out.println(" RAID COORDINATION BONUS: +20% damage");
        }

        return totalAttack;
    }

    private int getAliveCount() {
        return 0;
    }

    @Override
    public int getHealth() {
        int totalHealth = 0;
        List<CombatNode> children = getChildren();

        for (CombatNode child : children) {
            totalHealth += child.getHealth();
        }

        return totalHealth;
    }

    @Override
    public boolean isAlive() {
        List<CombatNode> children = getChildren();
        for (CombatNode child : children) {
            if (child.isAlive()) {
                return true;
            }
        }
        return false;
    }

}