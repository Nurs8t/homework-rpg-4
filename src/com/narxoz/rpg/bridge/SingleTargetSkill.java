package com.narxoz.rpg.bridge;

import com.narxoz.rpg.composite.CombatNode;
import com.narxoz.rpg.composite.PartyComposite;
import com.narxoz.rpg.composite.RaidGroup;

public class SingleTargetSkill extends Skill {
    public SingleTargetSkill(String skillName, int basePower, EffectImplementor effect) {
        super(skillName, basePower, effect);
    }

    @Override
    public void cast(CombatNode target) {
        if (target == null || !target.isAlive()) {
            return;
        }

        int damage = resolvedDamage();
        int hits = applyToLeaves(target, damage);

        System.out.println(getSkillName() + " [" + getEffectName() + "] hits "
                + hits + " targets for " + damage + " damage each.");
    }

    private int applyToLeaves(CombatNode node, int damage) {

        if (!node.isAlive()) {
            return 0;
        }

        if (node instanceof PartyComposite party) {
            int total = 0;
            for (CombatNode child : party.getChildren()) {
                total += applyToLeaves(child, damage);
            }
            return total;
        }

        node.takeDamage(damage);
        return 1;
    }
}