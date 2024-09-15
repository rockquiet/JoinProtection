package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.MessageManager;
import me.rockquiet.joinprotection.ProtectionHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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

            messageManager.sendMessage(config, attacker, "messages.cannotHurt", Placeholder.unparsed("player", protectedPlayer.getName()));

            if (config.getBoolean("sound.enabled")) {
                Sound sound = Sound.sound(Key.key(config.getString("sound.type")), Sound.Source.PLAYER, (float) config.getDouble("sound.volume"), (float) config.getDouble("sound.pitch"));
                plugin.adventure().player(attacker).playSound(sound, protectedPlayer);
            }
        }

        if (protectionHandler.isEventCancelled(protectedPlayerUUID, "modules.disable-damage-by-entities")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable-damage-by-blocks")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable-damage")) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityTargetPlayer(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable-entity-targeting.enabled")) {
            event.setCancelled(true);
        }
    }
}
