package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener extends MoveHandler implements Listener {

    public MoveListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        super(configManager, protectionHandler);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        handlePlayerMove(event);
    }
}
