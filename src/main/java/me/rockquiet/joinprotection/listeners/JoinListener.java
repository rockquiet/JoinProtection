package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public JoinListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.USE)) return;

        if (!protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission(Permissions.BYPASS_WORLD_LIST)) {
            return;
        }

        if (player.hasPlayedBefore() && configManager.get().plugin.firstJoinOnly) {
            return;
        }

        protectionHandler.startJoinProtection(player);
    }
}
