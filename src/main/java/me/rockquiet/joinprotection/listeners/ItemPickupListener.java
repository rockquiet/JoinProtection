package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public ItemPickupListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (protectionHandler.isEventCancelled(player.getUniqueId(), "modules.disable-item-pickup")) {
            event.setCancelled(true);
        }
    }
}
