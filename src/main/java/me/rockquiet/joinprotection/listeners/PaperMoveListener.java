package me.rockquiet.joinprotection.listeners;

import me.rockquiet.joinprotection.ProtectionHandler;
import me.rockquiet.joinprotection.configuration.Config;
import me.rockquiet.joinprotection.configuration.ConfigManager;
import me.rockquiet.joinprotection.configuration.Permissions;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PaperMoveListener implements Listener {

    private final ConfigManager configManager;
    private final ProtectionHandler protectionHandler;

    public PaperMoveListener(ConfigManager configManager, ProtectionHandler protectionHandler) {
        this.configManager = configManager;
        this.protectionHandler = protectionHandler;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location fromLocation = event.getFrom();
        if (!event.hasChangedPosition() || fromLocation.distanceSquared(event.getTo()) < 0.01) return;

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (player.hasPermission(Permissions.BYPASS_CANCEL_ON_MOVE)) return;
        if (!protectionHandler.hasProtection(playerUUID)) return;

        Config config = configManager.get();
        if (!config.cancel.onMove.enabled) return;

        // fix for player getting pushed by an entity
        if (config.cancel.onMove.disablePushing && player.getNearbyEntities(0.5, 0.5, 0.5).stream().anyMatch(LivingEntity.class::isInstance)) {
            player.teleportAsync(fromLocation);
            return;
        }

        Location joinLocation = protectionHandler.getLocation(playerUUID);
        Location playerLocation = player.getLocation();

        if (config.cancel.onMove.ignoreYAxis) {
            joinLocation.setY(playerLocation.getY());
        }

        double distance = config.cancel.onMove.distance;
        double distanceSquared = distance * distance;

        if (joinLocation.getWorld() != playerLocation.getWorld() || joinLocation.distanceSquared(playerLocation) >= distanceSquared) {
            protectionHandler.cancelProtection(player, config.messages.protectionDeactivated);
        }
    }
}
