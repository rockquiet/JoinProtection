package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public WorldChangeListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission(Permissions.USE_WORLD)) return;

        if (!protectionHandler.isEnabledInWorld(player.getWorld()) && !player.hasPermission(Permissions.BYPASS_WORLD_LIST)) {
            return;
        }

        protectionHandler.startWorldProtection(player);

        if (configManager.get().modules.disableEntityTargeting.enabled) {
            protectionHandler.clearMobTargets(player);
        }
    }
}
