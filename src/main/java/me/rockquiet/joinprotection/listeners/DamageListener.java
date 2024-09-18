package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.JoinProtection;
import me.rockquiet.joinprotection.MessageManager;
import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        final UUID protectedPlayerUUID = protectedPlayer.getUniqueId();
        final Config config = plugin.config();

        if (protectionHandler.hasProtection(protectedPlayerUUID) && event.getDamager() instanceof Player attacker) {

            messageManager.sendMessage(attacker, config.messages.cannotHurt, Placeholder.unparsed("player", protectedPlayer.getName()));

            if (config.sound.enabled) {
                Sound sound = Sound.sound(Key.key(config.sound.type), Sound.Source.PLAYER, (float) config.sound.volume, (float) config.sound.pitch);
                plugin.adventure().player(attacker).playSound(sound, protectedPlayer);
            }
        }

        if (protectionHandler.isEventCancelled(protectedPlayerUUID, config.modules.disableDamageByEntities)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), plugin.config().modules.disableDamageByBlocks)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), plugin.config().modules.disableDamage)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityTargetPlayer(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), plugin.config().modules.disableEntityTargeting.enabled)) {
            event.setCancelled(true);
        }
    }
}
