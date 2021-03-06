/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herobounty;

import com.herocraftonline.dthielke.herobounty.bounties.Bounty;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import java.util.List;

public class HeroBountyEntityListener extends EntityListener {
    public static HeroBounty plugin;

    public HeroBountyEntityListener(HeroBounty plugin) {
        HeroBountyEntityListener.plugin = plugin;
    }

    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;
        Player defender = (Player) entity;
        String defenderName = defender.getName();

        EntityDamageEvent dmgEvent = entity.getLastDamageCause();
        String attackerName;
        if (dmgEvent instanceof EntityDamageByEntityEvent) {
            Entity attacker = ((EntityDamageByEntityEvent) dmgEvent).getDamager();
            if (dmgEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                attacker = ((Projectile) attacker).getShooter();
            if (attacker instanceof Player)
                attackerName = ((Player) attacker).getName();
            else
                return;
        } else {
            return;
        }

        List<Bounty> bounties = plugin.getBountyManager().getBounties();
        for (int i = 0; i < bounties.size(); i++) {
            Bounty b = bounties.get(i);

            if (b.getTarget().equalsIgnoreCase(defenderName) && b.isHunter(attackerName)) {
                plugin.getBountyManager().completeBounty(i, attackerName);
                return;
            }
        }
    }
}
