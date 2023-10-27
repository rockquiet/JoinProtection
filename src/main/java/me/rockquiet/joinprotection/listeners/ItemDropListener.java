package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public ItemDropListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (protectionHandler.isEventCancelled(event.getPlayer().getUniqueId(), "modules.disable-item-drops")) {
            event.setCancelled(true);
        }
    }
}
