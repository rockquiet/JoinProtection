package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class DamageListener implements Listener {

    private final JoinProtection joinProtection;
    private final ProtectionHandler protectionHandler;

    public DamageListener(JoinProtection joinProtection,
                          ProtectionHandler protectionHandler) {
        this.joinProtection = joinProtection;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (joinProtection.getConfig().getBoolean("cancel.on-attack") && (event.getDamager() instanceof Player player) && protectionHandler.hasProtection(player) && !event.getDamager().hasPermission("joinprotection.bypass.cancel-on-attack")) {
            protectionHandler.cancelProtection(player, "messages.protectionDeactivatedAttack");
        }

        if (joinProtection.getConfig().getBoolean("sound.enabled") && (event.getEntity() instanceof Player player) && protectionHandler.hasProtection(player) && (event.getDamager() instanceof Player attacker)) {
            String sound = joinProtection.getConfig().getString("sound.type");
            float volume = (float) joinProtection.getConfig().getDouble("sound.volume");
            float pitch = (float) joinProtection.getConfig().getDouble("sound.pitch");

            attacker.playSound(player, Sound.valueOf(sound), volume, pitch);
        }

        if ((event.getEntity() instanceof Player player) && protectionHandler.isEventCancelled(player, "modules.disable_damage_by_entities")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (protectionHandler.isEventCancelled(player, "modules.disable_damage_by_blocks")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (protectionHandler.isEventCancelled(player, "modules.disable_damage")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityTargetPlayer(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) {
            return;
        }

        if (protectionHandler.isEventCancelled(player, "modules.disable_entity_targeting")) {
            event.setCancelled(true);
        }
    }
}
