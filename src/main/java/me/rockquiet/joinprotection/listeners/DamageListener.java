package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import net.kyori.adventure.text.minimessage.MiniMessage;
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

    public DamageListener(JoinProtection joinProtection) {
        this.joinProtection = joinProtection;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (joinProtection.getConfig().getBoolean("cancel.on-attack") && (event.getDamager() instanceof Player player) && JoinListener.invinciblePlayers.containsKey(player.getUniqueId()) && !event.getDamager().hasPermission("joinprotection.bypass.cancel-on-move")) {
            JoinListener.invinciblePlayers.remove(player.getUniqueId());
            player.sendActionBar(MiniMessage.miniMessage().deserialize(joinProtection.getConfig().getString("messages.protectionDeactivatedAttack")));
        }

        if (joinProtection.getConfig().getBoolean("sound.enabled") && (event.getEntity() instanceof Player player) && JoinListener.invinciblePlayers.containsKey(player.getUniqueId()) && (event.getDamager() instanceof Player attacker)) {
            String sound = joinProtection.getConfig().getString("sound.type");
            float volume = (float) joinProtection.getConfig().getDouble("sound.volume");
            float pitch = (float) joinProtection.getConfig().getDouble("sound.pitch");

            attacker.playSound(player, Sound.valueOf(sound), volume, pitch);
        }

        if (joinProtection.getConfig().getBoolean("modules.disable_damage_by_entities") && (event.getEntity() instanceof Player player) && JoinListener.invinciblePlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (joinProtection.getConfig().getBoolean("modules.disable_damage_by_blocks") && JoinListener.invinciblePlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (joinProtection.getConfig().getBoolean("modules.disable_damage") && JoinListener.invinciblePlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityTargetPlayer(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) {
            return;
        }

        if (joinProtection.getConfig().getBoolean("modules.disable_entity_targeting") && JoinListener.invinciblePlayers.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
