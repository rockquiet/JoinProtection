package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.MessageManager;
import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.UUID;

public class DamageListener implements Listener {

    private final JoinProtection plugin;
    private final MessageManager messageManager;
    private final ProtectionHandler protectionHandler;

    public DamageListener(JoinProtection joinProtection,
                          MessageManager messageManager,
                          ProtectionHandler protectionHandler) {
        this.plugin = joinProtection;
        this.messageManager = messageManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player protectedPlayer)) return;
        UUID protectedPlayerUUID = protectedPlayer.getUniqueId();

        if (protectionHandler.hasProtection(protectedPlayerUUID) && event.getDamager() instanceof Player attacker) {
            FileConfiguration config = plugin.getConfig();

            messageManager.sendMessage(config, attacker, "messages.cannotHurt", "%player%", protectedPlayer.getName());

            if (config.getBoolean("sound.enabled")) {
                Sound sound = Sound.valueOf(config.getString("sound.type"));
                float volume = (float) config.getDouble("sound.volume");
                float pitch = (float) config.getDouble("sound.pitch");

                attacker.playSound(protectedPlayer, sound, volume, pitch);
            }
        }

        if (protectionHandler.isEventCancelled(protectedPlayerUUID, "modules.disable_damage_by_entities")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable_damage_by_blocks")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable_damage")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityTargetPlayer(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable_entity_targeting")) {
            event.setCancelled(true);
        }
    }
}
