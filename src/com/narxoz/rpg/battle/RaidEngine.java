package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        if (teamA == null || teamB == null) {
            throw new IllegalArgumentException("Teams cannot be null");
        }
        if (teamASkill == null || teamBSkill == null) {
            throw new IllegalArgumentException("Skills cannot be null");
        }
        if (!teamA.isAlive() || !teamB.isAlive()) {
            throw new IllegalArgumentException("Both teams must have alive members");
        }

        RaidResult result = new RaidResult();
        int round = 0;

        result.addLine("=== RAID BATTLE START ===");
        result.addLine("Team A: " + teamA.getName());
        result.addLine("Team B: " + teamB.getName());
        result.addLine("Team A Skill: " + teamASkill.getSkillName() +
                " (" + teamASkill.getEffectName() + ")");
        result.addLine("Team B Skill: " + teamBSkill.getSkillName() +
                " (" + teamBSkill.getEffectName() + ")");

        result.addLine("\nTeam A composition:");
        teamA.printTree("  ");
        result.addLine("\nTeam B composition:");
        teamB.printTree("  ");
        result.addLine("");

        while (teamA.isAlive() && teamB.isAlive()) {
            round++;
            result.addLine("\n=== Round " + round + " ===");

            result.addLine("Team A casts " + teamASkill.getSkillName() + ":");
            boolean A = random.nextInt(100) < 10; // 10% critical chance
            if (A) {
                result.addLine("  ⚡ CRITICAL HIT!");
            }

            int healthBeforeB = teamB.getHealth();
            teamASkill.cast(teamB);

            result.addLine("  Team B health: " + healthBeforeB + " → " + teamB.getHealth());

            if (!teamB.isAlive()) {
                result.addLine("\n💀 Team B has been defeated!");
                break;
            }

            if (teamB.isAlive()) {
                result.addLine("\nTeam B casts " + teamBSkill.getSkillName() + ":");
                boolean B = random.nextInt(100) < 10;
                if (B) {
                    result.addLine("  ⚡ CRITICAL HIT!");
                }

                int healthBeforeA = teamA.getHealth();
                teamBSkill.cast(teamA);
                result.addLine("  Team A health: " + healthBeforeA + " → " + teamA.getHealth());
            }
        }

        String winner;
        if (!teamA.isAlive() && !teamB.isAlive()) {
            winner = "Draw - both teams defeated";
        } else if (!teamA.isAlive()) {
            winner = "Team B";
        } else if (!teamB.isAlive()) {
            winner = "Team A";
        } else {
            winner = "Draw";
        }
        result.setWinner(winner);
        result.setRounds(round);

        result.addLine("TODO: implement raid simulation");
        result.addLine("Winner: " + winner);
        result.addLine("Rounds fought: " + round);
        return result;
    }
}
