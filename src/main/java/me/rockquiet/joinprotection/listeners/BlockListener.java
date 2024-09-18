package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public BlockListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
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
        final Config config = configManager.get();
        protectionHandler.cancelProtectionIfEnabled(
                player,
                Permissions.BYPASS_CANCEL_ON_BLOCK_INTERACT,
                config.cancel.onBlockInteract,
                config.messages.protectionDeactivated
        );
    }
}
