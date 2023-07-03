package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ProtectionHandler protectionHandler;

    public JoinListener(ProtectionHandler protectionHandler) {
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("joinprotection.use") || !protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission("joinprotection.bypass.world-list")) {
            return;
        }

        protectionHandler.startProtection(player);
    }
}
