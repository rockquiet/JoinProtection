package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public BlockListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        cancelOnInteraction(event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        cancelOnInteraction(event.getPlayer());
    }

    private void cancelOnInteraction(Player player) {
        protectionHandler.cancelProtectionIfEnabled(
                player,
                "joinprotection.bypass.cancel-on-block-interact",
                "cancel.on-block-interact",
                "messages.protectionDeactivated"
        );
    }
}
