package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PaperMoveListener extends MoveHandler implements Listener {

    public PaperMoveListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        super(configManager, protectionHandler);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        handlePlayerMove(event);
    }

    @Override
    public boolean hasNotMoved(PlayerMoveEvent event) {
        return !event.hasChangedPosition();
    }

    @Override
    public void teleport(Player player, Location fromLocation) {
        player.teleportAsync(fromLocation);
    }
}
