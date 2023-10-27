package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class PaperItemPickupListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public PaperItemPickupListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (protectionHandler.isEventCancelled(event.getPlayer().getUniqueId(), "modules.disable-item-pickup")) {
            event.setCancelled(true);
        }
    }
}
