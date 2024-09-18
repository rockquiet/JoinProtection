package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class PaperItemPickupListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public PaperItemPickupListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (protectionHandler.isEventCancelled(event.getPlayer().getUniqueId(), configManager.get().modules.disableItemPickup)) {
            event.setCancelled(true);
        }
    }
}
